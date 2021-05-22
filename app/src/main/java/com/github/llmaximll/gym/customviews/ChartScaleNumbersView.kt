package com.github.llmaximll.gym.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.github.llmaximll.gym.R

private const val PADDING_BOTTOM = 80f
private const val PADDING_START = 100f
private const val DEFAULT_MODE = 0

class ChartScaleNumbersView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val textPaint = Paint()

    private var mWidth = 100
    private var mHeight = 800
    private var mode: Int = DEFAULT_MODE

    init {
        setAttributes(attrs)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawScaleNumbers(canvas)
    }

    private fun drawScaleNumbers(canvas: Canvas?) {
        textPaint.apply {
            isAntiAlias = true
            color = Color.parseColor("#2699FB")
            textSize = 20f
            isFakeBoldText = false
        }
        var diffHeight = mHeight.toFloat()
        var scaleNumber = 0

        val count = when (mode) {
            0 -> 17
            1 -> 10
            else -> 16
        }

        repeat(count) {
            canvas?.drawText(scaleNumber.toString(), PADDING_START * 0.4f,
                diffHeight - PADDING_BOTTOM, textPaint)
            when (mode) {
                0 -> {
                    scaleNumber++
                    diffHeight -= 40f
                }
                1 -> {
                    scaleNumber += 20
                    diffHeight -= 70f
                }
            }
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

    private fun setAttributes(attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ChartScaleNumbersView,
            0, 0)
        mode = typedArray.getInt(R.styleable.ChartScaleNumbersView_mode, DEFAULT_MODE)
        typedArray.recycle()
    }
}