# **Still in development, there will be a lot of changes in code and readme**

# Custom Load View

* Making a custom view can be quite troublesome if you've never done it before and depending on the use case.

## onMeasure()

* Before starting to draw, we should make sure that the `Canvas` we'll be drawing on has the size we want. Think of the `Canvas` as a piece of paper you draw on. If the paper is too small, your drawing won't fit. If your paper is to big, it will take an unnecessary amount of space in a collage or on the wall.
* To follow next code snippet you should read and understand the **Layout** section, http://developer.android.com/reference/android/view/View.html#Layout

```
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
```

* The above piece of code will become clear in a moment, but **copying this useless!!** as this piece of code is the result of my specific use case and my decisions.
* First thing we do is get all the `MeasureSpec` data we need, the measure modes and the sizes passed to us.
* Understanding the modes, if a specific size is passed in xml, the mode for that size will be `MeasureSpec.EXACTLY`. Since in my use case I will be drawing a circle. I want a square canvas, thus I will take the exactly size as a side length. In case both are passed a specific size, we will set the biggest as the side length.
* If both are set to match_parent, assign the maximum size
* In all cases that don't match two conditions above, we will assign the minimum size.

#### Padding support

* Adding padding support for padding is not required, that's why it resides in a seperate topic.
* I recommened adding padding support from the start, to make your view more flexible and reuseable and uniform with your code and the Android system. 
* Padding behaviour:
  * Padding applied to `wrap_content` increases the size of the view, in order to wrap around the content inside the view
  * When `exactly` specified or set to `match_parent` the size of the view may not be increased, padding now reduces the "useable" space inside the canvas.

* Knowing this, it's quite easy to add padding functionality for us in our `onMeasure()` method. In the case that `minSize` would be determined as the size, we increase minSize with the padding.
```
var w: Int = 0
var h: Int = 0

when {
  (layoutParams.width == MATCH_PARENT && layoutParams.height == MATCH_PARENT) -> { w = getExact(widthSize, heightSize); h = w }
  (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) -> { w = getExact(widthSize, 0); h = w }
  (heightMode == MeasureSpec.EXACTLY && widthMode != MeasureSpec.EXACTLY) -> { w = getExact(0, heightSize); h= w }
  (heightMode == MeasureSpec.EXACTLY && widthMode == MeasureSpec.EXACTLY) -> { w = getExact(widthMode, heightMode); h= w }
  else -> { w = minSize + paddingStart + paddingEnd; h =  minSize + paddingTop + paddingBottom }
}

setMeasuredDimension(w, h);
```

* This is all we need to do to add padding support in this use case. It might be quite different, but once your onMeasure works as intended. We can finally begin with the fun stuff!


## onDraw()

* This is where all the magic happens and you draw your very own view!
* It is **important** that you keep your onDraw method as fast as possible. Since you have no control of how much it's called. Let's say it's slow, and another view calls `requestLayout()` your view's `onDraw()` might be called quite frequently. For that reason I choose to declare my variables outside of the `onDraw()` method.

```
  private var sizeOuterCircle = 40f
  private var sizeInnerCircle = 0f
  private var rect = RectF()
  private var innerColor = Color.GREEN
  private var outerColor = Color.CYAN

  override fun onDraw(canvas: Canvas) {
    sizeInnerCircle = (width - sizeOuterCircle).toFloat()

    paint.color = outerColor
    canvas.drawArc(0.toFloat(), 0.toFloat(),width.toFloat(), height.toFloat(), 270f, 180f, true, paint)

    paint.color = innerColor;
    canvas.drawCircle(width.toFloat() / 2, height.toFloat() / 2, sizeInnerCircle / 2, paint)
  }
```

* Let's break it down: We have an inner and an outer part.
  * The inner part is our `FAB`, so let's just draw a circle with the origin in the center of the canvas and the radius half the size of the circle. The size of the circle is determined by the size of the canvas and the width of the "progress" arc around our `FAB`
  * The outer part has a configurable width and is drawn below our inner part.
  * If you are unsure on how the methods `drawArc()` and `drawCircle()` work, I recommend that your read the documentation (http://developer.android.com/reference/android/graphics/Canvas.html) or navigate to it in Android studio (cmd+click or ctrl+click)
  

#### Padding support
