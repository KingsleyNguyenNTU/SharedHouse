package com.example.mkhoi.sharedhouse.dagger

import android.arch.persistence.room.Room
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.AppDatabase
import com.example.mkhoi.sharedhouse.database.dao.UnitDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class AppModule(val app: MyApp) {
    @Provides
    @Singleton
    fun provideApp() = app

    @Provides
    @Singleton
    fun provideUnitDao(appDatabase: AppDatabase) =  appDatabase.unitDao()

    @Provides
    @Singleton
    fun providePersonDao(appDatabase: AppDatabase) =  appDatabase.personDao()

    @Provides
    @Singleton
    fun provideDatabase() =  Room.databaseBuilder(app, AppDatabase::class.java!!, AppDatabase.DB_NAME).build()
}