package com.inventorilab.service.impl;

import com.inventorilab.dto.response.BarangResponse;
import com.inventorilab.mapper.BarangMapper;
import com.inventorilab.repository.BarangRepository;
import com.inventorilab.service.interfaces.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final BarangRepository barangRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BarangResponse> getBahanHabisPakai() {
        return barangRepository.findByHargaGreaterThanAndJumlahTersediaGreaterThan(BigDecimal.ZERO, 0)
                .stream()
                .map(BarangMapper::toResponse)
                .toList();
    }
}
