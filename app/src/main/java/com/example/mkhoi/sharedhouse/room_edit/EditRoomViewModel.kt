package com.example.mkhoi.sharedhouse.room_edit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.database.entity.Unit

class EditRoomViewModel(val repository: EditRoomRepository): ViewModel() {
    var room: MutableLiveData<Unit> = MutableLiveData()
    var roommates: MutableLiveData<List<Person>> = MutableLiveData()

    fun save() {
        room.value?.let {
            repository.addRoom(it, roommates.value?: emptyList())
        }
    }

    class Factory() : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditRoomViewModel(MyApp.component.editRoomRepository()) as T
        }
    }
}