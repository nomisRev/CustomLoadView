package be.vergauwen.simon.customloadview

import android.animation.Animator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import be.vergauwen.simon.customloadview.util.LayoutUtil

class LoadView @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0, defStyleRes: Int = 0)
: View(context, attrs, defStyleAttr, defStyleRes), Animator.AnimatorListener {

  //  constructor(context: Context) : super(context)
  //
  //  constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
  //
  //  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs,
  //                                                                                defStyleAttr, 0)
  //
  //  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
  //      context, attrs, defStyleAttr, defStyleRes) {
  //    getAttrs(attrs)
  //  }

  private val RADIUS = 300f
  private val paint = Paint()
  private val animator = animate()
  private var isGrowing = false
  private var isAnimating = false
  private val steps = 6.28318531 / 100
  private val originalX = width.toFloat() / 2f
  private val originalY = height.toFloat() / 2f
  private val rightLimit = LayoutUtil.getScreenWidth(context)
  private val minSize = LayoutUtil.convertDpToPixel(48f).toInt()
  private val leftLimit = 0

  init {
    paint.style = Paint.Style.FILL
    paint.isAntiAlias = true
    paint.color = Color.parseColor("#00FF00")

    animator.duration = 1000

    this.setOnLongClickListener {
      animator.cancel()
      false
    }
  }

  private fun getAttrs(attrs: AttributeSet) {

  }

  fun startAnimation() {
    animator.setListener(this)
    animateTheShitOutOfThis()
  }

  private fun animateTheShitOutOfThis() {
    if (isAnimating) return

    if (isGrowing) {
      animator.scaleXBy(-0.25f)
      animator.scaleYBy(-0.25f)
      isGrowing = false
    } else {
      animator.scaleXBy(0.25f)
      animator.scaleYBy(0.25f)
      isGrowing = true
    }

    animator.translationXBy(getMove())
    animator.start()
  }

  private var movingLeft = false
  private fun getMove(): Float {
    if (movingLeft && (this.x - 50f) < leftLimit ) {
      movingLeft = false
      return 50f
    } else if (movingLeft) {
      return -50f
    } else if ((this.x - 50f) > rightLimit) {
      return -50f
    }
    return 50f

  }
  //  private fun getNextX(): Float {
  //    return this.x
  //  }
  //
  //  private fun getNextY(): Float {
  //    return this.y
  //  }


  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

    val widthMode = MeasureSpec.getMode(widthMeasureSpec);
    val widthSize = MeasureSpec.getSize(widthMeasureSpec);
    val heightMode = MeasureSpec.getMode(heightMeasureSpec);
    val heightSize = MeasureSpec.getSize(heightMeasureSpec);
    var size: Int = 0

    when {
      (layoutParams.width == MATCH_PARENT && layoutParams.height == MATCH_PARENT) -> size = getExact(widthSize, heightSize)
      (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) -> size = getExact(widthSize, 0)
      (heightMode == MeasureSpec.EXACTLY && widthMode != MeasureSpec.EXACTLY) -> size = getExact(0, heightSize)
      (heightMode == MeasureSpec.EXACTLY && widthMode == MeasureSpec.EXACTLY) -> size = getExact(widthMode, heightMode)
      else -> size = minSize
    }

    setMeasuredDimension(size, size);
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

  private val rectF = RectF()

  override fun onDraw(canvas: Canvas) {
    canvas.drawColor(Color.RED)

    paint.color = Color.GREEN;
    paint.style = Paint.Style.FILL
    canvas.drawCircle(width.toFloat() / 2, height.toFloat() / 2, width.toFloat() / 2, paint)

    //    val x = width.toFloat()
    //    val y = height.toFloat()
    //
    //
    //
    //    paint.color = Color.RED;
    //    paint.style = Paint.Style.STROKE
    //    paint.strokeWidth = 550f
    //    rectF.set((x / 2) - (RADIUS + 100f) / 2, (y / 2) - (RADIUS + 100f) / 2,
    //              (x / 2) + (RADIUS + 100f) / 2, (y / 2) + (RADIUS + 100f) / 2)
    //    canvas.drawArc(rectF, 90f, 180f, true, paint)
    //
    //
    //    paint.color = Color.parseColor("#00FF00")
    //    paint.style = Paint.Style.FILL
    //    canvas.drawCircle(x / 2, y / 2, RADIUS, paint)
  }

  override fun onAnimationEnd(animation: Animator?) {
    isAnimating = false
    animateTheShitOutOfThis()
  }

  override fun onAnimationStart(animation: Animator?) {
    isAnimating = true
  }

  override fun onAnimationRepeat(animation: Animator?) {
    throw UnsupportedOperationException()
  }

  override fun onAnimationCancel(animation: Animator) {
    animator.setListener(null)
    animation.end()
    Log.e("CANCELLED", "BITCH")
  }
}