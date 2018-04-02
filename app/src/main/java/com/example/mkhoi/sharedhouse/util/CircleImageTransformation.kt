package com.example.mkhoi.sharedhouse.util

import android.graphics.*
import android.support.annotation.ColorInt
import com.squareup.picasso.Transformation

class CircleImageTransformation(private val borderRadius: Int = 0,
                                @ColorInt private val borderColor: Int = Color.WHITE) : Transformation {

    companion object {

        /**
         * A unique key for the transformation, used for caching purposes.
         */
        private const val KEY_CIRCLE_IMAGE_TRANSFORMATION = "KEY_CIRCLE_IMAGE_TRANSFORMATION"

    }

    override fun transform(source: Bitmap): Bitmap {

        val size = Math.min(source.width, source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f

        // Prepare the background
        val paintBg = Paint()
        paintBg.color = borderColor
        paintBg.isAntiAlias = true

        // Draw the background circle
        canvas.drawCircle(r, r, r, paintBg)

        // Draw the image smaller than the background so a little border will be seen
        canvas.drawCircle(r, r, r - borderRadius, paint)

        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String = KEY_CIRCLE_IMAGE_TRANSFORMATION

}
