package com.example.mkhoi.sharedhouse.util

import android.Manifest
import android.arch.lifecycle.MutableLiveData
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.view.View
import com.example.mkhoi.sharedhouse.database.BackgroundAsyncTask
import com.example.mkhoi.sharedhouse.database.entity.Person
import com.squareup.picasso.Transformation
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


private const val IMAGE_SIZE = 1024
private const val BORDER_SIZE = 50

fun Bitmap.toUri(context: Context, isPrivate: Boolean): Uri {
    val fileName = UUID.randomUUID().toString()

    return if (isPrivate){
        val fileOutputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        this.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.close()

        Uri.fromFile(context.getFileStreamPath(fileName))
    } else{
        val bytes = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, this, fileName, null)

        Uri.parse(path)
    }
}

fun View.toImage(): Bitmap {
    isDrawingCacheEnabled = true
    measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
    layout(0, 0, measuredWidth, measuredHeight)
    buildDrawingCache(true)

    val result = Bitmap.createBitmap(drawingCache)
    isDrawingCacheEnabled = false

    return result
}

fun Person.getProfilePictureLiveData(context: Context, uriLiveData: MutableLiveData<Uri?>){
    BackgroundAsyncTask().execute({
        uriLiveData.postValue(this.getProfilePicture(context))
    })
}

fun Person.getProfilePicture(context: Context): Uri?{
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED) {

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
            val photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO)
            try {
                val fd = contentResolver.openAssetFileDescriptor(photoUri, "r")
                photo = BitmapFactory.decodeStream(fd.createInputStream())
            } catch (e: IOException) {
                photo = null
            }
        }

        return photo?.toUri(context, true)
    }
    else return null
}

fun Uri.toBitmap(context: Context): Bitmap{
    val inputStream = context.contentResolver.openInputStream(this)
    return BitmapFactory.decodeStream(inputStream)
}

/**
 * Maximum combine 4 pictures
 */
fun List<Uri?>.combineProfilePictures(context: Context) : Uri?{
    val width = (IMAGE_SIZE - BORDER_SIZE)/2
    val listImages = this.filterNotNull()
    return with(listImages) {
        when (size){
            0 -> null
            1 -> this[0]
            2 -> {
                //merge 2 pictures: 1 on the left, 1 on the right
                val firstImage = Bitmap.createScaledBitmap(this[0].toBitmap(context), IMAGE_SIZE, IMAGE_SIZE, false)
                val secondImage = Bitmap.createScaledBitmap(this[1].toBitmap(context), IMAGE_SIZE, IMAGE_SIZE, false)

                //crop half of each images
                val croppedFirstImage = Bitmap.createBitmap(firstImage, 0, 0, width, IMAGE_SIZE)
                val croppedSecondImage = Bitmap.createBitmap(secondImage, IMAGE_SIZE - width, 0, width, IMAGE_SIZE)

                //combined together
                val combinedBitmap  = Bitmap.createBitmap(IMAGE_SIZE, IMAGE_SIZE, Bitmap.Config.ARGB_8888)
                val combinedImage = Canvas(combinedBitmap)
                combinedImage.drawColor(Color.WHITE)
                combinedImage.drawBitmap(croppedFirstImage, 0f, 0f, null)
                combinedImage.drawBitmap(croppedSecondImage, (IMAGE_SIZE - width).toFloat(), 0f, null)

                combinedBitmap.toUri(context, true)
            }
            3 -> {
                //1 on the left, 1 on top right, 1 on bottom right
                val firstImage = Bitmap.createScaledBitmap(this[0].toBitmap(context), IMAGE_SIZE, IMAGE_SIZE, false)
                val secondImage = Bitmap.createScaledBitmap(this[1].toBitmap(context), width, width, false)
                val thirdImage = Bitmap.createScaledBitmap(this[2].toBitmap(context), width, width, false)

                //crop half of first
                val croppedFirstImage = Bitmap.createBitmap(firstImage, 0, 0, width, IMAGE_SIZE)

                //combined together
                val combinedBitmap  = Bitmap.createBitmap(IMAGE_SIZE, IMAGE_SIZE, Bitmap.Config.ARGB_8888)
                val combinedImage = Canvas(combinedBitmap)
                combinedImage.drawColor(Color.WHITE)
                combinedImage.drawBitmap(croppedFirstImage, 0f, 0f, null)
                combinedImage.drawBitmap(secondImage, (IMAGE_SIZE - width).toFloat(), 0f, null)
                combinedImage.drawBitmap(thirdImage, (IMAGE_SIZE - width).toFloat(), (IMAGE_SIZE - width).toFloat(), null)

                combinedBitmap.toUri(context, true)
            }
            else -> {
                //1 on each corners, maximum 4 pictures
                val firstImage = Bitmap.createScaledBitmap(this[0].toBitmap(context), width, width, false)
                val secondImage = Bitmap.createScaledBitmap(this[1].toBitmap(context), width, width, false)
                val thirdImage = Bitmap.createScaledBitmap(this[2].toBitmap(context), width, width, false)
                val fourthImage = Bitmap.createScaledBitmap(this[3].toBitmap(context), width, width, false)

                //combined together
                val combinedBitmap  = Bitmap.createBitmap(IMAGE_SIZE, IMAGE_SIZE, Bitmap.Config.ARGB_8888)
                val combinedImage = Canvas(combinedBitmap)
                combinedImage.drawColor(Color.WHITE)
                combinedImage.drawBitmap(firstImage, 0f, 0f, null)
                combinedImage.drawBitmap(secondImage, (IMAGE_SIZE - width).toFloat(), 0f, null)
                combinedImage.drawBitmap(thirdImage, (IMAGE_SIZE - width).toFloat(), (IMAGE_SIZE - width).toFloat(), null)
                combinedImage.drawBitmap(fourthImage, 0f, (IMAGE_SIZE - width).toFloat(), null)

                combinedBitmap.toUri(context, true)
            }
        }
    }
}

fun Bitmap.toBase64String(): String{
    val bytes = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val byteArray = bytes.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun String.toBitmapFromBase64(): Bitmap{
    val bytes = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun Bitmap.reduceSize(maxSize: Int, imageTransformation: Transformation? = null) : Bitmap{
    var result = this

    imageTransformation?.let {
        result = imageTransformation.transform(this)
    }

    val ratio: Double = Math.sqrt(result.byteCount.toDouble()/maxSize.toDouble())
    if (ratio > 1) {
        val scaledWidthSize: Int = Math.floor(result.width/ratio).toInt()
        val scaledHeightSize: Int = Math.floor(result.height/ratio).toInt()
        result = Bitmap.createScaledBitmap(result, scaledWidthSize, scaledHeightSize, true)
    }

    return result
}