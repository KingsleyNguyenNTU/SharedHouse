package com.example.mkhoi.sharedhouse.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.example.mkhoi.sharedhouse.database.bean.UnitWithPersons
import com.example.mkhoi.sharedhouse.database.entity.Person

@Dao
interface PersonDao {
    @Insert
    fun insertPersons(persons: List<Person>): List<Long>

    @Update
    fun updatePersons(persons: List<Person>)

    @Query("select * from person")
    fun getAllPersons(): List<Person>
}