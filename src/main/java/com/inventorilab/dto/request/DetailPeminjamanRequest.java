package com.inventorilab.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailPeminjamanRequest {

    @NotNull(message = "ID Barang tidak boleh kosong!")
    private Long idBarang;

    @NotNull(message = "Jumlah barang tidak boleh kosong!")
    @Min(value = 1, message = "Jumlah barang minimal dipinjam adalah 1!")
    private Integer jumlah;
}
