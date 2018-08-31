package com.grudus.minutnikpoprzezwieki

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

// Copied from https://github.com/MRezaNasirloo/CircularProgressBar

class CircularProgressBar(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {
    var progress = 0F
        set(value) {
            field = value
            invalidate()
        }

    private var strokeWidth = 4F
    private var min = 0
    private var max = 100
    /**
     * Start the progress at 12 o'clock
     */
    private var startAngle = -90F
    private var color = Color.DKGRAY
    private val rectF: RectF = RectF()
    private val backgroundPaint: Paint
    private val foregroundPaint: Paint


    init {
        val attr = context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.CircleProgressBar,
                0, 0)

        try {
            strokeWidth = attr.getDimension(R.styleable.CircleProgressBar_progressBarThickness, strokeWidth)
            progress = attr.getFloat(R.styleable.CircleProgressBar_progress, progress)
            color = attr.getInt(R.styleable.CircleProgressBar_progressbarColor, color)
            min = attr.getInt(R.styleable.CircleProgressBar_min, min)
            max = attr.getInt(R.styleable.CircleProgressBar_max, max)
        } finally {
            attr.recycle()
        }

        backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundPaint.color = adjustAlpha(color, 0.1f)
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = strokeWidth

        foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        foregroundPaint.color = color
        foregroundPaint.style = Paint.Style.STROKE
        foregroundPaint.strokeWidth = strokeWidth
    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawOval(rectF, backgroundPaint)
        val angle = 360 * progress / max
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint)

    }


    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = Math.round(Color.alpha(color) * factor)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val height = View.getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val min = Math.min(width, height)
        setMeasuredDimension(min, min)
        rectF.set(0 + strokeWidth / 2, 0 + strokeWidth / 2, min - strokeWidth / 2, min - strokeWidth / 2)
    }
}