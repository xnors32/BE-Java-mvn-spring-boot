package com.inventorilab.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeminjamanRequest {

    @NotNull(message = "Tanggal pinjam tidak boleh kosong!")
    @FutureOrPresent(message = "Tanggal pinjam harus hari ini atau di masa mendatang!")
    private LocalDate tglPinjam;

    @NotNull(message = "Tanggal kembali tidak boleh kosong!")
    private LocalDate tglKembali;

    private String catatan;

    @NotEmpty(message = "Detail peminjaman barang tidak boleh kosong!")
    @Valid
    private List<DetailPeminjamanRequest> details;
}
