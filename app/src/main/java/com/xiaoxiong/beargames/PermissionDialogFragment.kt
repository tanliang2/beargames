package com.xiaoxiong.beargames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.*
import com.xiaoxiong.beargames.util.SPUtils
import com.xiaoxiong.beargames.util.ScreenUtils

class PermissionDialogFragment : DialogFragment() {
    private lateinit var tvCancel : TextView
    private lateinit var tvOk : TextView
    private lateinit var tvContent : TextView

    override fun onStart() {
        super.onStart()
        resetWindow()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = View.inflate(context,R.layout.dialog_permission,null)
//        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        tvCancel = view.findViewById(R.id.btn_exit)
        tvOk = view.findViewById(R.id.btn_confirm)
        tvContent = view.findViewById(R.id.tvContent)
        tvContent.text = Constants.PRIVACY_CONTENT
        tvCancel.setOnClickListener {
            this.dismiss()
        }
        tvOk.setOnClickListener {
            this.dismiss()
            SPUtils.setSharedBooleanData(requireContext(),KEY_AGREE_PRIVACY,true)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isAdded) {
            return
        }
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 在FragmentTransaction中， add和attach方法来添加fragment，这2个方法中的动作并不会立即执行，而是将OP任务加入了自己的队列。
     * OP任务在等待commit系列方法提交事务之后执行，但同时commit 方法提交的任务加入到了主线程Looper中，如果Looper阻塞，add OP可能会延迟。
     * 所以可能会导致重复添加fragment异常
     * 使用 commitNow 任务不会被加入主线Looper，可以立即执行
     */
    fun showDialogNow(activity: FragmentActivity) {
        if (!this.isAdded) {
            val ft: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            ft.setReorderingAllowed(true)
            ft.add(this, tag)
            ft.commitNow()
        }
    }

    fun showDialogNow(fragment: Fragment) {
        if (!this.isAdded) {
            val ft: FragmentTransaction = fragment.childFragmentManager.beginTransaction()
            ft.setReorderingAllowed(true)
            ft.add(this, tag)
            ft.commitNow()
        }
    }

    override fun dismiss() {
        if (fragmentManager == null) {
            return
        }
        super.dismiss()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }


    open fun resetWindow() {
        val dialog = dialog
        dialog?.let {
            dialog.setCancelable(false)
            val window = dialog.window
            if (window != null) {
                window.setBackgroundDrawableResource(R.drawable.shape_round_8)
                activity?.let {
                    val lp = window.attributes
                    if (dialogWidth() > 0) {
                        lp.width = dialogWidth()
                    }
                    if (dialogHeight() > 0) {
                        lp.height = dialogHeight()
                    }
                    if (offsetX() != 0) {
                        lp.x = lp.x + offsetX()
                    }
                    if (offsetY() != 0) {
                        lp.y = lp.y + offsetY()
                    }
                    window.attributes = lp
                }
            }
        }
    }

    /*y轴的偏移量 正的代表向下偏移，负的代表向上偏移*/
    protected open fun offsetY(): Int {
        return 0
    }

    protected open fun offsetX(): Int {
        return 0
    }

     fun dialogHeight(): Int = ScreenUtils.getScreenHeight(requireContext()) * 120 / 180
     fun dialogWidth(): Int = ScreenUtils.getScreenWidth(requireContext()) * 164 / 180

    companion object {
        const val KEY_AGREE_PRIVACY = "key_agree_privacy"
    }

}
