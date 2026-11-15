package com.evovocab.test;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 50, message = "Tiêu đề tối đa 50 ký tự")
    String title,

    @Size(max = 200, message = "Mô tả tối đa 200 ký tự") String description,

    @NotNull(message = "Trạng thái hoàn thành không được để trống") Boolean isCompleted
) {}
