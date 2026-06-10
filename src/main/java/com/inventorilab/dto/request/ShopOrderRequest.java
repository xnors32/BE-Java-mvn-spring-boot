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
public class ShopOrderRequest {

    @NotNull(message = "ID produk tidak boleh kosong!")
    private Long productId;

    @NotNull(message = "Jumlah tidak boleh kosong!")
    @Min(value = 1, message = "Jumlah minimal 1!")
    private Integer quantity;
}
