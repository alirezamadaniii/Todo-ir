package com.example.todoir.peresentaion.di


import com.example.todoir.data.repository.TodoRepositoryImpl
import com.example.todoir.data.repository.datasource.LocalDataSource
import com.example.todoir.domain.repository.TodoRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideClinicRepository(
        clinicLocalDataSource: LocalDataSource
    ):TodoRepository{
        return TodoRepositoryImpl(
            clinicLocalDataSource)
    }
}