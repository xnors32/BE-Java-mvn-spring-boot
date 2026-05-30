package com.inventorilab.service.impl;

import com.inventorilab.dto.request.KategoriRequest;
import com.inventorilab.dto.response.KategoriResponse;
import com.inventorilab.entity.Kategori;
import com.inventorilab.exception.BadRequestException;
import com.inventorilab.exception.ResourceNotFoundException;
import com.inventorilab.mapper.KategoriMapper;
import com.inventorilab.repository.KategoriRepository;
import com.inventorilab.service.interfaces.KategoriService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KategoriServiceImpl implements KategoriService {

    private final KategoriRepository kategoriRepository;

    @Override
    @Transactional
    public KategoriResponse create(KategoriRequest request) {
        if (kategoriRepository.existsByNamaKategori(request.getNamaKategori())) {
            throw new BadRequestException("Kategori dengan nama '" + request.getNamaKategori() + "' sudah ada!");
        }

        Kategori kategori = Kategori.builder()
                .namaKategori(request.getNamaKategori())
                .deskripsi(request.getDeskripsi())
                .build();

        Kategori savedKategori = kategoriRepository.save(kategori);
        return KategoriMapper.toResponse(savedKategori);
    }

    @Override
    @Transactional
    public KategoriResponse update(Long id, KategoriRequest request) {
        Kategori kategori = kategoriRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori dengan ID " + id + " tidak ditemukan!"));

        if (kategoriRepository.existsByNamaKategoriAndIdNot(request.getNamaKategori(), id)) {
            throw new BadRequestException("Kategori dengan nama '" + request.getNamaKategori() + "' sudah ada!");
        }

        kategori.setNamaKategori(request.getNamaKategori());
        kategori.setDeskripsi(request.getDeskripsi());

        Kategori updatedKategori = kategoriRepository.save(kategori);
        return KategoriMapper.toResponse(updatedKategori);
    }

    @Override
    @Transactional(readOnly = true)
    public KategoriResponse getById(Long id) {
        Kategori kategori = kategoriRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori dengan ID " + id + " tidak ditemukan!"));
        return KategoriMapper.toResponse(kategori);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KategoriResponse> getAll(Pageable pageable) {
        Page<Kategori> kategoriPage = kategoriRepository.findAll(pageable);
        return kategoriPage.map(KategoriMapper::toResponse);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!kategoriRepository.existsById(id)) {
            throw new ResourceNotFoundException("Kategori dengan ID " + id + " tidak ditemukan!");
        }
        kategoriRepository.deleteById(id);
    }
}
