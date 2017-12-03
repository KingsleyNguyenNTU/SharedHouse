package com.example.mkhoi.sharedhouse.dagger

import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.room_edit.EditRoomRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface MyAppComponent {
    fun inject(app: MyApp)

    fun editRoomRepository(): EditRoomRepository
}