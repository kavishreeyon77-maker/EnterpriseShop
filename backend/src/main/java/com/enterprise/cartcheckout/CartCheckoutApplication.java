package com.enterprise.cartcheckout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableCaching
public class CartCheckoutApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartCheckoutApplication.class, args);

        
    }
}
