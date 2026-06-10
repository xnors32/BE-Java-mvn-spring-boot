package com.inventorilab.controller;

import com.inventorilab.dto.request.ShopOrderRequest;
import com.inventorilab.dto.response.ShopOrderResponse;
import com.inventorilab.response.PagingResponse;
import com.inventorilab.response.WebResponse;
import com.inventorilab.service.interfaces.ShopOrderService;
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
@RequestMapping("/api/shop/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ShopOrderController {

    private final ShopOrderService shopOrderService;

    @PostMapping
    public ResponseEntity<WebResponse<ShopOrderResponse>> create(
            @Valid @RequestBody ShopOrderRequest request,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        ShopOrderResponse responseData = shopOrderService.create(request, userEmail);
        WebResponse<ShopOrderResponse> response = WebResponse.<ShopOrderResponse>builder()
                .code(HttpStatus.CREATED.value())
                .status("CREATED")
                .message("Pesanan berhasil dibuat!")
                .data(responseData)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<WebResponse<List<ShopOrderResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ShopOrderResponse> pageData = shopOrderService.getAll(pageable);

        PagingResponse paging = PagingResponse.builder()
                .currentPage(pageData.getNumber())
                .totalPages(pageData.getTotalPages())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .build();

        WebResponse<List<ShopOrderResponse>> response = WebResponse.<List<ShopOrderResponse>>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Berhasil mendapatkan daftar pesanan!")
                .data(pageData.getContent())
                .paging(paging)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mine")
    public ResponseEntity<WebResponse<List<ShopOrderResponse>>> getMyOrders(Authentication authentication) {
        String userEmail = authentication.getName();
        List<ShopOrderResponse> responseData = shopOrderService.getMyOrders(userEmail);
        WebResponse<List<ShopOrderResponse>> response = WebResponse.<List<ShopOrderResponse>>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Berhasil mendapatkan pesanan Anda!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WebResponse<ShopOrderResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        ShopOrderResponse responseData = shopOrderService.updateStatus(id, status);
        WebResponse<ShopOrderResponse> response = WebResponse.<ShopOrderResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Status pesanan berhasil diperbarui!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }
}
