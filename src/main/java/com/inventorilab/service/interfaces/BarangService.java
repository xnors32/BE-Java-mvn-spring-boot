package com.inventorilab.service.interfaces;

import com.inventorilab.dto.request.BarangRequest;
import com.inventorilab.dto.response.BarangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BarangService {
    BarangResponse create(BarangRequest request);
    BarangResponse update(Long id, BarangRequest request);
    BarangResponse getById(Long id);
    Page<BarangResponse> getAll(Pageable pageable);
    void delete(Long id);
}
