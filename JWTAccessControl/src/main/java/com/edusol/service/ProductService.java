package com.edusol.service;
import com.edusol.dto.Product;
import com.edusol.model.UserInfo;
import com.edusol.repository.UserInfoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductService {

    private List<Product> productList;

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    @PostConstruct
    public void loadProductsFromDB() {
        productList = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> Product.builder()
                        .productId(i)
                        .name("Product " + i)
                        .qty(random.nextInt(1, 11)) // quantity 1 to 10
                        .price(random.nextInt(100, 5001)) // price 100 to 5000
                        .build()
                ).collect(Collectors.toList());
    }

    public List<Product> getProducts() {
        return productList;
    }

    public Product getProduct(int id) {
        return productList.stream()
                .filter(product -> product.getProductId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product " + id + " not found"));
    }

    public String addUser(UserInfo userInfo) {
        if (userInfo.getPassword() == null || userInfo.getPassword().isEmpty()) {
            throw new RuntimeException("Password cannot be null or empty");
        }
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User added to system";
    }
}
