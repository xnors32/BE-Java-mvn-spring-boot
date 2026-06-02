package com.inventorilab.service.impl;

import com.inventorilab.dto.response.DetailPeminjamanResponse;
import com.inventorilab.entity.DetailPeminjaman;
import com.inventorilab.enums.KondisiBarang;
import com.inventorilab.exception.ResourceNotFoundException;
import com.inventorilab.mapper.PeminjamanMapper;
import com.inventorilab.repository.DetailPeminjamanRepository;
import com.inventorilab.service.interfaces.DetailPeminjamanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DetailPeminjamanServiceImpl implements DetailPeminjamanService {

    private final DetailPeminjamanRepository detailPeminjamanRepository;

    @Override
    @Transactional(readOnly = true)
    public DetailPeminjamanResponse getById(Long id) {
        DetailPeminjaman detail = detailPeminjamanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detail peminjaman dengan ID " + id + " tidak ditemukan!"));
        return PeminjamanMapper.toDetailResponse(detail);
    }

    @Override
    @Transactional
    public DetailPeminjamanResponse updateKondisi(Long id, KondisiBarang kondisiKembali) {
        DetailPeminjaman detail = detailPeminjamanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detail peminjaman dengan ID " + id + " tidak ditemukan!"));
        detail.setKondisiKembali(kondisiKembali);
        DetailPeminjaman saved = detailPeminjamanRepository.save(detail);
        return PeminjamanMapper.toDetailResponse(saved);
    }
}
