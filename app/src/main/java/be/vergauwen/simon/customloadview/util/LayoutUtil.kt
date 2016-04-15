package be.vergauwen.simon.customloadview.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

object LayoutUtil {

  fun convertPixelsToDp(px: Float): Float {
    val metrics = Resources.getSystem().displayMetrics
    val dp = px / (metrics.densityDpi / 160f)
    return Math.round(dp).toFloat()
  }

  fun convertDpToPixel(dp: Float): Float {
    val metrics = Resources.getSystem().displayMetrics
    val px = dp * (metrics.densityDpi / 160f)
    return Math.round(px).toFloat()
  }

  fun getScreenWidth(context: Context): Int {
    return context.resources.displayMetrics.widthPixels
  }

  fun getScreenHeight(context: Context): Int {
    return context.resources.displayMetrics.heightPixels
  }

  fun getAvailableHeight(context: Context): Int {
    return getScreenHeight(context) - getActionBarHeight(context) - getStatusBarHeight(context)
  }

  fun getActionBarHeight(context: Context): Int {
    val tv = TypedValue()
    context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
    return context.resources.getDimensionPixelSize(tv.resourceId)
  }

  fun getStatusBarHeight(context: Context): Int {
    var result = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
      result = context.resources.getDimensionPixelSize(resourceId)
    }
    return result
  }
}