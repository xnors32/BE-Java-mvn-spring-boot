package com.inventorilab.dto.request;

import com.inventorilab.enums.KondisiBarang;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarangRequest {

    @NotNull(message = "ID Kategori tidak boleh kosong!")
    private Long idKategori;

    @NotBlank(message = "Nama barang tidak boleh kosong!")
    private String namaBarang;

    @NotBlank(message = "Kode barang tidak boleh kosong!")
    private String kodeBarang;

    @NotNull(message = "Jumlah total tidak boleh kosong!")
    @Min(value = 1, message = "Jumlah total minimal adalah 1!")
    private Integer jumlahTotal;

    @NotNull(message = "Harga barang tidak boleh kosong!")
    @DecimalMin(value = "0.0", inclusive = true, message = "Harga barang tidak boleh negatif!")
    private BigDecimal harga;

    @NotNull(message = "Kondisi barang tidak boleh kosong!")
    private KondisiBarang kondisi;

    @NotBlank(message = "Lokasi barang tidak boleh kosong!")
    private String lokasi;
}
