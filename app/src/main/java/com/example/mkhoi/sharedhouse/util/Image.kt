package com.example.mkhoi.sharedhouse.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import java.io.ByteArrayOutputStream
import java.util.*

fun Bitmap.toUri(context: Context): Uri {
    val fileName = UUID.randomUUID().toString()
    val bytes = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, this, fileName, null)

    return Uri.parse(path)
}

fun View.toImage(): Bitmap {
    isDrawingCacheEnabled = true
    measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    layout(0, 0, measuredWidth, measuredHeight);
    buildDrawingCache(true)

    val result = Bitmap.createBitmap(drawingCache)
    isDrawingCacheEnabled = false

    return result
}