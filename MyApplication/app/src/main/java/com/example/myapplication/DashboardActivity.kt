package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.adapters.ApiAdapter
import com.example.myapplication.ads.PeriodicAdActivity
import com.example.myapplication.models.ApiItem
import com.example.myapplication.models.GithubRepo
import com.example.myapplication.models.GptProvider
import com.example.myapplication.utils.PrefsManager
import com.example.myapplication.viewmodels.DashboardViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class DashboardActivity : AppCompatActivity() {

    companion object {
        const val AD_INTERVAL = 5 * 60 * 1000L // 5 minutes in milliseconds
    }

    private lateinit var viewModel: DashboardViewModel
    private lateinit var apiAdapter: ApiAdapter
    private lateinit var rvApis: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBarMain: ProgressBar
    private lateinit var layoutGptPanel: LinearLayout
    private lateinit var chipGroupProviders: ChipGroup
    private lateinit var tilApiKey: TextInputLayout
    private lateinit var etApiKey: TextInputEditText
    private lateinit var etCustomPrompt: TextInputEditText
    private lateinit var btnRunGpt: MaterialButton
    private lateinit var tvGptResponse: TextView
    private lateinit var etSearch: EditText
    private lateinit var btnClearSearch: ImageButton
    private lateinit var tvLiveCount: TextView
    private lateinit var tvDownCount: TextView
    private lateinit var tvTotalCount: TextView
    private lateinit var btnStopCheck: MaterialButton

    private var isGptPanelVisible = false
    private var checkingAll = false
    private var isAdScheduled = false
    private var githubMode = false
    private var lastGithubQuery = ""
    private lateinit var tvRateLimit: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var adRunnable: Runnable? = null

    private val adLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // Periodic ad closed, continue normal operation
    }

    private val checkAdLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // Refresh-check ad closed -> run the checks now
        runChecksNow()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        initViews()
        setupRecyclerView()
        setupClickListeners()
        setupSearch()
        observeViewModel()
        startPeriodicAd()

        // Silent auto-scan on start (no ad) — manual refresh still shows an ad
        if (PrefsManager.isAutoScan(this) && savedInstanceState == null) {
            handler.postDelayed({ runChecksNow() }, 800)
        }
    }

    private fun initViews() {
        rvApis = findViewById(R.id.rvApis)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        progressBarMain = findViewById(R.id.progressBarMain)
        layoutGptPanel = findViewById(R.id.layoutGptPanel)
        chipGroupProviders = findViewById(R.id.chipGroupProviders)
        tilApiKey = findViewById(R.id.tilApiKey)
        etApiKey = findViewById(R.id.etApiKey)
        etCustomPrompt = findViewById(R.id.etCustomPrompt)
        btnRunGpt = findViewById(R.id.btnRunGpt)
        tvGptResponse = findViewById(R.id.tvGptResponse)
        etSearch = findViewById(R.id.etSearch)
        btnClearSearch = findViewById(R.id.btnClearSearch)
        tvLiveCount = findViewById(R.id.tvLiveCount)
        tvDownCount = findViewById(R.id.tvDownCount)
        tvTotalCount = findViewById(R.id.tvTotalCount)
        btnStopCheck = findViewById(R.id.btnStopCheck)
        tvRateLimit = findViewById(R.id.tvRateLimit)
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

        btnStopCheck.setOnClickListener {
            checkingAll = false
            btnStopCheck.visibility = View.GONE
            findViewById<MaterialButton>(R.id.btnCheckAll).visibility = View.VISIBLE
            Toast.makeText(this, "Check stopped", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialButton>(R.id.btnGptPanel).setOnClickListener {
            toggleGptPanel()
        }

        btnRunGpt.setOnClickListener {
            runGptTest()
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCloseGpt).setOnClickListener {
            toggleGptPanel()
        }

        findViewById<MaterialButtonToggleGroup>(R.id.toggleSource).addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            setGithubMode(checkedId == R.id.btnModeGithub)
        }

        findViewById<MaterialButton>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_about -> {
                AlertDialog.Builder(this)
                    .setTitle("AI for APIs (AFA) v1.2")
                    .setMessage("AI in APIs — test public APIs + GitHub repos with GPT help.\n\n35 built-in APIs • 11 AI providers • GitHub search with rate-limit display • Auto-scan on start.")
                    .setPositiveButton("OK", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnClearSearch.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                if (!githubMode) filterApis(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && githubMode) {
                runGithubSearch(etSearch.text.toString())
                true
            } else {
                false
            }
        }

        btnClearSearch.setOnClickListener {
            etSearch.text?.clear()
            btnClearSearch.visibility = View.GONE
            if (githubMode) viewModel.clearGithubResults()
        }
    }

    private fun filterApis(query: String) {
        val allApis = viewModel.apis.value ?: return
        if (query.isEmpty()) {
            apiAdapter.submitList(allApis)
        } else {
            val searchTerms = query.lowercase().split(" ", ",", ";").filter { it.isNotBlank() }
            val filtered = allApis.filter { api ->
                searchTerms.any { term ->
                    api.name.lowercase().contains(term) ||
                    api.description.lowercase().contains(term) ||
                    api.category.lowercase().contains(term) ||
                    api.tags.any { tag -> tag.lowercase().contains(term) } ||
                    api.githubRepo.lowercase().contains(term) ||
                    api.baseUrl.lowercase().contains(term)
                }
            }
            apiAdapter.submitList(filtered)
        }
    }

    private fun observeViewModel() {
        viewModel.apis.observe(this) { apis ->
            apiAdapter.submitList(apis)
            updateStats(apis)
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

        viewModel.gptResponse.observe(this) { response ->
            tvGptResponse.text = response
        }

        viewModel.githubRepos.observe(this) { repos ->
            if (githubMode) {
                apiAdapter.submitList(repos.map { mapRepoToApi(it) })
                tvTotalCount.text = repos.size.toString()
            }
        }

        viewModel.isSearchingGithub.observe(this) { searching ->
            if (githubMode) swipeRefresh.isRefreshing = searching
        }

        viewModel.rateLimit.observe(this) { rate ->
            tvRateLimit.text = rate?.let {
                "GitHub quota: ${it.remaining}/${it.limit} left (used ${it.used})"
            } ?: "GitHub quota: unknown — add token in Settings for 5000/hr"
        }
    }

    private fun setGithubMode(enabled: Boolean) {
        githubMode = enabled
        tvRateLimit.visibility = if (enabled) View.VISIBLE else View.GONE
        etSearch.hint = if (enabled) "Search GitHub repos... (press 🔍)" else "Search APIs..."
        if (enabled) {
            val token = PrefsManager.getGithubToken(this)
            viewModel.checkRateLimit(token)
            val q = etSearch.text.toString()
            if (q.length >= 2) runGithubSearch(q)
        } else {
            filterApis(etSearch.text.toString())
        }
    }

    private fun runGithubSearch(query: String) {
        if (query.length < 2) {
            Toast.makeText(this, "Type at least 2 characters", Toast.LENGTH_SHORT).show()
            return
        }
        lastGithubQuery = query
        viewModel.checkRateLimit(PrefsManager.getGithubToken(this))
        viewModel.searchGithub(query, PrefsManager.getGithubToken(this))
    }

    private fun mapRepoToApi(repo: GithubRepo): ApiItem {
        val lang = repo.language ?: "Code"
        return ApiItem(
            id = "gh_${repo.id}",
            name = repo.fullName.substringAfter("/"),
            description = "${repo.description ?: "No description"}\n⭐ ${repo.stars} • $lang • ${repo.owner?.login ?: ""}",
            baseUrl = repo.htmlUrl,
            category = lang,
            icon = "💻",
            githubRepo = repo.fullName,
            documentation = repo.htmlUrl,
            tags = repo.topics + listOf("github", lang.lowercase(), "repo", "code")
        )
    }

    private fun updateStats(apis: List<ApiItem>) {
        val liveCount = apis.count { it.isLive }
        val downCount = apis.count { !it.isLive && it.lastChecked > 0 }
        val totalCount = apis.size

        tvLiveCount.text = liveCount.toString()
        tvDownCount.text = downCount.toString()
        tvTotalCount.text = totalCount.toString()
    }

    private fun checkAllApis() {
        // Show ad first — checks run automatically when the ad closes
        checkAdLauncher.launch(Intent(this, PeriodicAdActivity::class.java))
    }

    private fun runChecksNow() {
        if (githubMode) {
            if (lastGithubQuery.isNotBlank()) runGithubSearch(lastGithubQuery)
            else swipeRefresh.isRefreshing = false
            return
        }
        checkingAll = true
        btnStopCheck.visibility = View.VISIBLE
        findViewById<MaterialButton>(R.id.btnCheckAll).visibility = View.GONE

        viewModel.apis.value?.forEach { api ->
            if (!checkingAll) return@forEach
            viewModel.testApi(api)
        }

        Toast.makeText(this, "Checking all APIs...", Toast.LENGTH_SHORT).show()
    }

    private fun toggleGptPanel() {
        isGptPanelVisible = !isGptPanelVisible
        layoutGptPanel.visibility = if (isGptPanelVisible) View.VISIBLE else View.GONE
    }

    private fun setupGptChips(providers: List<GptProvider>) {
        chipGroupProviders.removeAllViews()

        providers.forEach { provider ->
            val chip = Chip(this).apply {
                text = if (provider.isFree) "${provider.name} (Free)" else provider.name
                isCheckable = true
                isCheckedIconVisible = true

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
        val customPrompt = etCustomPrompt.text.toString()
        val selectedProvider = viewModel.selectedProvider.value

        if (selectedProvider == null) {
            Toast.makeText(this, "Select a GPT provider first", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedProvider.apiKeyRequired && apiKey.isBlank()) {
            Toast.makeText(this, "Enter API key for ${selectedProvider.name}", Toast.LENGTH_SHORT).show()
            return
        }

        val prompt = if (customPrompt.isNotBlank()) {
            customPrompt
        } else {
            val currentApi = apiAdapter.currentList.firstOrNull { it.isLive }
            if (currentApi != null) {
                """
                    Analyze this API and suggest tests:
                    
                    API: ${currentApi.name}
                    URL: ${currentApi.baseUrl}
                    Description: ${currentApi.description}
                    
                    Please provide:
                    1. Sample GET request
                    2. Expected response format
                    3. Common test cases
                """.trimIndent()
            } else {
                "Suggest 5 public APIs for testing with their endpoints"
            }
        }

        viewModel.sendGptRequest(prompt, apiKey)
    }

    private fun showApiDetails(api: ApiItem) {
        val message = """
            ${api.icon} ${api.name}
            
            Category: ${api.category}
            URL: ${api.baseUrl}
            
            ${api.description}
            
            Status: ${if (api.isLive) "LIVE" else if (api.lastChecked > 0) "DOWN" else "UNCHECKED"}
            ${if (api.responseTime > 0) "Response: ${api.responseTime}ms" else ""}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("API Details")
            .setMessage(message)
            .setPositiveButton("Test API") { _, _ ->
                viewModel.testApi(api)
                viewModel.generateTestPrompt(api)
                if (!isGptPanelVisible) toggleGptPanel()
            }
            .setNeutralButton("Open Docs") { _, _ ->
                val intent = Intent(this, ApiDetailActivity::class.java)
                intent.putExtra("api_id", api.id)
                startActivity(intent)
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun startPeriodicAd() {
        if (isAdScheduled) return
        val runnable = object : Runnable {
            override fun run() {
                showPeriodicAd()
                handler.postDelayed(this, AD_INTERVAL)
            }
        }
        adRunnable = runnable
        handler.postDelayed(runnable, AD_INTERVAL)
        isAdScheduled = true
    }

    private fun showPeriodicAd() {
        val intent = Intent(this, PeriodicAdActivity::class.java)
        adLauncher.launch(intent)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
