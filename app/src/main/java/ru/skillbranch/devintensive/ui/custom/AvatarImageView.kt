package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.graphics.toColorInt
import ru.skillbranch.devintensive.utils.Utils
import kotlin.random.Random

class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CircleImageView(context, attrs, defStyleAttr)
/*
{

    private var initials: String? = null
    private val bgColors = arrayOf(
        "#7BC862",
        "#E17076",
        "#FAA774",
        "#6EC9CB",
        "#65AADD",
        "#A695E7",
        "#EE7AAE"
    )

    fun setInitials(_initials: String) {
        initials = _initials
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null && initials != null) {
            val rand = Random.nextInt(bgColors.size)
            val color = bgColors[rand].toColorInt()

            var bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.textSize = Utils.convertSpToPx(14)
            paint.color = Color.WHITE
            paint.textAlign = Paint.Align.CENTER

            val subCanvas = Canvas(bitmap)
            subCanvas.drawColor(color)

            val textBounds = Rect()
            paint.getTextBounds(initials, 0, initials!!.length, textBounds)

            subCanvas.drawText(
                initials,
                width / 2f,
                layoutParams.height / 2f + textBounds.height() / 2f,
                paint
            )

            subCanvas.drawBitmap(bitmap, 0f, 0f, null)

            this.setImageBitmap(bitmap)
        }
        super.onDraw(canvas)
    }
}
*/