package com.inventorilab.controller;

import com.inventorilab.dto.response.DetailPeminjamanResponse;
import com.inventorilab.enums.KondisiBarang;
import com.inventorilab.response.WebResponse;
import com.inventorilab.service.interfaces.DetailPeminjamanService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/detail-peminjaman")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DetailPeminjamanController {

    private final DetailPeminjamanService detailPeminjamanService;

    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<DetailPeminjamanResponse>> getById(@PathVariable Long id) {
        DetailPeminjamanResponse responseData = detailPeminjamanService.getById(id);
        WebResponse<DetailPeminjamanResponse> response = WebResponse.<DetailPeminjamanResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Detail peminjaman ditemukan!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WebResponse<DetailPeminjamanResponse>> updateKondisi(
            @PathVariable Long id,
            @RequestBody UpdateKondisiRequest request
    ) {
        DetailPeminjamanResponse responseData = detailPeminjamanService.updateKondisi(id, request.getKondisiKembali());
        WebResponse<DetailPeminjamanResponse> response = WebResponse.<DetailPeminjamanResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Kondisi barang kembali berhasil diperbarui!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateKondisiRequest {
        private KondisiBarang kondisiKembali;
    }
}
