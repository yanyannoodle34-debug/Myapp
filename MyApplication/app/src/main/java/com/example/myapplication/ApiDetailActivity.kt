package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.models.ApiItem
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
    }

    private fun loadApiData() {
        val apiId = intent.getStringExtra("api_id")
        apiId?.let { id ->
            apiItem = com.example.myapplication.utils.ApiConstants.PUBLIC_APIS.find { it.id == id }
            apiItem?.let { api ->
                tvApiName.text = "${api.icon} ${api.name}"
                tvApiDescription.text = api.description
                tvApiUrl.text = api.baseUrl
                tvApiCategory.text = api.category
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
