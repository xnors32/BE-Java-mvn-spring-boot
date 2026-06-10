package com.inventorilab.repository;

import com.inventorilab.entity.ShopProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopProductRepository extends JpaRepository<ShopProduct, Long> {
}
