package com.inventorilab.mapper;

import com.inventorilab.dto.response.ShopProductResponse;
import com.inventorilab.entity.ShopProduct;

public class ShopProductMapper {

    public static ShopProductResponse toResponse(ShopProduct product) {
        if (product == null) return null;

        return ShopProductResponse.builder()
                .id(product.getId())
                .namaProduk(product.getNamaProduk())
                .deskripsi(product.getDeskripsi())
                .harga(product.getHarga())
                .stok(product.getStok())
                .gambarUrl(product.getGambarUrl())
                .kategori(product.getKategori())
                .tags(product.getTags())
                .createdBy(product.getCreatedBy() != null ? product.getCreatedBy().getNama() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
