package com.github.llmaximll.gym.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import com.github.llmaximll.gym.BuildConfig

private const val TAG = "ChartView"
private const val PADDING_BOTTOM = 80f
private const val PADDING_START = 0f

class ChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    //colors
    @ColorInt private val backgroundColor = Color.BLACK
    @ColorInt private val foregroundColor = Color.parseColor("#2699FB")
    //paint
    private val bgPaint = Paint()
    private val fgPaint = Paint()
    private val textPaint = Paint()
    private val rectF = RectF()
    //size
    private var mWidth = 600
    private var mHeight = 300

    var dataItem = 0f
        set(count) {
            field = count
            dataList.add(count)
            mWidth += 129
            invalidate()
            requestLayout()
        }
    private val dataList = mutableListOf<Float>()

    init {
        bgPaint.apply {
            isAntiAlias = true
            color = backgroundColor
        }
        fgPaint.apply {
            color = foregroundColor
        }
        textPaint.apply {
            color = foregroundColor
            isAntiAlias = true
            textSize = 20f
            textAlign = Paint.Align.CENTER
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawChart(canvas)
        drawNumbers(canvas)
    }

    private fun drawChart(canvas: Canvas?) {
        var left = 0f
        var right = 70f
        for (dataItem in dataList) {
            rectF.set(
                left + PADDING_START,
                height - dataItem - PADDING_BOTTOM,
                right + PADDING_START,
                height - PADDING_BOTTOM)
            canvas?.drawRect(rectF, fgPaint)
            left += 150f
            right += 150f
        }
    }

    private fun drawNumbers(canvas: Canvas?) {
        var diffWidth = 35f

        textPaint.apply {
            isAntiAlias = true
            color = Color.parseColor("#2699FB")
            textSize = 30f
            isFakeBoldText = true
        }
        for (number in 1..dataList.size) {
            canvas?.drawText(number.toString(), diffWidth + PADDING_START,
                mHeight - PADDING_BOTTOM * 0.4f, textPaint)
            diffWidth += 150f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                mWidth = MeasureSpec.getSize(widthMeasureSpec)
            }
            MeasureSpec.AT_MOST -> {
                //
            }
            MeasureSpec.UNSPECIFIED -> {
                //
            }
        }
        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                mHeight = MeasureSpec.getSize(heightMeasureSpec)
            }
            MeasureSpec.AT_MOST -> {
                mHeight = mHeight.coerceAtMost(MeasureSpec.getSize(heightMeasureSpec))
            }
            MeasureSpec.UNSPECIFIED -> {
                //
            }
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    private fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    companion object {
        const val STEP_TRAINING = 40f
    }
}