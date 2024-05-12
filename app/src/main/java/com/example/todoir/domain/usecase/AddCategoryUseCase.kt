package com.example.todoir.domain.usecase

import com.example.todoir.data.model.Category
import com.example.todoir.domain.repository.TodoRepository

class AddCategoryUseCase(private val todoRepository: TodoRepository) {
    suspend fun execute(category: Category) = todoRepository.addCategory(category)
}