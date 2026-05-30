package com.inventorilab.service.interfaces;

import com.inventorilab.dto.response.LaporanPeminjamanResponse;
import com.inventorilab.dto.response.LaporanStokResponse;

public interface LaporanService {
    LaporanStokResponse getStokLaporan();
    LaporanPeminjamanResponse getPeminjamanLaporan();
}
