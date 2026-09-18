package com.example.myapplication.ads

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class PeriodicAdActivity : AppCompatActivity() {

    companion object {
        const val AD_URL = "https://www.profitableratecpmnetwork.com/eyjjtp4aj?key=68ea55cc29c5c85c681c4ac949fb45e0"
        const val AD_DURATION = 5000L // 5 seconds
    }

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvCountdown: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnSkip: Button
    private lateinit var btnRefresh: Button
    private lateinit var btnBrowser: Button
    private var countDownTimer: CountDownTimer? = null
    private var retryCount = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)

        initViews()
        setupWebView()
        loadAd()
        startCountdown()
    }

    private fun initViews() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        tvCountdown = findViewById(R.id.tvCountdown)
        tvStatus = findViewById(R.id.tvStatus)
        btnSkip = findViewById(R.id.btnSkip)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnBrowser = findViewById(R.id.btnBrowser)

        btnSkip.setOnClickListener {
            finishAd()
        }

        btnRefresh.setOnClickListener {
            retryCount = 0
            btnRefresh.visibility = View.GONE
            btnBrowser.visibility = View.GONE
            tvStatus.text = "Loading ad..."
            loadAd()
        }

        btnBrowser.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AD_URL)))
            } catch (e: Exception) {
                tvStatus.text = "No browser found"
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return true
                return !(url.startsWith("http://") || url.startsWith("https://"))
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                tvStatus.text = "Ad loaded"
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame != true) return
                // VPN/DNS may block the ad host: retry once, then browser fallback
                if (retryCount < 1) {
                    retryCount++
                    tvStatus.text = "Retrying ad... ($retryCount)"
                    view?.clearCache(true)
                    view?.loadUrl(AD_URL)
                } else {
                    progressBar.visibility = View.GONE
                    tvStatus.text = "Ad blocked (VPN?) — try browser"
                    btnRefresh.visibility = View.VISIBLE
                    btnBrowser.visibility = View.VISIBLE
                }
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.visibility = if (newProgress < 100) View.VISIBLE else View.GONE
            }
        }
    }

    private fun loadAd() {
        webView.loadUrl(AD_URL)
    }

    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(AD_DURATION, 1000) {
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
        finish()
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        webView.destroy()
        super.onDestroy()
    }
}
