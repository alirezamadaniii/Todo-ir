package com.example.todoir.peresentaion.di

import com.example.todoir.domain.repository.TodoRepository
import com.example.todoir.domain.usecase.AddCategoryUseCase
import com.example.todoir.domain.usecase.AddTaskUseCase
import com.example.todoir.domain.usecase.CompletedTaskUseCase
import com.example.todoir.domain.usecase.DeleteTaskUseCase
import com.example.todoir.domain.usecase.GetCategoryTaskUseCase
import com.example.todoir.domain.usecase.GetCategoryUseCase
import com.example.todoir.domain.usecase.GetCompletedTaskUseCase
import com.example.todoir.domain.usecase.GetPriorityTaskUseCase
import com.example.todoir.domain.usecase.GetTaskAfterFilterUseCase
import com.example.todoir.domain.usecase.GetTaskUseCase
import com.example.todoir.domain.usecase.UpdateTaskUseCase


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideAddTaskUseCase(
        todoRepository: TodoRepository
    ):AddTaskUseCase{
        return AddTaskUseCase(todoRepository)
    }

    @Singleton
    @Provides
    fun provideGetTaskUseCase(
        todoRepository: TodoRepository
    ):GetTaskUseCase{
        return GetTaskUseCase(todoRepository)
    }

    @Singleton
    @Provides
    fun provideAddCategoryUseCase(
        todoRepository: TodoRepository
    ):AddCategoryUseCase{
        return AddCategoryUseCase(todoRepository)
    }

    @Singleton
    @Provides
    fun provideGetCategoryUseCase(
        todoRepository: TodoRepository
    ):GetCategoryUseCase{
        return GetCategoryUseCase(todoRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteTaskUseCase(
        todoRepository: TodoRepository
    ):DeleteTaskUseCase{
        return DeleteTaskUseCase(todoRepository)
    }

    @Singleton
    @Provides
    fun provideUpdateTaskUseCase(
        todoRepository: TodoRepository
    ):UpdateTaskUseCase{
        return UpdateTaskUseCase(todoRepository)
    }

    @Singleton
    @Provides
    fun provideCompletedTaskUseCase(
        todoRepository: TodoRepository
    ):CompletedTaskUseCase{
        return CompletedTaskUseCase(todoRepository)
    }

    @Singleton
    @Provides
    fun provideGetTaskAfterFilterUseCase(
        todoRepository: TodoRepository
    ):GetTaskAfterFilterUseCase{
        return GetTaskAfterFilterUseCase(todoRepository)
    }

    @Singleton
    @Provides
    fun provideGetPriorityTaskUseCase(
        todoRepository: TodoRepository
    ):GetPriorityTaskUseCase{
        return GetPriorityTaskUseCase(todoRepository)
    }


    @Singleton
    @Provides
    fun provideGetCategoryTaskUseCase(
        todoRepository: TodoRepository
    ):GetCategoryTaskUseCase{
        return GetCategoryTaskUseCase(todoRepository)
    }


    @Singleton
    @Provides
    fun provideGetCompletedTaskUseCase(
        todoRepository: TodoRepository
    ):GetCompletedTaskUseCase{
        return GetCompletedTaskUseCase(todoRepository)
    }

}