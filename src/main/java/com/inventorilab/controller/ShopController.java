package com.inventorilab.controller;

import com.inventorilab.dto.response.BarangResponse;
import com.inventorilab.response.WebResponse;
import com.inventorilab.service.interfaces.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bahan-habis-pakai")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    public ResponseEntity<WebResponse<List<BarangResponse>>> getAll() {
        List<BarangResponse> items = shopService.getBahanHabisPakai();
        WebResponse<List<BarangResponse>> response = WebResponse.<List<BarangResponse>>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Berhasil mendapatkan daftar bahan habis pakai!")
                .data(items)
                .build();
        return ResponseEntity.ok(response);
    }
}
