package com.enterprise.cartcheckout.config;

import com.enterprise.cartcheckout.entity.Product;
import com.enterprise.cartcheckout.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        productRepository.deleteAll(); // Force re-seed with premium images
        if (productRepository.count() == 0) {
            log.info("Seeding database with default products...");
            List<Product> dummyProducts = Arrays.asList(
                    Product.builder()
                            .sku("LAP-001")
                            .name("Enterprise Laptop Pro X")
                            .price(new BigDecimal("1299.99"))
                            .discountPrice(new BigDecimal("999.99"))
                            .stock(15)
                            .categoryId("Electronics")
                            .images(Arrays.asList("https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400&q=80"))
                            .description("High-performance laptop for enterprise use.")
                            .active(true)
                            .build(),
                    Product.builder()
                            .sku("MOU-002")
                            .name("Ergonomic Wireless Mouse")
                            .price(new BigDecimal("49.99"))
                            .stock(50)
                            .categoryId("Accessories")
                            .images(Arrays.asList("https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=400&q=80"))
                            .description("Comfortable wireless mouse for all-day use.")
                            .active(true)
                            .build(),
                    Product.builder()
                            .sku("KEY-003")
                            .name("Mechanical Keyboard Blue")
                            .price(new BigDecimal("129.50"))
                            .discountPrice(new BigDecimal("89.99"))
                            .stock(30)
                            .categoryId("Accessories")
                            .images(Arrays.asList("https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=400&q=80"))
                            .description("Tactile blue-switch mechanical keyboard.")
                            .active(true)
                            .build(),
                    Product.builder()
                            .sku("MON-004")
                            .name("4K UltraSharp Monitor 27\"")
                            .price(new BigDecimal("450.00"))
                            .stock(8)
                            .categoryId("Electronics")
                            .images(Arrays.asList("https://images.unsplash.com/photo-1527443224154-c4a573d5e6b0?w=400&q=80"))
                            .description("Crystal-clear 4K display for professionals.")
                            .active(true)
                            .build(),
                    Product.builder()
                            .sku("HDR-005")
                            .name("Noise Cancelling Headphones")
                            .price(new BigDecimal("299.99"))
                            .discountPrice(new BigDecimal("199.99"))
                            .stock(20)
                            .categoryId("Audio")
                            .images(Arrays.asList("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&q=80"))
                            .description("Premium ANC headphones with 30-hour battery.")
                            .active(true)
                            .build(),
                    Product.builder()
                            .sku("WEB-006")
                            .name("HD Webcam 1080p")
                            .price(new BigDecimal("79.99"))
                            .stock(40)
                            .categoryId("Accessories")
                            .images(Arrays.asList("https://images.unsplash.com/photo-1611532736597-de2d4265fba3?w=400&q=80"))
                            .description("Sharp HD webcam with built-in mic.")
                            .active(true)
                            .build(),
                    Product.builder()
                            .sku("CHR-007")
                            .name("USB-C Fast Charger 65W")
                            .price(new BigDecimal("39.99"))
                            .discountPrice(new BigDecimal("24.99"))
                            .stock(100)
                            .categoryId("Accessories")
                            .images(Arrays.asList("https://images.unsplash.com/photo-1591488320449-011701bb6704?w=400&q=80"))
                            .description("65W GaN USB-C charger, ultra-compact.")
                            .active(true)
                            .build(),
                    Product.builder()
                            .sku("SSD-008")
                            .name("Portable SSD 1TB")
                            .price(new BigDecimal("119.99"))
                            .discountPrice(new BigDecimal("89.99"))
                            .stock(25)
                            .categoryId("Storage")
                            .images(Arrays.asList("https://images.unsplash.com/photo-1531492746076-161ca9bcad58?w=400&q=80"))
                            .description("Ultra-fast 1TB portable SSD, USB 3.2.")
                            .active(true)
                            .build()
            );
            productRepository.saveAll(dummyProducts);
            log.info("Database seeded successfully with {} products.", dummyProducts.size());
        }
    }
}
