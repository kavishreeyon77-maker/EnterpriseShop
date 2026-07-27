# Enterprise Shopping Cart & Checkout API

This is a production-ready, enterprise-grade Shopping Cart and Checkout API built using Spring Boot 3, Java 21, and MongoDB Atlas.

## Architecture & Tech Stack

- **Framework**: Spring Boot 3.2.x
- **Language**: Java 21
- **Database**: MongoDB Atlas (Cloud)
- **Caching**: Redis
- **Message Broker**: RabbitMQ
- **Security**: JWT Stateless Authentication
- **Documentation**: Swagger UI / OpenAPI 3
- **Object Mapping**: MapStruct
- **Testing**: JUnit 5, Mockito

## Key Features

- **Clean Architecture & SOLID Principles**: Controller -> Service -> Repository layers.
- **DTO Pattern**: Raw entities are never exposed to the frontend.
- **Caching**: Redis is used to cache Products, Coupons, and Carts.
- **Asynchronous Events**: RabbitMQ handles async order placement, payment, and notification events.
- **Schedulers**: Background tasks handle abandoned cart notifications, coupon expiration, and guest cart cleanup.
- **Audit Logging**: Request details and service operations are logged via AOP.
- **Graceful Fallbacks**: The `dev` profile runs without Redis and RabbitMQ dependencies via simple memory caches.

## How to Run

### Development Mode (Without Docker)

Uses simple in-memory caching and bypasses RabbitMQ. Connects to MongoDB Atlas.

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production / Docker Mode

Spins up Redis and RabbitMQ via Docker Compose.

```bash
docker-compose up -d
./mvnw spring-boot:run
```

## API Documentation

Once the app is running, access Swagger UI at:
http://localhost:8080/swagger-ui.html

## Modules

- Auth & Users
- Product Catalog & Inventory
- Cart & Checkout Session
- Orders & Payments
- Wishlist & Notifications
- Coupons
- Addresses
