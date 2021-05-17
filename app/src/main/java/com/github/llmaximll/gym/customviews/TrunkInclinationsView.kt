package com.github.llmaximll.gym.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class TrunkInclinationsView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var baseColor = Color.parseColor("#FF77A1D3")
    // View size in pixels
    private var sizeX = 350
    private var sizeY = 200

    var arrowAngle = DEFAULT_ARROW_ANGLE
        set(count) {
            field = when {
                count < -9f -> -9f * 10f
                count > 9f -> 9f * 10f
                else -> count * 10f
            }
            invalidate()
        }

    init {
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawBackground(canvas)
        drawText(canvas)
        drawArrow(canvas)
    }

    private fun drawBackground(canvas: Canvas?) {
        paint.color = baseColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f

        val oval = RectF()
        val centerX = sizeX * 0.5f
        val centerY = sizeY * 0.85f
        val radius = sizeX * 0.41f

        oval.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        canvas?.drawArc(oval, 180f, 180f, true, paint)
    }

    private fun drawText(canvas: Canvas?) {
        val textPaint = Paint()
        textPaint.apply {
            isAntiAlias = true
            color = Color.parseColor("#FF77A1D3")
            textSize = 25f
        }
        // text on the left
        canvas?.drawText("Step", sizeX * 0.015f, sizeY * 0.85f, textPaint)
        // text on the right
        canvas?.drawText("Step", sizeX * 0.928f, sizeY * 0.85f, textPaint)
    }

    private fun drawArrow(canvas: Canvas?) {
        paint.strokeWidth = 8f

        canvas?.rotate(arrowAngle, sizeX * 0.5f, sizeY * 0.85f)

        // base line
        canvas?.drawLine(sizeX * 0.5f, sizeY * 0.85f, sizeX * 0.5f, sizeY * 0.13f, paint)
        // left line
        canvas?.drawLine(sizeX * 0.5f, sizeY * 0.14f, sizeX * 0.46f, sizeY * 0.28f, paint)
        // right line
        canvas?.drawLine(sizeX * 0.5f, sizeY * 0.14f, sizeX * 0.54f, sizeY * 0.28f, paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        sizeX = measuredWidth
        sizeY = measuredHeight
        setMeasuredDimension(sizeX, sizeY)
    }

    companion object {
        private const val DEFAULT_ARROW_ANGLE = 0f
    }
}