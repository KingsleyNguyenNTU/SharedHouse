package com.example.mkhoi.sharedhouse.list_view

data class ListItem<T>(var mainName: String,
                       var caption: String,
                       var deleteAction: () -> Unit = {},
                       var onClickAction: () -> Unit = {})