package com.example.mkhoi.sharedhouse.list_view

import android.net.Uri
import android.support.annotation.DrawableRes

data class ListItem(var mainName: String,
                    var caption: String,
                    var profilePicture: Uri? = null,
                    @DrawableRes var profilePictureId: Int? = null,
                    var deleteAction: (() -> Unit)? = null,
                    var onClickAction: () -> Unit = {})