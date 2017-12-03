package com.example.mkhoi.sharedhouse.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import com.example.mkhoi.sharedhouse.database.entity.Person

@Dao
interface PersonDao {
    @Insert
    fun insertPersons(persons: List<Person>): List<Long>
}