package com.inventorilab.service.interfaces;

import com.inventorilab.dto.response.DetailPeminjamanResponse;
import com.inventorilab.enums.KondisiBarang;

public interface DetailPeminjamanService {
    DetailPeminjamanResponse getById(Long id);
    DetailPeminjamanResponse updateKondisi(Long id, KondisiBarang kondisiKembali);
}
