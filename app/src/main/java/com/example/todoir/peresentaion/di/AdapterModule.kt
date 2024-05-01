package com.example.todoir.peresentaion.di

import com.example.todoir.peresentaion.adapter.TaskAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AdapterModule {
    @Singleton
    @Provides
    fun provideTaskAdapter():TaskAdapter{
        return TaskAdapter()
    }
}