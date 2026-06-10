package com.inventorilab.controller;

import com.inventorilab.dto.request.ShopProductRequest;
import com.inventorilab.dto.response.ShopProductResponse;
import com.inventorilab.response.PagingResponse;
import com.inventorilab.response.WebResponse;
import com.inventorilab.service.interfaces.ShopProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ShopProductController {

    private final ShopProductService shopProductService;

    @PostMapping
    public ResponseEntity<WebResponse<ShopProductResponse>> create(
            @Valid @RequestBody ShopProductRequest request,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        ShopProductResponse responseData = shopProductService.create(request, userEmail);
        WebResponse<ShopProductResponse> response = WebResponse.<ShopProductResponse>builder()
                .code(HttpStatus.CREATED.value())
                .status("CREATED")
                .message("Produk shop berhasil ditambahkan!")
                .data(responseData)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<WebResponse<List<ShopProductResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ShopProductResponse> pageData = shopProductService.getAll(pageable);

        PagingResponse paging = PagingResponse.builder()
                .currentPage(pageData.getNumber())
                .totalPages(pageData.getTotalPages())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .build();

        WebResponse<List<ShopProductResponse>> response = WebResponse.<List<ShopProductResponse>>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Berhasil mendapatkan daftar produk shop!")
                .data(pageData.getContent())
                .paging(paging)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<ShopProductResponse>> getById(@PathVariable Long id) {
        ShopProductResponse responseData = shopProductService.getById(id);
        WebResponse<ShopProductResponse> response = WebResponse.<ShopProductResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Produk shop ditemukan!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<WebResponse<ShopProductResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ShopProductRequest request
    ) {
        ShopProductResponse responseData = shopProductService.update(id, request);
        WebResponse<ShopProductResponse> response = WebResponse.<ShopProductResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Produk shop berhasil diperbarui!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable Long id) {
        shopProductService.delete(id);
        WebResponse<String> response = WebResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Produk shop dengan ID " + id + " berhasil dihapus!")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
