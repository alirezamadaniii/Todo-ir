package com.example.todoir.domain.usecase

import com.example.todoir.data.model.Task
import com.example.todoir.domain.repository.TodoRepository

class AddTaskUseCase(private val todoRepository: TodoRepository) {
    suspend fun execute(task: Task) = todoRepository.addTask(task)
}