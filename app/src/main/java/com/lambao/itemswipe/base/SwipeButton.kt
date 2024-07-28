package com.lambao.itemswipe.base

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

data class SwipeButton(
    val context: Context,
    val imageResId: Int,
    val backgroundColor: Int,
    val text: String,
    val fontSize: Float?,
    val fontColor: Int?,
    val fontFamily: Int?,
    val iconTextSpacing: Float,
    val listener: OnSwipeButtonClickListener
) {
    private var position = 0
    private var clickRegion: RectF? = null

    fun onClick(x: Float, y: Float): Boolean {
        if (clickRegion != null && clickRegion?.contains(x, y) == true) {
            listener.onClick(position)
            return true
        }
        return false
    }

    fun onDraw(c: Canvas, rectF: RectF, position: Int) {
        val p = Paint()
        p.color = backgroundColor
        c.drawRect(rectF, p)

        // Vẽ biểu tượng
        val d = ContextCompat.getDrawable(context, imageResId) ?: return
        val bitmap = drawableToBitmap(d)

        val bw = bitmap.width.toFloat()
        val bh = bitmap.height.toFloat()

        // Tính toán vị trí biểu tượng
        val iconX = (rectF.left + rectF.right) / 2 - bw / 2
        val iconY =
            (rectF.top + rectF.bottom) / 2 - (bh / 2) - iconTextSpacing // Điều chỉnh khoảng cách theo chiều dọc

        c.drawBitmap(bitmap, iconX, iconY, p)

        // Vẽ chữ
        if (fontColor != null) {
            p.color = fontColor
        } // Sử dụng màu font được truyền vào
        p.textSize = fontSize ?: 20f // Sử dụng kích thước font được truyền vào
        p.textAlign = Paint.Align.CENTER

        // Thiết lập font family
        val typeface = fontFamily?.let { ResourcesCompat.getFont(context, it) }
        p.setTypeface(typeface)

        // Tính toán vị trí chữ
        val fontMetrics = p.fontMetrics
        val textHeight = fontMetrics.descent - fontMetrics.ascent
        val textY =
            iconY + bh + iconTextSpacing + textHeight / 2

        c.drawText(text, (rectF.left + rectF.right) / 2, textY, p)

        clickRegion = rectF
        this.position = position
    }

    private fun drawableToBitmap(d: Drawable): Bitmap {
        if (d is BitmapDrawable) {
            return Bitmap.createScaledBitmap(d.bitmap, 160, 160, true)
        }
        val bitmap =
            Bitmap.createBitmap(d.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        d.setBounds(0, 0, canvas.width, canvas.height)
        d.draw(canvas)

        return bitmap
    }
}