package com.example.mkhoi.sharedhouse.dagger

import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.fee_edit.EditFeeRepository
import com.example.mkhoi.sharedhouse.room_edit.EditRoomRepository
import com.example.mkhoi.sharedhouse.rooms_view.RoomsRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface MyAppComponent {
    fun inject(app: MyApp)

    fun editRoomRepository(): EditRoomRepository
    fun roomsRepository(): RoomsRepository
    fun editFeeRepository(): EditFeeRepository
}