package com.inventorilab.service.impl;

import com.inventorilab.dto.request.ShopProductRequest;
import com.inventorilab.dto.response.ShopProductResponse;
import com.inventorilab.entity.ShopProduct;
import com.inventorilab.entity.User;
import com.inventorilab.exception.ResourceNotFoundException;
import com.inventorilab.mapper.ShopProductMapper;
import com.inventorilab.repository.ShopProductRepository;
import com.inventorilab.repository.UserRepository;
import com.inventorilab.service.interfaces.ShopProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopProductServiceImpl implements ShopProductService {

    private final ShopProductRepository shopProductRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ShopProductResponse create(ShopProductRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan!"));

        ShopProduct product = ShopProduct.builder()
                .namaProduk(request.getNamaProduk())
                .deskripsi(request.getDeskripsi())
                .harga(request.getHarga())
                .stok(request.getStok())
                .gambarUrl(request.getGambarUrl())
                .kategori(request.getKategori())
                .tags(request.getTags())
                .createdBy(user)
                .build();

        ShopProduct saved = shopProductRepository.save(product);
        return ShopProductMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShopProductResponse> getAll(Pageable pageable) {
        return shopProductRepository.findAll(pageable)
                .map(ShopProductMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ShopProductResponse getById(Long id) {
        ShopProduct product = shopProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produk shop dengan ID " + id + " tidak ditemukan!"));
        return ShopProductMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ShopProductResponse update(Long id, ShopProductRequest request) {
        ShopProduct product = shopProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produk shop dengan ID " + id + " tidak ditemukan!"));

        product.setNamaProduk(request.getNamaProduk());
        product.setDeskripsi(request.getDeskripsi());
        product.setHarga(request.getHarga());
        product.setStok(request.getStok());
        product.setGambarUrl(request.getGambarUrl());
        product.setKategori(request.getKategori());
        product.setTags(request.getTags());

        ShopProduct updated = shopProductRepository.save(product);
        return ShopProductMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ShopProduct product = shopProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produk shop dengan ID " + id + " tidak ditemukan!"));
        shopProductRepository.delete(product);
    }
}
