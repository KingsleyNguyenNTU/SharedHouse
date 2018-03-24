package com.example.mkhoi.sharedhouse.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import com.example.mkhoi.sharedhouse.database.entity.Person
import java.io.ByteArrayOutputStream
import java.util.*
import android.provider.ContactsContract
import android.content.ContentUris
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.widget.ImageView
import java.io.ByteArrayInputStream


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

fun Person.getProfilePicture(context: Context): Uri?{
    var photo: Bitmap? = null
    //find contact ID by phone
    val contentResolver = context.contentResolver
    var contactId: Long? = null
    val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(this.phone))
    val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID)
    contentResolver.query(uri, projection, null, null, null)?.apply {
        while (moveToNext()) {
            contactId = getString(getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID)).toLong()
        }
        close()
    }

    //find photo by contact ID
    contactId?.let {
        val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, it)
        val photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
        contentResolver.query(photoUri, arrayOf(ContactsContract.Contacts.Photo.PHOTO),
                null, null, null)?.apply {
            if (moveToFirst()) {
                val data = getBlob(0)
                if (data != null) {
                    photo = BitmapFactory.decodeStream(ByteArrayInputStream(data))
                }
            }
            close()
        }
    }

    return photo?.toUri(context)
}

fun ImageView.displayRoundImage(resources: Resources){
    val imageBitmap = (drawable as BitmapDrawable).bitmap
    val imageDrawable = RoundedBitmapDrawableFactory.create(resources, imageBitmap)
    imageDrawable.isCircular = true
    imageDrawable.cornerRadius = Math.max(imageBitmap.width, imageBitmap.height) / 2.0f
    setImageDrawable(imageDrawable)
}