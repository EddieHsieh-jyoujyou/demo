package com.jyoujyou.demo.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jyoujyou.demo.product.exception.NotFoundException;
import com.jyoujyou.demo.product.exception.UnprocessableEntityException;

@Service
public class ProductService {

    // dependency injection
    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product request) {
        boolean isIdDuplicate = productRepository.find(request.getId()).isPresent();
        if (isIdDuplicate) {
            throw new UnprocessableEntityException("The id of the product is duplicated.");
        }

        Product product = new Product(request.getId(), request.getName(), request.getPrice());
        return productRepository.insert(product);
    }

    public Product getProduct(String id) {
        return productRepository.find(id).orElseThrow(() -> new NotFoundException("Cannot find product"));
    }

    public Product replaceProduct(String id, Product request) {
        Product product = getProduct(id);
        return productRepository.replace(product.getId(), request);
    }

    public void deleteProduct(String id) {
        Product product = getProduct(id);
        productRepository.delete(product.getId());
    }

    public List<Product> getProducts(ProductQueryParameter param) {
        return productRepository.find(param);
    }
}
