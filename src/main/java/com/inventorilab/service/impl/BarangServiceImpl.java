package com.inventorilab.service.impl;

import com.inventorilab.dto.request.BarangRequest;
import com.inventorilab.dto.response.BarangResponse;
import com.inventorilab.entity.Barang;
import com.inventorilab.entity.Kategori;
import com.inventorilab.exception.BadRequestException;
import com.inventorilab.exception.ResourceNotFoundException;
import com.inventorilab.mapper.BarangMapper;
import com.inventorilab.repository.BarangRepository;
import com.inventorilab.repository.KategoriRepository;
import com.inventorilab.service.interfaces.BarangService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BarangServiceImpl implements BarangService {

    private final BarangRepository barangRepository;
    private final KategoriRepository kategoriRepository;

    @Override
    @Transactional
    public BarangResponse create(BarangRequest request) {
        if (barangRepository.existsByKodeBarang(request.getKodeBarang())) {
            throw new BadRequestException("Kode barang '" + request.getKodeBarang() + "' sudah terdaftar!");
        }

        Kategori kategori = kategoriRepository.findById(request.getIdKategori())
                .orElseThrow(() -> new ResourceNotFoundException("Kategori dengan ID " + request.getIdKategori() + " tidak ditemukan!"));

        Barang barang = Barang.builder()
                .kategori(kategori)
                .namaBarang(request.getNamaBarang())
                .kodeBarang(request.getKodeBarang())
                .jumlahTotal(request.getJumlahTotal())
                .jumlahTersedia(request.getJumlahTotal()) // Initially available stock = total stock
                .harga(request.getHarga())
                .kondisi(request.getKondisi())
                .lokasi(request.getLokasi())
                .build();

        Barang savedBarang = barangRepository.save(barang);
        return BarangMapper.toResponse(savedBarang);
    }

    @Override
    @Transactional
    public BarangResponse update(Long id, BarangRequest request) {
        Barang barang = barangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barang dengan ID " + id + " tidak ditemukan!"));

        if (barangRepository.existsByKodeBarangAndIdNot(request.getKodeBarang(), id)) {
            throw new BadRequestException("Kode barang '" + request.getKodeBarang() + "' sudah terdaftar!");
        }

        Kategori kategori = kategoriRepository.findById(request.getIdKategori())
                .orElseThrow(() -> new ResourceNotFoundException("Kategori dengan ID " + request.getIdKategori() + " tidak ditemukan!"));

        // Calculate available stock changes (delta)
        int stockDelta = request.getJumlahTotal() - barang.getJumlahTotal();
        int newJumlahTersedia = barang.getJumlahTersedia() + stockDelta;

        if (newJumlahTersedia < 0) {
            throw new BadRequestException("Jumlah total baru tidak boleh kurang dari unit yang sedang aktif dipinjam! " +
                    "(Jumlah dipinjam saat ini: " + (barang.getJumlahTotal() - barang.getJumlahTersedia()) + ")");
        }

        barang.setKategori(kategori);
        barang.setNamaBarang(request.getNamaBarang());
        barang.setKodeBarang(request.getKodeBarang());
        barang.setJumlahTotal(request.getJumlahTotal());
        barang.setJumlahTersedia(newJumlahTersedia);
        barang.setHarga(request.getHarga());
        barang.setKondisi(request.getKondisi());
        barang.setLokasi(request.getLokasi());

        Barang updatedBarang = barangRepository.save(barang);
        return BarangMapper.toResponse(updatedBarang);
    }

    @Override
    @Transactional(readOnly = true)
    public BarangResponse getById(Long id) {
        Barang barang = barangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barang dengan ID " + id + " tidak ditemukan!"));
        return BarangMapper.toResponse(barang);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BarangResponse> getAll(Pageable pageable) {
        Page<Barang> barangPage = barangRepository.findAll(pageable);
        return barangPage.map(BarangMapper::toResponse);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Barang barang = barangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barang dengan ID " + id + " tidak ditemukan!"));

        // Check if there are active loans
        if (barang.getJumlahTersedia() < barang.getJumlahTotal()) {
            throw new BadRequestException("Barang tidak dapat dihapus karena masih ada unit yang sedang dipinjam!");
        }

        barangRepository.delete(barang);
    }
}
