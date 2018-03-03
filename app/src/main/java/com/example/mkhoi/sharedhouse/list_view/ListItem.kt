package com.example.mkhoi.sharedhouse.list_view

data class ListItem(var mainName: String,
                    var caption: String,
                    var deleteAction: (() -> Unit)? = null,
                    var onClickAction: () -> Unit = {})