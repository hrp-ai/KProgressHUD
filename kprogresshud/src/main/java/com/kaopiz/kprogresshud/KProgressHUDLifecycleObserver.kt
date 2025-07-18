package com.kaopiz.kprogresshud

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

/**
 *  这个类用于在Activity销毁时自动处理KProgressHUD的生命周期。
 */
internal class KProgressHUDLifecycleObserver private constructor(
    kProgressHUD: KProgressHUD
) : DefaultLifecycleObserver {

    private val reference = WeakReference(kProgressHUD)

    companion object {
        private const val TAG = "KProgressHUD"

        // 添加工厂方法自动注册观察者
        @JvmStatic
        fun attachTo(kProgressHUD: KProgressHUD, context: Context): KProgressHUDLifecycleObserver? {
            val activity = findActivity(context)
            return if (activity is LifecycleOwner) {
                val observer = KProgressHUDLifecycleObserver(kProgressHUD)
                try {
                    activity.lifecycle.addObserver(observer)
                } catch (e: Exception) {
                    Log.e(TAG, "Error attaching observer", e)
                }
                observer
            } else {
                Log.w(TAG, "Context is not a LifecycleOwner")
                null
            }
        }

        @JvmStatic
        tailrec fun findActivity(context: Context?): Activity? {
            return when (context) {
                is Activity -> context
                is ContextWrapper -> findActivity(context.baseContext)
                else -> null
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        try {
            val kProgressHUD = reference.get() ?: run {
                Log.d(TAG, "KProgressHUD reference is null")
                return
            }

            if (kProgressHUD.isShowing) {
                Log.d(TAG, "Auto-dismissing KProgressHUD on Activity destroy")
                kProgressHUD.dismiss()
            }

            // 清理观察者避免内存泄漏
            owner.lifecycle.removeObserver(this)
        } catch (e: Exception) {
            Log.e(TAG, "Error in onDestroy", e)
        }
    }
}