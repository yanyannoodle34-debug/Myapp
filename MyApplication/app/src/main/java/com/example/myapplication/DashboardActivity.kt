package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.adapters.ApiAdapter
import com.example.myapplication.models.ApiItem
import com.example.myapplication.models.GptProvider
import com.example.myapplication.viewmodels.DashboardViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class DashboardActivity : AppCompatActivity() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var apiAdapter: ApiAdapter
    private lateinit var rvApis: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBarMain: ProgressBar
    private lateinit var layoutGptPanel: LinearLayout
    private lateinit var chipGroupProviders: ChipGroup
    private lateinit var tilApiKey: TextInputLayout
    private lateinit var etApiKey: TextInputEditText
    private lateinit var btnRunGpt: MaterialButton
    private lateinit var tvGptResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        initViews()
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun initViews() {
        rvApis = findViewById(R.id.rvApis)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        progressBarMain = findViewById(R.id.progressBarMain)
        layoutGptPanel = findViewById(R.id.layoutGptPanel)
        chipGroupProviders = findViewById(R.id.chipGroupProviders)
        tilApiKey = findViewById(R.id.tilApiKey)
        etApiKey = findViewById(R.id.etApiKey)
        btnRunGpt = findViewById(R.id.btnRunGpt)
        tvGptResponse = findViewById(R.id.tvGptResponse)
    }

    private fun setupRecyclerView() {
        apiAdapter = ApiAdapter(
            onTestClick = { api -> viewModel.testApi(api) },
            onItemClick = { api -> showApiDetails(api) }
        )
        
        rvApis.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = apiAdapter
        }

        swipeRefresh.setOnRefreshListener {
            checkAllApis()
        }
    }

    private fun setupClickListeners() {
        findViewById<MaterialButton>(R.id.btnCheckAll).setOnClickListener {
            checkAllApis()
        }

        findViewById<MaterialButton>(R.id.btnGptTest).setOnClickListener {
            toggleGptPanel()
        }

        btnRunGpt.setOnClickListener {
            runGptTest()
        }
    }

    private fun observeViewModel() {
        viewModel.apis.observe(this) { apis ->
            apiAdapter.submitList(apis)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBarMain.visibility = if (isLoading) View.VISIBLE else View.GONE
            swipeRefresh.isRefreshing = false
        }

        viewModel.testResult.observe(this) { result ->
            val message = if (result.success) {
                "API is LIVE! Status: ${result.statusCode}, Time: ${result.responseTime}ms"
            } else {
                "API is DOWN: ${result.error}"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.gptProviders.observe(this) { providers ->
            setupGptChips(providers)
        }

        viewModel.gptPrompt.observe(this) { prompt ->
            Toast.makeText(this, "Prompt generated! Tap 'Run GPT Test'", Toast.LENGTH_SHORT).show()
        }

        viewModel.gptResponse.observe(this) { response ->
            tvGptResponse.text = response
        }
    }

    private fun checkAllApis() {
        viewModel.apis.value?.forEach { api ->
            viewModel.testApi(api)
        }
    }

    private fun toggleGptPanel() {
        if (layoutGptPanel.visibility == View.VISIBLE) {
            layoutGptPanel.visibility = View.GONE
        } else {
            layoutGptPanel.visibility = View.VISIBLE
        }
    }

    private fun setupGptChips(providers: List<GptProvider>) {
        chipGroupProviders.removeAllViews()
        
        providers.forEach { provider ->
            val chip = Chip(this).apply {
                text = provider.name
                isCheckable = true
                isCheckedIconVisible = true
                
                if (provider.isFree) {
                    text = "${provider.name} (Free)"
                }
                
                setOnClickListener {
                    viewModel.selectGptProvider(provider)
                    tilApiKey.visibility = if (provider.apiKeyRequired) View.VISIBLE else View.GONE
                }
            }
            chipGroupProviders.addView(chip)
        }
    }

    private fun runGptTest() {
        val apiKey = etApiKey.text.toString()
        val prompt = viewModel.gptPrompt.value

        if (prompt.isNullOrBlank()) {
            Toast.makeText(this, "Select an API first to generate a prompt", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedProvider = viewModel.selectedProvider.value
        if (selectedProvider == null) {
            Toast.makeText(this, "Select a GPT provider first", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedProvider.apiKeyRequired && apiKey.isBlank()) {
            Toast.makeText(this, "Enter API key for ${selectedProvider.name}", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.sendGptRequest(prompt, apiKey)
    }

    private fun showApiDetails(api: ApiItem) {
        val message = """
            API: ${api.name}
            URL: ${api.baseUrl}
            Category: ${api.category}
            GitHub: ${api.githubRepo}
            
            Description: ${api.description}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("${api.icon} ${api.name}")
            .setMessage(message)
            .setPositiveButton("Test Now") { _, _ ->
                viewModel.testApi(api)
                viewModel.generateTestPrompt(api)
                layoutGptPanel.visibility = View.VISIBLE
            }
            .setPositiveButton("Open Docs") { _, _ ->
                val intent = Intent(this, ApiDetailActivity::class.java)
                intent.putExtra("api_id", api.id)
                startActivity(intent)
            }
            .setNegativeButton("Close", null)
            .show()
    }
}
