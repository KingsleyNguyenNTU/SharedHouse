package com.example.mkhoi.sharedhouse.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.example.mkhoi.sharedhouse.database.entity.Person

@Dao
interface PersonDao {
    @Insert
    fun insertPersons(persons: List<Person>): List<Long>

    @Update
    fun updatePersons(persons: List<Person>)

    @Query("select * from person where active = :active")
    fun getAllActivePersons(active: Boolean = true): LiveData<List<Person>>
}