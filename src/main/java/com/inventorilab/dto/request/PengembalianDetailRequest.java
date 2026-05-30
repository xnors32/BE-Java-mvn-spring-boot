package com.inventorilab.dto.request;

import com.inventorilab.enums.KondisiBarang;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PengembalianDetailRequest {

    @NotNull(message = "ID Detail Peminjaman tidak boleh kosong!")
    private Long idDetail;

    @NotNull(message = "Kondisi kembali barang tidak boleh kosong!")
    private KondisiBarang kondisiKembali;
}
