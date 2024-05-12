package com.example.todoir.domain.usecase

import com.example.todoir.data.model.Category
import com.example.todoir.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class GetCategoryUseCase(private val repository: TodoRepository) {
     fun execute(): Flow<List<Category>> {
        return repository.getCategory()
    }
}