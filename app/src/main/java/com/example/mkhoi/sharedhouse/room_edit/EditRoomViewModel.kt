package com.example.mkhoi.sharedhouse.room_edit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.DatabaseAsyncTask
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.database.entity.Unit

class EditRoomViewModel(val repository: EditRoomRepository): ViewModel() {
    var room: MutableLiveData<Unit> = MutableLiveData()
    var roommates: MutableLiveData<List<Person>> = MutableLiveData()
        get() {
            if (field.value == null) {
                field.value = emptyList()
            }
            return field
        }
    var isSaving: MutableLiveData<Boolean> = MutableLiveData()

    fun save(newRoom: Unit) {
        isSaving.value = true
        room.value = newRoom
        room.value?.let {
            val task = DatabaseAsyncTask()
            task.execute({
                repository.addRoom(it, roommates.value?: emptyList())
                isSaving.postValue(false)
            })
        }
    }

    fun addRoomate(roommate: Person) {
        roommates.value = roommates.value?.plus(roommate)
    }

    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditRoomViewModel(MyApp.component.editRoomRepository()) as T
        }
    }
}