package com.example.todoir.peresentaion.di

import com.example.todoir.data.db.Dao
import com.example.todoir.data.repository.datasource.LocalDataSource
import com.example.todoir.data.repository.datasourceimpl.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(dao: Dao): LocalDataSource {
        return LocalDataSourceImpl(dao)
    }
}