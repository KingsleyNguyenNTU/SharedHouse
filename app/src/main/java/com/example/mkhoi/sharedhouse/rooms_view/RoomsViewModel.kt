package com.example.mkhoi.sharedhouse.rooms_view

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons


class RoomsViewModel(repository: RoomsRepository): ViewModel() {
    val rooms: LiveData<List<UnitWithPersons>> = repository.getActiveRooms()

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RoomsViewModel(MyApp.component.roomsRepository()) as T
        }
    }
}