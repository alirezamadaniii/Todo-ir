package com.example.todoir.peresentaion.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferenceModule {
    @Provides
    fun provideSharedPreference(context:Application):SharedPreferences{
        return context.getSharedPreferences("system",Context.MODE_PRIVATE)
    }
}