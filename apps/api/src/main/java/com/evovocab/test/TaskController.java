package com.evovocab.test;

import com.evovocab.shared.dto.responses.ApiResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping("/v1/tasks")
    public ApiResponseDto<List<Task>> getAll() {
        List<Task> task = service.findAll();
        return ApiResponseDto.success(task);
    }

    @PostMapping("/v1/tasks")
    public ApiResponseDto<Task> create(@Valid @RequestBody CreateTaskRequest request) {
        Task task = service.create(request);
        return ApiResponseDto.created(task, "Tạo task thành công");
    }

    @PutMapping("/v1/tasks/{id}")
    public ApiResponseDto<Task> update(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        Task task = service.update(id, request);
        return ApiResponseDto.success(task, "Cập nhật task thành công");
    }

    @DeleteMapping("/v1/tasks/{id}")
    public ApiResponseDto<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ApiResponseDto.success(null, "Xóa task thành công");
    }
}
