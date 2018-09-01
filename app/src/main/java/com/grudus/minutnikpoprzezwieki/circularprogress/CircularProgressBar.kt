package com.grudus.minutnikpoprzezwieki.circularprogress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.grudus.minutnikpoprzezwieki.R

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
    private var noProgressColor = Color.GRAY
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
            noProgressColor = attr.getInt(R.styleable.CircleProgressBar_noProgressColor, noProgressColor)
            min = attr.getInt(R.styleable.CircleProgressBar_min, min)
            max = attr.getInt(R.styleable.CircleProgressBar_max, max)
        } finally {
            attr.recycle()
        }

        backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundPaint.color = noProgressColor
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = 2F

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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val height = View.getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val min = Math.min(width, height)
        setMeasuredDimension(min, min)
        rectF.set(0 + strokeWidth / 2, 0 + strokeWidth / 2, min - strokeWidth / 2, min - strokeWidth / 2)
    }
}