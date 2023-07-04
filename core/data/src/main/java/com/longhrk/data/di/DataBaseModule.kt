package com.longhrk.data.di

import android.content.Context
import androidx.room.Room
import com.longhrk.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @DatabaseName
    @Provides
    @Singleton
    fun provideDatabaseName() = "mf_chat.db"

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context, @DatabaseName dbName: String
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}