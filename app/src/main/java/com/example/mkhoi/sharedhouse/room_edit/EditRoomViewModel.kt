package com.example.mkhoi.sharedhouse.room_edit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.mkhoi.sharedhouse.MyApp
import com.example.mkhoi.sharedhouse.database.BackgroundAsyncTask
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.example.mkhoi.sharedhouse.database.entity.Unit

class EditRoomViewModel(private val repository: EditRoomRepository,
                        private val existingRoom: UnitWithPersons?): ViewModel() {
    val room: MutableLiveData<Unit> = MutableLiveData()
        get() {
            if (field.value == null ) {
                field.value = existingRoom?.unit?:Unit(name = "", active = true)
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
    val deletedRoommates: MutableLiveData<MutableList<Person>> = MutableLiveData()
    val isSaving: MutableLiveData<Boolean> = MutableLiveData()

    init {
        deletedRoommates.value = mutableListOf()
    }

    fun save() {
        isSaving.value = true
        room.value?.let {
            val task = BackgroundAsyncTask()
            task.execute({
                repository.saveRoom(
                        it,
                        roommates.value?: emptyList(),
                        deletedRoommates.value?: emptyList())
                isSaving.postValue(false)
            })
        }
    }

    fun addRoommate(roommate: Person) {
        roommates.value = roommates.value?.plus(roommate)
    }

    fun deleteRoommate(roommate: Person){
        roommates.value = roommates.value?.minus(roommate)
        deletedRoommates.value?.add(roommate)
    }

    class Factory(val existingRoom: UnitWithPersons?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditRoomViewModel(MyApp.component.editRoomRepository(), existingRoom) as T
        }
    }
}