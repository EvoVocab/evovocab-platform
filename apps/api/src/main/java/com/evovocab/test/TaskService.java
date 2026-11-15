package com.evovocab.test;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    @Transactional
    public Task create(CreateTaskRequest request) {
        Task task = Task.fromRequest(request);

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(task.title());
        taskEntity.setDescription(task.description());
        taskEntity.setIsCompleted(task.isCompleted());

        return Task.fromEntity(repository.save(taskEntity));
    }

    @Transactional
    public Task update(Long id, UpdateTaskRequest request) {
        TaskEntity existingTask = repository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy task với id: " + id));

        existingTask.setTitle(request.title().trim());
        existingTask.setDescription(request.description() != null ? request.description().trim() : null);
        existingTask.setIsCompleted(request.isCompleted());

        return Task.fromEntity(repository.save(existingTask));
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return repository.findAll().stream().map(Task::fromEntity).toList();
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Không tìm thấy task với id: " + id);
        }

        repository.deleteById(id);
    }
}
