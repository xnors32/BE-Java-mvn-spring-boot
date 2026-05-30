package com.inventorilab.dto.response;

import com.inventorilab.enums.StatusPeminjaman;
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
public class PeminjamanResponse {
    private Long idPeminjaman;
    private UserResponse peminjam;
    private UserResponse petugas;
    private LocalDate tglPinjam;
    private LocalDate tglKembali;
    private StatusPeminjaman status;
    private String catatan;
    private List<DetailPeminjamanResponse> details;
}
