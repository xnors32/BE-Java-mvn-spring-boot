package com.inventorilab.mapper;

import com.inventorilab.dto.response.KategoriResponse;
import com.inventorilab.entity.Kategori;

public class KategoriMapper {
    
    public static KategoriResponse toResponse(Kategori kategori) {
        if (kategori == null) {
            return null;
        }
        return KategoriResponse.builder()
                .idKategori(kategori.getId())
                .namaKategori(kategori.getNamaKategori())
                .deskripsi(kategori.getDeskripsi())
                .build();
    }
}
