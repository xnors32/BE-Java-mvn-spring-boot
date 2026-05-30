package com.inventorilab.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaporanStokResponse {
    private long totalKategori;
    private long totalBarangUnit;
    private long totalStokSistem;
    private long totalStokTersedia;
    private long totalStokDipinjam;
    private long barangKondisiBaik;
    private long barangKondisiRusakRingan;
    private long barangKondisiRusakBerat;
}
