package com.inventorilab.service.interfaces;

import com.inventorilab.dto.request.ShopOrderRequest;
import com.inventorilab.dto.response.ShopOrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShopOrderService {
    ShopOrderResponse create(ShopOrderRequest request, String userEmail);
    Page<ShopOrderResponse> getAll(Pageable pageable);
    List<ShopOrderResponse> getMyOrders(String userEmail);
    ShopOrderResponse updateStatus(Long id, String status);
}
