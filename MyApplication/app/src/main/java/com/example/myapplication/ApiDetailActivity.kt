package com.example.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.models.ApiItem
import com.example.myapplication.utils.PrefsManager
import com.example.myapplication.viewmodels.DashboardViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ApiDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var tvApiName: TextView
    private lateinit var tvApiDescription: TextView
    private lateinit var tvApiUrl: TextView
    private lateinit var tvApiCategory: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvResponseTime: TextView
    private lateinit var tvResponse: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnTest: Button

    private var apiItem: ApiItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_detail)

        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        initViews()
        loadApiData()
        setupObservers()
    }

    private fun initViews() {
        tvApiName = findViewById(R.id.tvApiName)
        tvApiDescription = findViewById(R.id.tvApiDescription)
        tvApiUrl = findViewById(R.id.tvApiUrl)
        tvApiCategory = findViewById(R.id.tvApiCategory)
        tvStatus = findViewById(R.id.tvStatus)
        tvResponseTime = findViewById(R.id.tvResponseTime)
        tvResponse = findViewById(R.id.tvResponse)
        progressBar = findViewById(R.id.progressBar)
        btnTest = findViewById(R.id.btnTest)

        btnTest.setOnClickListener {
            apiItem?.let { api ->
                viewModel.testApi(api)
                progressBar.visibility = android.view.View.VISIBLE
                tvStatus.text = "Testing..."
            }
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCopy).setOnClickListener {
            copyResponse()
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnShare).setOnClickListener {
            shareResponse()
        }
    }

    private fun copyResponse() {
        val text = tvResponse.text.toString()
        if (text.isBlank()) {
            Toast.makeText(this, "Nothing to copy", Toast.LENGTH_SHORT).show()
            return
        }
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("api_response", text))
        Toast.makeText(this, "Response copied", Toast.LENGTH_SHORT).show()
    }

    private fun shareResponse() {
        val text = tvResponse.text.toString()
        if (text.isBlank()) {
            Toast.makeText(this, "Nothing to share", Toast.LENGTH_SHORT).show()
            return
        }
        val title = apiItem?.let { "${it.name} response" } ?: "API response"
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "$title\n\n$text")
        }
        startActivity(Intent.createChooser(send, "Share response"))
    }

    private fun loadApiData() {
        val apiId = intent.getStringExtra("api_id")
        apiId?.let { id ->
            // Search built-in first, then custom APIs (so detail works for both)
            apiItem = com.example.myapplication.utils.ApiConstants.PUBLIC_APIS.find { it.id == id }
                ?: PrefsManager.getCustomApis(this).find { it.id == id }
            apiItem?.let { api ->
                tvApiName.text = "${api.icon} ${api.name}"
                tvApiDescription.text = api.description
                tvApiUrl.text = api.baseUrl
                tvApiCategory.text = if (api.isCustom) "✨ ${api.category}" else api.category
            }
        }
    }

    private fun setupObservers() {
        viewModel.testResult.observe(this) { result ->
            progressBar.visibility = android.view.View.GONE
            
            if (result.success) {
                tvStatus.text = "Status: ${result.statusCode}"
                tvStatus.setTextColor(getColor(R.color.status_live))
                tvResponseTime.text = "Response Time: ${result.responseTime}ms"
                tvResponse.text = result.body ?: "No response body"
            } else {
                tvStatus.text = "Error: ${result.error}"
                tvStatus.setTextColor(getColor(R.color.status_down))
                tvResponseTime.text = ""
                tvResponse.text = "Failed to connect"
            }
        }
    }
}
