package com.example.mkhoi.sharedhouse.room_edit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.DatabaseAsyncTask
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.database.entity.Unit

class EditRoomViewModel(val repository: EditRoomRepository,
                        val existingRoom: UnitWithPersons?): ViewModel() {
    val room: MutableLiveData<Unit> = MutableLiveData()
        get() {
            if (field.value == null ) {
                field.value = existingRoom?.unit?:Unit(name = "", active = false)
            }
            return field
        }
    val roommates: MutableLiveData<List<Person>> = MutableLiveData()
        get() {
            if (field.value == null) {
                field.value = existingRoom?.roommates?:emptyList()
            }
            return field
        }
    val isSaving: MutableLiveData<Boolean> = MutableLiveData()

    fun save() {
        isSaving.value = true
        room.value?.let {
            val task = DatabaseAsyncTask()
            task.execute({
                repository.saveRoom(it, roommates.value?: emptyList())
                isSaving.postValue(false)
            })
        }
    }

    fun addRoomate(roommate: Person) {
        roommates.value = roommates.value?.plus(roommate)
    }

    class Factory(val existingRoom: UnitWithPersons?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditRoomViewModel(MyApp.component.editRoomRepository(), existingRoom) as T
        }
    }
}