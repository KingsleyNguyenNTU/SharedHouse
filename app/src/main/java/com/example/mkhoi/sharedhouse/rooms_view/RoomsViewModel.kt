package com.example.mkhoi.sharedhouse.rooms_view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.DatabaseAsyncTask
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons


class RoomsViewModel(private val repository: RoomsRepository): ViewModel() {
    val rooms: LiveData<List<UnitWithPersons>> = repository.getActiveRooms()

    fun deleteRoom(room: UnitWithPersons) {
        val task = DatabaseAsyncTask()
        task.execute({
            repository.deleteRoom(room)
        })
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RoomsViewModel(MyApp.component.roomsRepository()) as T
        }
    }
}