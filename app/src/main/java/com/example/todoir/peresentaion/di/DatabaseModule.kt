package com.example.todoir.peresentaion.di

import android.app.Application
import androidx.room.Room
import com.example.todoir.data.db.Dao
import com.example.todoir.data.db.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(app: Application):Database{
        return Room.databaseBuilder(app,
            Database::class.java,"todo_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideClinicDao(database: Database): Dao {
        return database.dao()
    }
}