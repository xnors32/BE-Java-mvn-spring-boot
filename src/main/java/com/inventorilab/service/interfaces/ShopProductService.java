package com.inventorilab.service.interfaces;

import com.inventorilab.dto.request.ShopProductRequest;
import com.inventorilab.dto.response.ShopProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShopProductService {
    ShopProductResponse create(ShopProductRequest request, String userEmail);
    Page<ShopProductResponse> getAll(Pageable pageable);
    ShopProductResponse getById(Long id);
    ShopProductResponse update(Long id, ShopProductRequest request);
    void delete(Long id);
}
