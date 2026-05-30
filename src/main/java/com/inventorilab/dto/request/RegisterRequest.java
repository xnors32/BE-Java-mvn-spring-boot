package com.inventorilab.dto.request;

import com.inventorilab.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Nama tidak boleh kosong!")
    @Size(min = 2, max = 100, message = "Nama harus berukuran antara 2 hingga 100 karakter!")
    private String nama;

    @NotBlank(message = "Email tidak boleh kosong!")
    @Email(message = "Format email tidak valid!")
    private String email;

    @NotBlank(message = "Password tidak boleh kosong!")
    @Size(min = 6, message = "Password minimal terdiri dari 6 karakter!")
    private String password;

    @NotNull(message = "Role tidak boleh kosong!")
    private Role role;
}
