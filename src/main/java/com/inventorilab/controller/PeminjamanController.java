package com.inventorilab.controller;

import com.inventorilab.dto.request.PeminjamanRequest;
import com.inventorilab.dto.request.PengembalianRequest;
import com.inventorilab.dto.response.PeminjamanResponse;
import com.inventorilab.response.PagingResponse;
import com.inventorilab.response.WebResponse;
import com.inventorilab.service.interfaces.PeminjamanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/peminjaman")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PeminjamanController {

    private final PeminjamanService peminjamanService;

    @PostMapping
    public ResponseEntity<WebResponse<PeminjamanResponse>> create(
            @Valid @RequestBody PeminjamanRequest request,
            Principal principal
    ) {
        PeminjamanResponse responseData = peminjamanService.create(request, principal.getName());
        WebResponse<PeminjamanResponse> response = WebResponse.<PeminjamanResponse>builder()
                .code(HttpStatus.CREATED.value())
                .status("CREATED")
                .message("Pengajuan peminjaman berhasil dibuat, menunggu persetujuan petugas!")
                .data(responseData)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<WebResponse<List<PeminjamanResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir,
            Principal principal
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PeminjamanResponse> pageData = peminjamanService.getAll(pageable, principal.getName());

        PagingResponse paging = PagingResponse.builder()
                .currentPage(pageData.getNumber())
                .totalPages(pageData.getTotalPages())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .build();

        WebResponse<List<PeminjamanResponse>> response = WebResponse.<List<PeminjamanResponse>>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Daftar transaksi peminjaman berhasil dimuat!")
                .data(pageData.getContent())
                .paging(paging)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<PeminjamanResponse>> getById(
            @PathVariable Long id,
            Principal principal
    ) {
        PeminjamanResponse responseData = peminjamanService.getById(id, principal.getName());
        WebResponse<PeminjamanResponse> response = WebResponse.<PeminjamanResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Data transaksi peminjaman berhasil dimuat!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<WebResponse<PeminjamanResponse>> approve(
            @PathVariable Long id,
            Principal principal
    ) {
        PeminjamanResponse responseData = peminjamanService.approve(id, principal.getName());
        WebResponse<PeminjamanResponse> response = WebResponse.<PeminjamanResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Transaksi peminjaman disetujui, stok barang otomatis dikurangi!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<WebResponse<PeminjamanResponse>> reject(
            @PathVariable Long id,
            Principal principal
    ) {
        PeminjamanResponse responseData = peminjamanService.reject(id, principal.getName());
        WebResponse<PeminjamanResponse> response = WebResponse.<PeminjamanResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Transaksi peminjaman berhasil ditolak!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/kembalikan")
    public ResponseEntity<WebResponse<PeminjamanResponse>> kembalikan(
            @PathVariable Long id,
            @Valid @RequestBody(required = false) PengembalianRequest request,
            Principal principal
    ) {
        PeminjamanResponse responseData = peminjamanService.kembalikan(id, request, principal.getName());
        WebResponse<PeminjamanResponse> response = WebResponse.<PeminjamanResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Barang peminjaman berhasil dikembalikan, stok tersedia bertambah kembali!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }
}
