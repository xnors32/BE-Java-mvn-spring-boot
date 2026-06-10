package com.inventorilab.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopProductResponse {
    private Long id;
    private String namaProduk;
    private String deskripsi;
    private BigDecimal harga;
    private Integer stok;
    private String gambarUrl;
    private String kategori;
    private String tags;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
