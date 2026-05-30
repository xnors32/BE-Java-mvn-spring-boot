package com.inventorilab.dto.response;

import com.inventorilab.enums.KondisiBarang;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarangResponse {
    private Long idBarang;
    private Long idKategori;
    private String namaKategori;
    private String namaBarang;
    private String kodeBarang;
    private Integer jumlahTotal;
    private Integer jumlahTersedia;
    private KondisiBarang kondisi;
    private String lokasi;
    private BigDecimal harga;
}
