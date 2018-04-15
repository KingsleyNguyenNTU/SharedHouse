package com.example.mkhoi.sharedhouse.dagger

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.AppDatabase
import com.example.mkhoi.sharedhouse.database.AppDatabase.Companion.MIGRATION_1_2
import com.example.mkhoi.sharedhouse.database.AppDatabase.Companion.MIGRATION_2_3
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class AppModule(val app: MyApp) {
    @Provides
    @Singleton
    fun provideApp() = app

    @Provides
    @Singleton
    fun provideContext(): Context =  app.applicationContext

    @Provides
    @Singleton
    fun provideUnitDao(appDatabase: AppDatabase) =  appDatabase.unitDao()

    @Provides
    @Singleton
    fun providePersonDao(appDatabase: AppDatabase) =  appDatabase.personDao()

    @Provides
    @Singleton
    fun provideUnitPersonDao(appDatabase: AppDatabase) =  appDatabase.unitPersonDao()

    @Provides
    @Singleton
    fun provideFeeDao(appDatabase: AppDatabase) = appDatabase.feeDao()

    @Provides
    @Singleton
    fun provideSplitterDao(appDatabase: AppDatabase) = appDatabase.splitterDao()

    @Provides
    @Singleton
    fun provideSettingDao(appDatabase: AppDatabase) = appDatabase.settingDao()

    @Provides
    @Singleton
    fun provideDatabase() =  Room.databaseBuilder(app, AppDatabase::class.java, AppDatabase.DB_NAME)
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .build()
}