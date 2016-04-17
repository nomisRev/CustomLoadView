package be.vergauwen.simon.customloadview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import be.vergauwen.simon.customloadview.util.LayoutUtil

class LoadView @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0, defStyleRes: Int = 0)
: View(context, attrs, defStyleAttr, defStyleRes) {

  private val paint = Paint()
  private val minSize = LayoutUtil.convertDpToPixel(48f).toInt()
  private var widthOuterArc = 0f
  private var sizeInnerCircle = 0f
  private var innerColor = Color.GREEN
  private var outerColor = Color.CYAN
  private var max = 0
  private var progress = 0
  private var topArc = 0f
  private var leftArc = 0f
  private var rightArc = 0f
  private var bottomArc = 0f

  init {
    paint.style = Paint.Style.FILL
    paint.isAntiAlias = true
    paint.color = Color.parseColor("#00FF00")
    getAttrs(attrs, defStyleAttr, defStyleRes)
  }


  private fun getAttrs(attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
    val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.LoadView, defStyleAttr,
                                                          defStyleRes);

    try {
      innerColor = attributes.getColor(R.styleable.LoadView_inner_color,Color.GREEN)
      outerColor  = attributes.getColor(R.styleable.LoadView_outer_color,Color.CYAN)
      max = attributes.getInteger(R.styleable.LoadView_max,0)
      widthOuterArc = attributes.getFloat(R.styleable.LoadView_width_progress,40f)
    } finally {
      attributes.recycle();
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // onMeasure Impl

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

    val widthMode = MeasureSpec.getMode(widthMeasureSpec);
    val widthSize = MeasureSpec.getSize(widthMeasureSpec);
    val heightMode = MeasureSpec.getMode(heightMeasureSpec);
    val heightSize = MeasureSpec.getSize(heightMeasureSpec);
    var w: Int
    var h: Int

    when {
      (layoutParams.width == MATCH_PARENT && layoutParams.height == MATCH_PARENT) -> {
        w = getExact(widthSize, heightSize); h = w
      }
      (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) -> {
        w = getExact(widthSize, 0); h = w
      }
      (heightMode == MeasureSpec.EXACTLY && widthMode != MeasureSpec.EXACTLY) -> {
        w = getExact(0, heightSize); h = w
      }
      (heightMode == MeasureSpec.EXACTLY && widthMode == MeasureSpec.EXACTLY) -> {
        w = getExact(widthMode, heightMode); h = w
      }
      else -> {
        w = minSize + paddingStart + paddingEnd; h = minSize + paddingTop + paddingBottom
      }
    }

    setMeasuredDimension(w, h);
  }

  private fun getExact(w: Int, h: Int): Int {
    var size: Int
    if (w < h && minSize < h) {
      size = h
    } else if (minSize < w) {
      size = w
    } else {
      size = minSize
    }
    return size
  }

 ///////////////////////////////////////////////////////////////////////////////////////////////////
  // onDraw Impl

  override fun onDraw(canvas: Canvas) {
    drawProgress(canvas)
    drawInnerCircle(canvas)
  }

  private fun drawProgress(canvas : Canvas){
    paint.color = outerColor
    topArc = (0 + paddingStart ).toFloat()
    rightArc = (0 + paddingTop ).toFloat()
    bottomArc = (width - paddingEnd).toFloat()
    leftArc = (height - paddingBottom).toFloat()
    canvas.drawArc(topArc, rightArc, bottomArc, leftArc, 270f, (360f / max) * progress, true, paint)
  }

  private fun drawInnerCircle(canvas: Canvas){
    paint.color = innerColor
    sizeInnerCircle = (width - paddingStart - paddingEnd - widthOuterArc).toFloat()
    canvas.drawCircle(width.toFloat() / 2, height.toFloat() / 2, sizeInnerCircle / 2, paint)
  }



  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Programmatically equivalent of xml attributes

  fun setMax(max: Int) {
    if (0 <= max) {
      this.max = max
      invalidate()
    }
  }

  fun setProgress(progress: Int) {
    this.progress = progress
    invalidate()
  }

  fun setProgressColor(color: Int) {
    outerColor = color
    invalidate()
  }

  fun setFABColor(color: Int) {
    innerColor = color
    invalidate()
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Load View Impl

  fun incrementProgressBy(incrementBy: Int) {
    if (progress + incrementBy < max) {
      progress += incrementBy
    } else {
      progress = max
    }
    invalidate()
  }

  fun resetProgress() {
    progress = 0
    invalidate()
  }
}