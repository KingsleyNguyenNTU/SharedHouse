package com.example.mkhoi.sharedhouse.list_view

import android.net.Uri

data class ListItem(var mainName: String,
                    var caption: String,
                    var profilePicture: Uri? = null,
                    var deleteAction: (() -> Unit)? = null,
                    var onClickAction: () -> Unit = {})