package com.jyoujyou.demo.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
    private final List<Product> productDb = new ArrayList<>();

    // Fake data
    @PostConstruct
    private void initDb() {
        productDb.add(new Product("B0001", "TestB001", 10));
        productDb.add(new Product("B0002", "TestB002", 20));
        productDb.add(new Product("B0003", "TestB003", 30));
        productDb.add(new Product("B0004", "TestB004", 40));
        productDb.add(new Product("B0005", "TestB005", 50));
    }

    public Product insert(Product product) {
        productDb.add(product);
        return product;
    }

    public Product replace(String id, Product product) {
        Optional<Product> productOp = find(id);
        productOp.ifPresent(p -> {
            p.setName(product.getName());
            p.setPrice(product.getPrice());
        });
        return product;
    }

    public void delete(String id) {
        productDb.removeIf(p -> p.getId().equals(id));
    }

    public Optional<Product> find(String id) {
        return productDb.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<Product> find(ProductQueryParameter param) {
        String keyword = Optional.ofNullable(param.getKeyword()).orElse("");
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();
        Comparator<Product> comparator = genSortComparator(orderBy, sortRule);

        return productDb.stream()
                .filter(p -> p.getName().contains(keyword))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private Comparator<Product> genSortComparator(String orderBy, String sortRule) {
        Comparator<Product> comparator = (p1, p2) -> 0;
        if (Objects.isNull(orderBy) || Objects.isNull(sortRule)) {
            return comparator;
        }

        if (orderBy.equalsIgnoreCase("price")) {
            comparator = Comparator.comparing(Product::getPrice);
        } else if  (orderBy.equalsIgnoreCase("name")) {
            comparator = Comparator.comparing(Product::getName);
        }
        return sortRule.equalsIgnoreCase("desc")
                ? comparator.reversed()
                : comparator;
    }
}
