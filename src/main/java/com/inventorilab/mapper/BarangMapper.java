package com.inventorilab.mapper;

import com.inventorilab.dto.response.BarangResponse;
import com.inventorilab.entity.Barang;

public class BarangMapper {
    
    public static BarangResponse toResponse(Barang barang) {
        if (barang == null) {
            return null;
        }
        
        Long idKategori = null;
        String namaKategori = null;
        if (barang.getKategori() != null) {
            idKategori = barang.getKategori().getId();
            namaKategori = barang.getKategori().getNamaKategori();
        }
        
        return BarangResponse.builder()
                .idBarang(barang.getId())
                .idKategori(idKategori)
                .namaKategori(namaKategori)
                .namaBarang(barang.getNamaBarang())
                .kodeBarang(barang.getKodeBarang())
                .jumlahTotal(barang.getJumlahTotal())
                .jumlahTersedia(barang.getJumlahTersedia())
                .kondisi(barang.getKondisi())
                .lokasi(barang.getLokasi())
                .harga(barang.getHarga())
                .build();
    }
}
