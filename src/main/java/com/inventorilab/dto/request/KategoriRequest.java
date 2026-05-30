package com.inventorilab.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KategoriRequest {

    @NotBlank(message = "Nama kategori tidak boleh kosong!")
    @Size(max = 100, message = "Nama kategori maksimal 100 karakter!")
    private String namaKategori;

    private String deskripsi;
}
