package com.inventorilab.mapper;

import com.inventorilab.dto.response.DetailPeminjamanResponse;
import com.inventorilab.dto.response.PeminjamanResponse;
import com.inventorilab.entity.DetailPeminjaman;
import com.inventorilab.entity.Peminjaman;
import com.inventorilab.dto.response.BarangResponse;
import com.inventorilab.mapper.BarangMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PeminjamanMapper {

    public static PeminjamanResponse toResponse(Peminjaman peminjaman) {
        if (peminjaman == null) {
            return null;
        }

        List<DetailPeminjamanResponse> detailResponses = Collections.emptyList();
        if (peminjaman.getDetails() != null) {
            detailResponses = peminjaman.getDetails().stream()
                    .map(PeminjamanMapper::toDetailResponse)
                    .collect(Collectors.toList());
        }

        return PeminjamanResponse.builder()
                .idPeminjaman(peminjaman.getId())
                .peminjam(UserMapper.toResponse(peminjaman.getPeminjam()))
                .petugas(UserMapper.toResponse(peminjaman.getPetugas()))
                .tglPinjam(peminjaman.getTglPinjam())
                .tglKembali(peminjaman.getTglKembali())
                .status(peminjaman.getStatus())
                .catatan(peminjaman.getCatatan())
                .details(detailResponses)
                .build();
    }

    public static DetailPeminjamanResponse toDetailResponse(DetailPeminjaman detail) {
        if (detail == null) {
            return null;
        }

        BarangResponse barang = null;
        if (detail.getBarang() != null) {
            barang = BarangMapper.toResponse(detail.getBarang());
        }

        return DetailPeminjamanResponse.builder()
                .idDetail(detail.getId())
                .barang(barang)
                .jumlah(detail.getJumlah())
                .kondisiKembali(detail.getKondisiKembali())
                .build();
    }
}
