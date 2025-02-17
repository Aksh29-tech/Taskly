package com.taskly.service;

import com.taskly.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getAllTasks(Long userId);
    Optional<Task> getTaskById(Long id);
    Task createTask(Task task);
    Task updateTask(Long id, Task updatedTask);
    void deleteTask(Long id);
}
