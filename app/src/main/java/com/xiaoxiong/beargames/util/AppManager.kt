package com.xiaoxiong.beargames.util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.util.*
import kotlin.system.exitProcess

/**
 * @author: tanliang
 * @date:Create at 15:40 2022/11/21
 * @Description:
 */
object AppManager {
    private val activityStack: Stack<Activity> = Stack()

    fun appExit(context: Context, isBackground: Boolean) {
        try {
            finishAllActivity()
            val activityMgr = context
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.restartPackage(context.packageName)
        } catch (e: Exception) {

        } finally {
            // 注意，如果您有后台程序运行，请不要支持此句子
            if ((!isBackground)) {
                exitProcess(0)
            }
        }
    }

    fun finishAllActivity() {
        for (activity in activityStack) {
            activity?.finish()
        }
        activityStack.clear()
    }

    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }
}