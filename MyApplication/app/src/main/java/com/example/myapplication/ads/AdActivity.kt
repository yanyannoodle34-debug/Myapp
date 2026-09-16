package com.example.myapplication.ads

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.myapplication.DashboardActivity
import com.example.myapplication.R

class AdActivity : AppCompatActivity() {

    companion object {
        const val AD_URL = "https://www.profitableratecpmnetwork.com/eyjjtp4aj?key=68ea55cc29c5c85c681c4ac949fb45e0"
        const val SPLASH_DURATION = 5000L
    }

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvCountdown: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnSkip: Button
    private lateinit var btnRefresh: Button
    private var countDownTimer: CountDownTimer? = null
    private var adLoaded = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)

        initViews()
        setupWebView()
        loadAd()
        startCountdown()

        onBackPressedDispatcher.addCallback(this, object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    goToDashboard()
                }
            }
        })
    }

    private fun initViews() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        tvCountdown = findViewById(R.id.tvCountdown)
        tvStatus = findViewById(R.id.tvStatus)
        btnSkip = findViewById(R.id.btnSkip)
        btnRefresh = findViewById(R.id.btnRefresh)

        btnSkip.setOnClickListener {
            goToDashboard()
        }

        btnRefresh.setOnClickListener {
            loadAd()
            tvStatus.text = "Loading ad..."
            btnRefresh.visibility = View.GONE
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
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            userAgentString = "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return true
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    return false
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                adLoaded = true
                progressBar.visibility = View.GONE
                tvStatus.text = "Ad loaded"
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                progressBar.visibility = View.GONE
                tvStatus.text = "Failed to load ad"
                btnRefresh.visibility = View.VISIBLE
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
                if (newProgress >= 100) {
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun loadAd() {
        try {
            webView.loadUrl(AD_URL)
            tvStatus.text = "Loading ad..."
        } catch (e: Exception) {
            tvStatus.text = "Error loading ad"
            btnRefresh.visibility = View.VISIBLE
        }
    }

    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(SPLASH_DURATION, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                tvCountdown.text = "Skip in ${secondsLeft}s"
                btnSkip.isEnabled = false
                btnSkip.alpha = 0.5f
            }

            override fun onFinish() {
                tvCountdown.text = "Ready!"
                btnSkip.isEnabled = true
                btnSkip.alpha = 1.0f
                goToDashboard()
            }
        }.start()
    }

    private fun goToDashboard() {
        countDownTimer?.cancel()
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        webView.stopLoading()
        webView.destroy()
        super.onDestroy()
    }
}
