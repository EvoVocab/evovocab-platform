package com.evovocab.test;

public record Task(Long id, String title, String description, Boolean isCompleted) {
    public static Task fromRequest(CreateTaskRequest request) {
        return new Task(
            null,
            request.title().trim(),
            request.description() != null ? request.description().trim() : null,
            false
        );
    }

    public static Task fromEntity(TaskEntity entity) {
        return new Task(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getIsCompleted());
    }
}
