package com.inventorilab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shop_products")
public class ShopProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_produk", nullable = false)
    private String namaProduk;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal harga;

    @Column(nullable = false)
    private Integer stok;

    @Column(name = "gambar_url", columnDefinition = "TEXT")
    private String gambarUrl;

    @Column
    private String kategori;

    @Column(columnDefinition = "TEXT")
    private String tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
