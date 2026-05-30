package com.inventorilab.service.interfaces;

import com.inventorilab.dto.request.PeminjamanRequest;
import com.inventorilab.dto.request.PengembalianRequest;
import com.inventorilab.dto.response.PeminjamanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PeminjamanService {
    PeminjamanResponse create(PeminjamanRequest request, String currentUserEmail);
    Page<PeminjamanResponse> getAll(Pageable pageable, String currentUserEmail);
    PeminjamanResponse getById(Long id, String currentUserEmail);
    PeminjamanResponse approve(Long id, String petugasEmail);
    PeminjamanResponse reject(Long id, String petugasEmail);
    PeminjamanResponse kembalikan(Long id, PengembalianRequest request, String petugasEmail);
}
