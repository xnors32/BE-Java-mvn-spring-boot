package com.inventorilab.service.impl;

import com.inventorilab.dto.request.ShopOrderRequest;
import com.inventorilab.dto.response.ShopOrderResponse;
import com.inventorilab.entity.ShopOrder;
import com.inventorilab.entity.ShopProduct;
import com.inventorilab.entity.User;
import com.inventorilab.exception.ResourceNotFoundException;
import com.inventorilab.mapper.ShopOrderMapper;
import com.inventorilab.repository.ShopOrderRepository;
import com.inventorilab.repository.ShopProductRepository;
import com.inventorilab.repository.UserRepository;
import com.inventorilab.service.interfaces.ShopOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopOrderServiceImpl implements ShopOrderService {

    private final ShopOrderRepository shopOrderRepository;
    private final ShopProductRepository shopProductRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ShopOrderResponse create(ShopOrderRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan!"));

        ShopProduct product = shopProductRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produk dengan ID " + request.getProductId() + " tidak ditemukan!"));

        if (product.getStok() < request.getQuantity()) {
            throw new IllegalStateException("Stok tidak mencukupi! Stok tersedia: " + product.getStok());
        }

        BigDecimal totalHarga = product.getHarga().multiply(BigDecimal.valueOf(request.getQuantity()));

        product.setStok(product.getStok() - request.getQuantity());
        shopProductRepository.save(product);

        ShopOrder order = ShopOrder.builder()
                .user(user)
                .product(product)
                .quantity(request.getQuantity())
                .totalHarga(totalHarga)
                .status("MENUNGGU")
                .build();

        ShopOrder saved = shopOrderRepository.save(order);
        return ShopOrderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShopOrderResponse> getAll(Pageable pageable) {
        return shopOrderRepository.findAll(pageable)
                .map(ShopOrderMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShopOrderResponse> getMyOrders(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan!"));
        return shopOrderRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(ShopOrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ShopOrderResponse updateStatus(Long id, String status) {
        ShopOrder order = shopOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pesanan dengan ID " + id + " tidak ditemukan!"));

        if (status.equals("DIBATALKAN") && !order.getStatus().equals("MENUNGGU")) {
            throw new IllegalStateException("Pesanan sudah " + order.getStatus() + ", tidak bisa dibatalkan!");
        }

        if (status.equals("DISIAPKAN") && !order.getStatus().equals("MENUNGGU")) {
            throw new IllegalStateException("Pesanan sudah " + order.getStatus() + ", tidak bisa disiapkan!");
        }

        if (status.equals("SELESAI") && !order.getStatus().equals("DISIAPKAN")) {
            throw new IllegalStateException("Pesanan harus DISIAPKAN dulu sebelum SELESAI!");
        }

        order.setStatus(status);
        ShopOrder updated = shopOrderRepository.save(order);
        return ShopOrderMapper.toResponse(updated);
    }
}
