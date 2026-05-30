package com.inventorilab.dto.response;

import com.inventorilab.enums.KondisiBarang;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailPeminjamanResponse {
    private Long idDetail;
    private BarangResponse barang;
    private Integer jumlah;
    private KondisiBarang kondisiKembali;
}
