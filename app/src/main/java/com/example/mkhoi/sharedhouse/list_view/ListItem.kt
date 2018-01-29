package com.example.mkhoi.sharedhouse.list_view

import com.example.mkhoi.sharedhouse.database.entity.Person

data class ListItem<T>(val originalObject: T,
                       var mainName: String,
                       var caption: String,
                       var deleteAction: () -> Unit = {},
                       var onClickAction: () -> Unit = {}){
    companion object {
        fun fromPerson(person: Person): ListItem<Person> {
            return ListItem(
                    originalObject = person,
                    mainName = person.name,
                    caption = person.phone)
        }
    }
}