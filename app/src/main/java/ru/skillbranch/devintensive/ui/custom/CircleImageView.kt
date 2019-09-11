package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.graphics.drawable.toBitmap
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2F
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = Utils.convertDpToPx(DEFAULT_BORDER_WIDTH)

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor =
                a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth =
                a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, borderWidth)
            a.recycle()
        }
    }

    @Dimension
    fun getBorderWidth(): Int = Utils.convertPxToDp(borderWidth)

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = Utils.convertDpToPx(dp.toFloat())
        invalidate()
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = resources.getColor(colorId, context.theme)
        invalidate()
    }

    fun setInitials(initials: String?) {
        if (initials == null)
            setImageResource(R.drawable.avatar_default)
        else
            setImageBitmap(getTextAvatar(initials))
    }

    private fun getTextAvatar(initials: String): Bitmap? {
        val color = TypedValue()
        context.theme.resolveAttribute(R.attr.colorAccent, color, true)

        val bitmap: Bitmap = Bitmap.createBitmap(
            layoutParams.width,
            layoutParams.height,
            Bitmap.Config.ARGB_8888
        )

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = layoutParams.height / 2f
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER

        val canvas = Canvas(bitmap)
        canvas.drawColor(color.data)

        val textBounds = Rect()
        paint.getTextBounds(initials, 0, initials.length, textBounds)

        canvas.drawText(initials, layoutParams.width / 2f, layoutParams.height / 2f + textBounds.height() / 2f, paint)

        return bitmap
    }


    override fun onDraw(canvas: Canvas) {
        var bitmap = getBitmapFromDrawable() ?: return
        bitmap = getScaledBitmap(bitmap, width)
        bitmap = cropCenterBitmap(bitmap)
        bitmap = makeCircleBitmap(bitmap)

        if (borderWidth > 0)
            bitmap = createRoundBorderBitmap(bitmap, borderWidth, borderColor)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    private fun getBitmapFromDrawable(): Bitmap? {
        return if (drawable != null) {
            if (drawable is BitmapDrawable)
                (drawable as BitmapDrawable).bitmap
            else drawable.toBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        } else null
    }

    private fun getScaledBitmap(bitmap: Bitmap, size: Int): Bitmap {
        return if (bitmap.width != size || bitmap.height != size) {
            val minSide = min(bitmap.width, bitmap.height).toFloat()
            val factor = minSide / size
            Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width / factor).toInt(),
                (bitmap.height / factor).toInt(),
                false
            )
        } else bitmap
    }

    private fun cropCenterBitmap(bitmap: Bitmap): Bitmap {
        val newWidth = (bitmap.width) / 2
        val newHeight = (bitmap.height) / 2

        return if (bitmap.height >= bitmap.width) {
            Bitmap.createBitmap(bitmap, 0, newHeight - newWidth, bitmap.width, bitmap.width)
        } else {
            Bitmap.createBitmap(bitmap, newWidth - newHeight, 0, bitmap.height, bitmap.height)
        }
    }

    private fun makeCircleBitmap(bitmap: Bitmap): Bitmap {
        val smallest = min(bitmap.width, bitmap.height)
        val outputBmp = Bitmap.createBitmap(smallest, smallest, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outputBmp)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        canvas.drawCircle(smallest / 2F, smallest / 2F, smallest.toFloat() / 2F, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, 0F, 0F, paint)

        return outputBmp
    }

    private fun createRoundBorderBitmap(
        bitmap: Bitmap,
        borderWidth: Int,
        borderColor: Int
    ): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.color = borderColor
        paint.strokeWidth = borderWidth.toFloat()

        val smallest = min(bitmap.width, bitmap.height)
        val canvas = Canvas(bitmap)
        canvas.drawCircle(
            smallest / 2F,
            smallest / 2F,
            smallest.toFloat() / 2F - borderWidth / 2,
            paint
        )

        return bitmap
    }
}
