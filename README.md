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
