package com.inventorilab.mapper;

import com.inventorilab.dto.response.ShopOrderResponse;
import com.inventorilab.entity.ShopOrder;

public class ShopOrderMapper {

    public static ShopOrderResponse toResponse(ShopOrder order) {
        if (order == null) return null;

        return ShopOrderResponse.builder()
                .id(order.getId())
                .productId(order.getProduct().getId())
                .productName(order.getProduct().getNamaProduk())
                .productImage(order.getProduct().getGambarUrl())
                .quantity(order.getQuantity())
                .totalHarga(order.getTotalHarga())
                .status(order.getStatus())
                .orderBy(order.getUser().getNama())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
