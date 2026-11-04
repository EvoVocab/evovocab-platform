# Hướng dẫn triển khai RESTful API với Spring Boot 3, DDD và Hexagonal Architecture

Tài liệu này cung cấp hướng dẫn chi tiết để xây dựng một RESTful API tuân thủ **Domain-Driven Design (DDD)** và **Hexagonal Architecture (Ports and Adapters)**.

## 🛠 Tech Stack

- **Java 21**: Tận dụng các tính năng mới như `record`, pattern matching.
- **Spring Boot 3**: Framework cốt lõi (sử dụng namespace `jakarta.*`).
- **Spring Data JPA & Hibernate**: Quản lý thao tác cơ sở dữ liệu.
- **MySQL**: Cơ sở dữ liệu quan hệ.
- **MapStruct**: Mapping object giữa các layer.
- **Spring ApplicationEventPublisher**: Triển khai Domain Event.

---

## Kiến trúc DDD + Hexagonal

![Kiến trúc tổng thể](https://miro.medium.com/v2/resize:fit:1100/format:webp/1*L8_40RsZJvYSl8sILtUcsQ.png)

## 📁 Cấu trúc thư mục (Project Structure)

Cấu trúc project được chia thành 3 phần chính của Hexagonal: **Domain**, **Application**, và **Infrastructure**.

```text
src/main/java/com/example/demo/product
│
├── domain                  # Core Business Logic (KHÔNG phụ thuộc framework)
│   ├── model               # Entities, Value Objects
│   ├── exception           # Domain Exceptions
│   └── repository          # Outbound Ports (Interfaces cho DB)
│
├── application             # Use Cases & Application Logic
│   ├── port
│   │   ├── in              # Inbound Ports (Use case interfaces cho Web)
│   │   └── out             # Outbound Ports (External services, Event publishers)
│   └── service             # Application Services (Implement Inbound Ports)
│
└── infrastructure          # Adapters (Phụ thuộc framework: Spring, JPA, Web)
    ├── adapter
    │   ├── in              # Inbound Adapters (REST Controllers)
    │   │   └── web
    │   │       └── dto     # Request/Response DTOs
    │   └── out             # Outbound Adapters
    │       ├── persistence # JPA Entities, Repositories, DB Adapters
    │       └── event       # Event Listeners, Spring Event Publisher Adapters
    ├── mapper              # MapStruct Mappers
    └── config              # Spring Configurations
```

---

## 1. Domain Layer (Lớp cốt lõi)

Lớp này chứa business logic nguyên thủy, **không được có bất kỳ annotation nào của Spring hay JPA**.

**Domain Entity: `Product.java`**

```java
package com.example.demo.product.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

  private UUID id;
  private String name;
  private BigDecimal price;

  // Constructors
  public Product(UUID id, String name, BigDecimal price) {
    if (price.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Price must be greater than zero");
    }
    this.id = id;
    this.name = name;
    this.price = price;
  }

  // Getters and Setters... (Omitted for brevity)
  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }
}
```

**Outbound Port (Repository Interface): `ProductRepository.java`**

```java
package com.example.demo.product.domain.repository;

import com.example.demo.product.domain.model.Product;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
  Product save(Product product);
  Optional<Product> findById(UUID id);
}
```

---

## 2. Application Layer

Lớp này điều phối các domain model. Nó định nghĩa các Use Case (Inbound Port) và Event (Outbound Port).

**Event Model (Java 21 Record): `ProductCreatedEvent.java`**

```java
package com.example.demo.product.application.port.out;

import java.util.UUID;

// Sử dụng Record của Java 21 cho immutable event
public record ProductCreatedEvent(UUID productId, String name) {}
```

**Outbound Port (Event Publisher Interface): `EventPublisherPort.java`**

```java
package com.example.demo.product.application.port.out;

public interface EventPublisherPort {
  void publish(Object event);
}
```

**Outbound Port kết hợp Anti-Corruption Layer: `ProductFacadePort.java`**

```java
package com.example.demo.order.application.port.out;

public interface ProductFacadePort {
  boolean checkProductAvailability(UUID productId);
}
```

**Inbound Port (Use Case Interface): `CreateProductUseCase.java`**

```java
package com.example.demo.product.application.port.in;

import com.example.demo.product.domain.model.Product;
import java.math.BigDecimal;

public interface CreateProductUseCase {
  Product createProduct(String name, BigDecimal price);
}
```

**Application Service (Implement Use Case): `ProductService.java`**

```java
package com.example.demo.product.application.service;

import com.example.demo.product.application.port.in.CreateProductUseCase;
import com.example.demo.product.application.port.out.EventPublisherPort;
import com.example.demo.product.application.port.out.ProductCreatedEvent;
import com.example.demo.product.domain.model.Product;
import com.example.demo.product.domain.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService implements CreateProductUseCase {

  private final ProductRepository productRepository;
  private final EventPublisherPort eventPublisher;

  public ProductService(ProductRepository productRepository, EventPublisherPort eventPublisher) {
    this.productRepository = productRepository;
    this.eventPublisher = eventPublisher;
  }

  @Override
  @Transactional
  public Product createProduct(String name, BigDecimal price) {
    // 1. Tạo Domain Entity
    Product newProduct = new Product(UUID.randomUUID(), name, price);

    // 2. Lưu thông qua Port (DIP - Dependency Inversion Principle)
    Product savedProduct = productRepository.save(newProduct);

    // 3. Publish Event
    eventPublisher.publish(new ProductCreatedEvent(savedProduct.getId(), savedProduct.getName()));

    return savedProduct;
  }
}
```

---

## 3. Infrastructure Layer (Adapters)

### 3.1 Persistence Adapter (JPA + MySQL)

**JPA Entity: `ProductJpaEntity.java`**

```java
package com.example.demo.product.infrastructure.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductJpaEntity {

  @Id
  private UUID id;

  private String name;
  private BigDecimal price;

  // JPA requires default constructor
  public ProductJpaEntity() {}

  // Getters and Setters...
}
```

**Spring Data Repository: `SpringDataProductRepository.java`**

```java
package com.example.demo.product.infrastructure.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProductRepository extends JpaRepository<ProductJpaEntity, UUID> {}
```

**MapStruct Mapper: `ProductMapper.java`**

```java
package com.example.demo.product.infrastructure.mapper;

import com.example.demo.product.domain.model.Product;
import com.example.demo.product.infrastructure.adapter.in.web.dto.ProductResponse;
import com.example.demo.product.infrastructure.adapter.out.persistence.ProductJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  // Domain <-> JPA Entity
  ProductJpaEntity toJpaEntity(Product product);
  Product toDomainEntity(ProductJpaEntity jpaEntity);

  // Domain -> DTO
  ProductResponse toResponseDto(Product product);
}
```

**Persistence Adapter (Implement Domain Repository): `ProductPersistenceAdapter.java`**

```java
package com.example.demo.product.infrastructure.adapter.out.persistence;

import com.example.demo.product.domain.model.Product;
import com.example.demo.product.domain.repository.ProductRepository;
import com.example.demo.product.infrastructure.mapper.ProductMapper;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceAdapter implements ProductRepository {

  private final SpringDataProductRepository repository;
  private final ProductMapper mapper;

  public ProductPersistenceAdapter(SpringDataProductRepository repository, ProductMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Product save(Product product) {
    ProductJpaEntity entity = mapper.toJpaEntity(product);
    ProductJpaEntity savedEntity = repository.save(entity);
    return mapper.toDomainEntity(savedEntity);
  }

  @Override
  public Optional<Product> findById(UUID id) {
    return repository.findById(id).map(mapper::toDomainEntity);
  }
}
```

### 3.2 Event Adapter (Spring ApplicationEventPublisher)

**Event Publisher Adapter:**

```java
package com.example.demo.product.infrastructure.adapter.out.event;

import com.example.demo.product.application.port.out.EventPublisherPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventPublisherAdapter implements EventPublisherPort {

  private final ApplicationEventPublisher applicationEventPublisher;

  public SpringEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void publish(Object event) {
    applicationEventPublisher.publishEvent(event);
  }
}
```

**Event Listener (Example of handling the event):**

```java
package com.example.demo.product.infrastructure.adapter.out.event;

import com.example.demo.product.application.port.out.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {

  private static final Logger log = LoggerFactory.getLogger(ProductEventListener.class);

  @EventListener
  public void handleProductCreatedEvent(ProductCreatedEvent event) {
    log.info(
      "Event Received: New Product created with ID: {} and Name: {}",
      event.productId(),
      event.name()
    );
    // Có thể thực hiện gửi email, gọi external API, v.v. tại đây
  }
}
```

### 3.3 Web Adapter (REST Controller)

**DTOs (Java 21 Records):**

```java
package com.example.demo.product.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductRequest(String name, BigDecimal price) {}

public record ProductResponse(UUID id, String name, BigDecimal price) {}
```

### 3.3 Facade Adapter

```java
package com.example.demo.order.infrastructure.adapter.out;

import com.example.demo.order.application.port.out.ProductFacadePort;
import com.example.demo.product.application.port.in.GetProductUseCase;
import org.springframework.stereotype.Component;

@Component
public class ProductFacadeAdapter implements ProductFacadePort {

  private final GetProductUseCase getProductUseCase;

  public ProductFacadeAdapter(GetProductUseCase getProductUseCase) {
    this.getProductUseCase = getProductUseCase;
  }

  @Override
  public boolean checkProductAvailability(UUID productId) {
    // Chuyển đổi dữ liệu và gọi sang Inbound Port của Module Product
    var product = getProductUseCase.getProductById(productId);
    return product != null;
  }
}
```

**REST Controller:**

```java
package com.example.demo.product.infrastructure.adapter.in.web;

import com.example.demo.product.application.port.in.CreateProductUseCase;
import com.example.demo.product.domain.model.Product;
import com.example.demo.product.infrastructure.adapter.in.web.dto.CreateProductRequest;
import com.example.demo.product.infrastructure.adapter.in.web.dto.ProductResponse;
import com.example.demo.product.infrastructure.mapper.ProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final CreateProductUseCase createProductUseCase;
  private final ProductMapper mapper;

  public ProductController(CreateProductUseCase createProductUseCase, ProductMapper mapper) {
    this.createProductUseCase = createProductUseCase;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
    // Gọi Inbound Port (Use case)
    Product product = createProductUseCase.createProduct(request.name(), request.price());

    // Map Domain model ra DTO để trả về cho Client
    ProductResponse response = mapper.toResponseDto(product);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}
```

## Tóm tắt luồng hoạt động (Data Flow)

1. **Client** gửi POST request đến `ProductController` (Web Adapter).
2. `ProductController` nhận `CreateProductRequest` DTO, gọi interface `CreateProductUseCase` (Inbound Port).
3. `ProductService` (Application Layer) thực thi logic:
   - Khởi tạo object `Product` (Domain Model).
   - Gọi interface `ProductRepository` (Outbound Port).
4. `ProductPersistenceAdapter` (Infrastructure) nhận `Product`, dùng `MapStruct` chuyển sang `ProductJpaEntity`, lưu bằng `SpringDataProductRepository`, sau đó chuyển ngược kết quả về Domain model.
5. `ProductService` gọi `EventPublisherPort` để bắn `ProductCreatedEvent`.
6. `SpringEventPublisherAdapter` nhận lệnh và sử dụng ApplicationEventPublisher của Spring để phát event.
7. `ProductEventListener` bắt được event và xử lý ngầm (ghi log).
8. `ProductController` dùng `MapStruct` biến đổi kết quả Domain Model thành `ProductResponse` và trả về HTTP 201 cho Client.
