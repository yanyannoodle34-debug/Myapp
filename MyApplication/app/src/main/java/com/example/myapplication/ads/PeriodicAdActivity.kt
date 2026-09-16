package com.example.myapplication.ads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
    private lateinit var btnSkip: Button
    private var countDownTimer: CountDownTimer? = null

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
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: String?): Boolean {
                if (request != null && (request.startsWith("http://") || request.startsWith("https://"))) {
                    return false
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
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        webView.destroy()
        super.onDestroy()
    }
}
