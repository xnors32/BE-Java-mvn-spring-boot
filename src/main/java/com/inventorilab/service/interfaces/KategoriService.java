package com.inventorilab.service.interfaces;

import com.inventorilab.dto.request.KategoriRequest;
import com.inventorilab.dto.response.KategoriResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KategoriService {
    KategoriResponse create(KategoriRequest request);
    KategoriResponse update(Long id, KategoriRequest request);
    KategoriResponse getById(Long id);
    Page<KategoriResponse> getAll(Pageable pageable);
    void delete(Long id);
}
