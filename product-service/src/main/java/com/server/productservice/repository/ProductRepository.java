package com.server.productservice.repository;

import com.server.productservice.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code);

    List<Product> findByCategory(String category);

    List<Product> findAllByName(String name);

    List<Product> findByAvailableTrue();

    boolean existsByCode(String code);
}
