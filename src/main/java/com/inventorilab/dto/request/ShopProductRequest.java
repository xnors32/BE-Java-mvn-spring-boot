package com.inventorilab.dto.request;

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
public class ShopProductRequest {

    @NotBlank(message = "Nama produk tidak boleh kosong!")
    private String namaProduk;

    private String deskripsi;

    @NotNull(message = "Harga tidak boleh kosong!")
    @DecimalMin(value = "0.0", inclusive = false, message = "Harga harus lebih dari 0!")
    private BigDecimal harga;

    @NotNull(message = "Stok tidak boleh kosong!")
    @Min(value = 0, message = "Stok tidak boleh negatif!")
    private Integer stok;

    private String gambarUrl;

    private String kategori;

    private String tags;
}
