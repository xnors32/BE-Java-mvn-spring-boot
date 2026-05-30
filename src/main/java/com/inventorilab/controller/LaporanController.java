package com.inventorilab.controller;

import com.inventorilab.dto.response.LaporanPeminjamanResponse;
import com.inventorilab.dto.response.LaporanStokResponse;
import com.inventorilab.response.WebResponse;
import com.inventorilab.service.interfaces.LaporanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/laporan")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class LaporanController {

    private final LaporanService laporanService;

    @GetMapping("/stok")
    public ResponseEntity<WebResponse<LaporanStokResponse>> getStokLaporan() {
        LaporanStokResponse responseData = laporanService.getStokLaporan();
        WebResponse<LaporanStokResponse> response = WebResponse.<LaporanStokResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Laporan stok inventaris berhasil dimuat!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/peminjaman")
    public ResponseEntity<WebResponse<LaporanPeminjamanResponse>> getPeminjamanLaporan() {
        LaporanPeminjamanResponse responseData = laporanService.getPeminjamanLaporan();
        WebResponse<LaporanPeminjamanResponse> response = WebResponse.<LaporanPeminjamanResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Laporan transaksi peminjaman berhasil dimuat!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }
}
