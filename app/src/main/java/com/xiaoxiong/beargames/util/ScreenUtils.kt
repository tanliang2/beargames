package com.xiaoxiong.beargames.util


import android.app.Activity
import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * @author lindan
 * 获得屏幕相关的辅助类
 */
object ScreenUtils {
    /**
     * 获得屏幕高度
     *
     * @param context Context
     * @return screen width
     */
    fun getScreenWidth(context: Context): Int {
        return getScreenSize(context)[0]
    }

    /**
     * 获得屏幕宽度
     *
     * @param context Context
     * @return screen height
     */
    fun getScreenHeight(context: Context): Int {
        return getScreenSize(context)[1]
    }

    /**
     * 获取屏幕大小
     *
     * @param context Context
     * @return 长度为2的数组，由宽、高组成
     */
    fun getScreenSize(context: Context): IntArray {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(outMetrics)
        return intArrayOf(outMetrics.widthPixels, outMetrics.heightPixels)
    }

    /**
     * 屏幕宽高比
     *
     * @param context Context
     * @return 屏幕宽高比
     */
    fun getScreenRatio(context: Context): Double {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(outMetrics)
        return outMetrics.heightPixels / (outMetrics.widthPixels + 0.0)
    }

    /**
     * 获得状态栏的高度
     *
     * @param context Context
     * @return statusbar height
     */
    fun getStatusHeight(context: Context): Int {
        var result = 0
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 获取导航栏高度，不管是否显示，都有值
     */
    fun getNavigationBarHeight(context: Activity): Int {
        val resources = context.resources
        val resourceId =
            resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId != 0) {
            return try {
                val result = resources.getDimensionPixelSize(resourceId)
                if (result <= 1) 0 else result
            } catch (e: Exception) {
                0
            }

        }
        return 0
    }


    /**
     * 华为手机是否隐藏了虚拟导航栏
     * @return true 表示隐藏了，false 表示未隐藏
     */
    private fun isHuaWeiHideNav(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Settings.System.getInt(context.contentResolver, "navigationbar_is_min", 0)
        } else {
            Settings.Global.getInt(context.contentResolver, "navigationbar_is_min", 0)
        } != 0

    /**
     * 小米手机是否开启手势操作
     * @return true 表示使用的是手势，false 表示使用的是虚拟导航栏(NavigationBar)，默认是false
     */
    private fun isMiuiFullScreen(context: Context) =
        Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0) != 0

    /**
     * Vivo手机是否开启手势操作
     * @return true 表示使用的是手势，false 表示使用的是虚拟导航栏(NavigationBar)，默认是false
     */
    private fun isVivoFullScreen(context: Context) =
        Settings.Secure.getInt(context.contentResolver, "navigation_gesture_on", 0) != 0

    /**
     * 根据屏幕真实高度与显示高度，判断虚拟导航栏是否显示
     * @return true 表示虚拟导航栏显示，false 表示虚拟导航栏未显示
     */
    private fun isHasNavigationBar(context: Activity): Boolean {
        val windowManager: WindowManager =
            context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        val realHeight: Int
        val realWidth: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowBounds = context.windowManager.currentWindowMetrics.bounds
            realHeight = windowBounds.height()
            realWidth = windowBounds.width()
        } else {
            val realDisplayMetrics = DisplayMetrics()
            display.getRealMetrics(realDisplayMetrics)
            realHeight = realDisplayMetrics.heightPixels
            realWidth = realDisplayMetrics.widthPixels
        }

        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels

        // 部分无良厂商的手势操作，显示高度 + 导航栏高度，竟然大于物理高度，对于这种情况，直接默认未启用导航栏
        if (displayHeight > displayWidth) {
            if (displayHeight + getNavigationBarHeight(context) > realHeight) return false
        } else {
            if (displayWidth + getNavigationBarHeight(context) > realWidth) return false
        }

        return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity Activity
     * @return Bitmap
     */
    fun snapshotWithStatusBar(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val width = getScreenWidth(activity)
        val height = getScreenHeight(activity)
        val bp = Bitmap.createBitmap(bmp, 0, 0, width, height)
        view.destroyDrawingCache()
        return bp

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity Activity
     * @return Bitmap
     */
    fun snapshotWithoutStatusBar(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top

        val width = getScreenWidth(activity)
        val height = getScreenHeight(activity)
        val bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight)
        view.destroyDrawingCache()
        return bp
    }
}