package com.android.loan.ca.ui.main.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.xiaoxiong.beargames.R
import com.xiaoxiong.beargames.util.AppManager


/**
 * @author xcb
 * date：12/21/20 1:44 PM
 * description:跳转到google 去评分 对话框
 */
class ExitDialog : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.dialog_exit_app, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)
        val tvOk = view.findViewById<TextView>(R.id.tvOk)
        tvCancel.setOnClickListener {
            this.dismiss()
        }
        tvOk.setOnClickListener {
            //openGooglePlay(requireContext())
            this.dismiss()
            AppManager.appExit(requireContext(),false)
        }
    }

    override fun onStart() {
        super.onStart()
        resetWindow()
    }

    private fun resetWindow() {
        val window = dialog?.window
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.shape_round_8)
            val lp = window.attributes
            lp.width = resources.displayMetrics.widthPixels * 164 / 180
            lp.gravity = Gravity.CENTER
            window.attributes = lp
        }
    }

    /**
     * 打开Google Play
     */
    private fun openGooglePlay(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=${context.packageName}")
            intent.setPackage("com.android.vending")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                val intentBrowser = Intent(Intent.ACTION_VIEW)
                intentBrowser.data =
                    Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (intentBrowser.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intentBrowser)
                }
            }
        } catch (e: Exception) {
            val intentBrowser = Intent(Intent.ACTION_VIEW)
            intentBrowser.data =
                Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
            intentBrowser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (intentBrowser.resolveActivity(context.packageManager) != null) {
                context.startActivity(intentBrowser)
            }
        }
    }
}