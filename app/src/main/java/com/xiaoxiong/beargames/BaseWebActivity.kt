package com.xiaoxiong.beargames

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.xiaoxiong.beargames.util.StatusBarUtil

/**
 * @author tanliang
 * webView
 */
@SuppressLint("SetJavaScriptEnabled")
class BaseWebActivity : Activity() {
    private var isSupportZoom = false
    private var titleGravity = -1
    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.transparencyBar(this)
        setContentView(R.layout.activity_web)
        webView = findViewById(R.id.webview)
    }

//    fun setToolBarBackgroundColor(colorRes: Int) {
//        findViewById<Toolbar>(R.id.toolbar)?.setBackgroundColor(colorRes)
//    }

    @SuppressLint("JavascriptInterface")
    private val runnable = Runnable {
        // 加载需要显示的网页
        webView?.setDownloadListener { url, _, _, _, _ ->
            try {
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } catch (e: NullPointerException) {
            }
        }

        webView?.removeJavascriptInterface("searchBoxJavaBridge_")

//        //根据服务端配置设置是否开启硬件加速
//        val layerType = if(ConfigHolder.config?.hardSpeedSwitch == true){
//            //开启
//            View.LAYER_TYPE_HARDWARE
//        } else{
//            //关闭
//            View.LAYER_TYPE_SOFTWARE
//        }
//        webView?.setLayerType(layerType, null)
        webView?.isVerticalScrollBarEnabled = false
        webView?.isHorizontalScrollBarEnabled = false
        webView?.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                title?.let {
                    setTitle(it)
                }
            }

            override fun getDefaultVideoPoster(): Bitmap? {
                return if (super.getDefaultVideoPoster() == null) {
                    val launcherDrawable =
                        resources.getIdentifier("ic_launcher", "mipmap", packageName)
                    BitmapFactory.decodeResource(resources, launcherDrawable)
                } else {
                    super.getDefaultVideoPoster()
                }
            }
        }

        webView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                if (url.isNullOrEmpty()) {
                    return super.shouldOverrideUrlLoading(view, url)
                }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                // progressBar.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String?) {
                if (!webView?.settings?.loadsImagesAutomatically!!) {
                    webView?.settings?.loadsImagesAutomatically = true
                }
                // progressBar.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
            }
        }
        val ws = webView?.settings
        if (ws != null) {
            ws.javaScriptEnabled = true
            ws.cacheMode = WebSettings.LOAD_NO_CACHE
            ws.useWideViewPort = true
            ws.loadWithOverviewMode = true
            ws.blockNetworkImage = false
            ws.defaultFontSize = 18
            ws.minimumFontSize = 18
            ws.defaultFixedFontSize = 18
            ws.textZoom = 100
            ws.domStorageEnabled = true
            ws.javaScriptCanOpenWindowsAutomatically = true
            ws.builtInZoomControls = isSupportZoom
            ws.setSupportZoom(isSupportZoom)
            ws.displayZoomControls = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ws.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            val intent = intent

            val url = intent.getStringExtra(URL)
            if (!url.isNullOrEmpty()) {
                webView?.loadUrl(url)
            }
        }
    }

    fun callJsOnBackPress() {
//        binding.webView?.evaluateJavascript("javascript:onBackPressed()") {
//            Logger.d(TAG,"onBackPressed it:${it}")
//            if(it?.contains("pageSuccess") == true){
//                Logger.d(TAG,"onBackPressed pageSuccess")
//            } else {
//                Logger.d(TAG,"onBackPressed call finished")
//                finish()
//            }
//        }
    }


    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val handler = Handler()
        handler.postDelayed(runnable, 500)
        return super.onCreateView(name, context, attrs)
    }



    override fun onResume() {
        super.onResume()

    }

    override fun onBackPressed() {
        callJsOnBackPress()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val TAG = "WebActivity"
        const val URL = "url"
        const val SUPPORT_ZOOM = "isSupport"
        private const val TITLE_GRAVITY = "titleGravity"
        private const val REPAY_LOAN_BEAN = "REPAY_LOAN_BEAN"

        fun startWebActivity(context: Context, url: String, isSupportZoom: Boolean = false) {
            val intent = Intent(context, BaseWebActivity::class.java)
            intent.putExtra(URL, url)
            intent.putExtra(SUPPORT_ZOOM, isSupportZoom)
            context.startActivity(intent)
        }
    }
}