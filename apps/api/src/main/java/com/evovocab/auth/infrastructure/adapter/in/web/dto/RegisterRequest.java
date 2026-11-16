package com.evovocab.auth.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Họ và tên không được để trống")
    @Size(min = 2, max = 100, message = "Họ và tên không hợp lệ")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Họ và tên không hợp lệ")
    String fullName,

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 255, message = "Email không hợp lệ")
    @Pattern(regexp = "\\S+", message = "Email không hợp lệ")
    String email,

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, max = 72, message = "Mật khẩu không hợp lệ")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[\\p{P}\\p{S}])(?=\\p{ASCII}+$)(?!\\s|.*\\s$).*$",
        message = "Mật khẩu không hợp lệ"
    )
    String password
) {}
