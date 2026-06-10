package com.inventorilab.repository;

import com.inventorilab.entity.ShopOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
    Page<ShopOrder> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    List<ShopOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
}
