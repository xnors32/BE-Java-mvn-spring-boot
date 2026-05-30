package com.inventorilab.controller;

import com.inventorilab.dto.request.KategoriRequest;
import com.inventorilab.dto.response.KategoriResponse;
import com.inventorilab.response.PagingResponse;
import com.inventorilab.response.WebResponse;
import com.inventorilab.service.interfaces.KategoriService;
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
@RequestMapping("/api/kategori")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class KategoriController {

    private final KategoriService kategoriService;

    @PostMapping
    public ResponseEntity<WebResponse<KategoriResponse>> create(@Valid @RequestBody KategoriRequest request) {
        KategoriResponse responseData = kategoriService.create(request);
        WebResponse<KategoriResponse> response = WebResponse.<KategoriResponse>builder()
                .code(HttpStatus.CREATED.value())
                .status("CREATED")
                .message("Kategori berhasil dibuat!")
                .data(responseData)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<WebResponse<KategoriResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody KategoriRequest request
    ) {
        KategoriResponse responseData = kategoriService.update(id, request);
        WebResponse<KategoriResponse> response = WebResponse.<KategoriResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Kategori berhasil diperbarui!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<KategoriResponse>> getById(@PathVariable Long id) {
        KategoriResponse responseData = kategoriService.getById(id);
        WebResponse<KategoriResponse> response = WebResponse.<KategoriResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Kategori ditemukan!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<WebResponse<List<KategoriResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<KategoriResponse> pageData = kategoriService.getAll(pageable);

        PagingResponse paging = PagingResponse.builder()
                .currentPage(pageData.getNumber())
                .totalPages(pageData.getTotalPages())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .build();

        WebResponse<List<KategoriResponse>> response = WebResponse.<List<KategoriResponse>>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Berhasil mendapatkan daftar kategori!")
                .data(pageData.getContent())
                .paging(paging)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable Long id) {
        kategoriService.delete(id);
        WebResponse<String> response = WebResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Kategori dengan ID " + id + " berhasil dihapus!")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
