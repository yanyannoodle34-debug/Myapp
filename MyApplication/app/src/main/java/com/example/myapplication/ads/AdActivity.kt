package com.example.myapplication.ads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.DashboardActivity
import com.example.myapplication.R

class AdActivity : AppCompatActivity() {

    companion object {
        const val AD_URL = "https://www.profitableratecpmnetwork.com/eyjjtp4aj?key=68ea55cc29c5c85c681c4ac949fb45e0"
        const val SPLASH_DURATION = 5000L // 5 seconds
        const val EXTRA_IS_SPLASH = "is_splash"
    }

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvCountdown: TextView
    private lateinit var btnSkip: Button
    private var countDownTimer: CountDownTimer? = null
    private var isSplash = true

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)

        isSplash = intent.getBooleanExtra(EXTRA_IS_SPLASH, true)

        initViews()
        setupWebView()
        loadAd()
        startCountdown()
    }

    private fun initViews() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        tvCountdown = findViewById(R.id.tvCountdown)
        btnSkip = findViewById(R.id.btnSkip)

        btnSkip.setOnClickListener {
            finishAd()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
            allowContentAccess = true
            allowFileAccess = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: String?): Boolean {
                if (request != null) {
                    if (request.startsWith("http://") || request.startsWith("https://")) {
                        return false // Let WebView handle it
                    }
                    // Open other URLs in browser
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(request)))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress < 100) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                }
            }
        }

        webView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> {
                    if (!webView.canScrollVertically(-1) && !webView.canScrollVertically(1)) {
                        webView.parent.requestDisallowInterceptTouchEvent(false)
                    } else {
                        webView.parent.requestDisallowInterceptTouchEvent(true)
                    }
                }
            }
            false
        }
    }

    private fun loadAd() {
        webView.loadUrl(AD_URL)
    }

    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(SPLASH_DURATION, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                tvCountdown.text = "Skip in ${secondsLeft}s"
            }

            override fun onFinish() {
                finishAd()
            }
        }.start()
    }

    private fun finishAd() {
        countDownTimer?.cancel()
        
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            finishAd()
        }
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        webView.destroy()
        super.onDestroy()
    }
}
