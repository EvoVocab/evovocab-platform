package com.evovocab.test;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 50, message = "Tiêu đề tối đa 50 ký tự")
    String title,

    @Size(max = 200, message = "Mô tả tối đa 200 ký tự") String description
) {}
