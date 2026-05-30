package com.inventorilab.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaporanPeminjamanResponse {
    private long totalPeminjaman;
    private long statusMenunggu;
    private long statusDisetujui;
    private long statusDitolak;
    private long statusDikembalikan;
}
