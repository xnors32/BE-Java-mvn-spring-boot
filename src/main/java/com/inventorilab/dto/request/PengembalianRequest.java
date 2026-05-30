package com.inventorilab.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PengembalianRequest {

    @NotEmpty(message = "Detail pengembalian barang tidak boleh kosong!")
    @Valid
    private List<PengembalianDetailRequest> details;
}
