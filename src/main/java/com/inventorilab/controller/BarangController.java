package com.inventorilab.controller;

import com.inventorilab.dto.request.BarangRequest;
import com.inventorilab.dto.response.BarangResponse;
import com.inventorilab.response.PagingResponse;
import com.inventorilab.response.WebResponse;
import com.inventorilab.service.interfaces.BarangService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barang")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BarangController {

    private final BarangService barangService;

    @PostMapping
    public ResponseEntity<WebResponse<BarangResponse>> create(@Valid @RequestBody BarangRequest request) {
        BarangResponse responseData = barangService.create(request);
        WebResponse<BarangResponse> response = WebResponse.<BarangResponse>builder()
                .code(HttpStatus.CREATED.value())
                .status("CREATED")
                .message("Barang inventaris berhasil didaftarkan!")
                .data(responseData)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<WebResponse<BarangResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody BarangRequest request
    ) {
        BarangResponse responseData = barangService.update(id, request);
        WebResponse<BarangResponse> response = WebResponse.<BarangResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Barang inventaris berhasil diperbarui!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<BarangResponse>> getById(@PathVariable Long id) {
        BarangResponse responseData = barangService.getById(id);
        WebResponse<BarangResponse> response = WebResponse.<BarangResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Barang ditemukan!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<WebResponse<List<BarangResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BarangResponse> pageData = barangService.getAll(pageable);

        PagingResponse paging = PagingResponse.builder()
                .currentPage(pageData.getNumber())
                .totalPages(pageData.getTotalPages())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .build();

        WebResponse<List<BarangResponse>> response = WebResponse.<List<BarangResponse>>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Berhasil mendapatkan daftar barang inventaris!")
                .data(pageData.getContent())
                .paging(paging)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable Long id) {
        barangService.delete(id);
        WebResponse<String> response = WebResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Barang inventaris dengan ID " + id + " berhasil dihapus!")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
