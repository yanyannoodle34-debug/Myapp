package com.example.myapplication.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.ApiItem
import com.example.myapplication.models.ApiResponse
import com.example.myapplication.models.GithubRate
import com.example.myapplication.models.GithubRepo
import com.example.myapplication.models.GptProvider
import com.example.myapplication.services.GithubClient
import com.example.myapplication.services.RetrofitClient
import com.example.myapplication.utils.ApiConstants
import com.example.myapplication.utils.PrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel : ViewModel() {

    private val _apis = MutableLiveData<List<ApiItem>>()
    val apis: LiveData<List<ApiItem>> = _apis

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _testResult = MutableLiveData<ApiResponse>()
    val testResult: LiveData<ApiResponse> = _testResult

    private val _gptProviders = MutableLiveData<List<GptProvider>>()
    val gptProviders: LiveData<List<GptProvider>> = _gptProviders

    private val _selectedProvider = MutableLiveData<GptProvider?>()
    val selectedProvider: LiveData<GptProvider?> = _selectedProvider

    private val _gptPrompt = MutableLiveData<String>()
    val gptPrompt: LiveData<String> = _gptPrompt

    private val _gptResponse = MutableLiveData<String>()
    val gptResponse: LiveData<String> = _gptResponse

    private val _githubRepos = MutableLiveData<List<GithubRepo>>()
    val githubRepos: LiveData<List<GithubRepo>> = _githubRepos

    private val _rateLimit = MutableLiveData<GithubRate?>()
    val rateLimit: LiveData<GithubRate?> = _rateLimit

    private val _isSearchingGithub = MutableLiveData<Boolean>()
    val isSearchingGithub: LiveData<Boolean> = _isSearchingGithub

    private var allApis: List<ApiItem> = emptyList()
    private var showHidden = false

    init {
        loadGptProviders()
    }

    /** Load built-in + custom APIs, apply hidden flags. Call from Activity with context. */
    fun loadApis(context: Context) {
        val hidden = PrefsManager.getHiddenIds(context.applicationContext)
        val customs = PrefsManager.getCustomApis(context.applicationContext)
            .map { it.copy(isCustom = true, isHidden = hidden.contains(it.id)) }
        val builtins = ApiConstants.PUBLIC_APIS
            .map { it.copy(isHidden = hidden.contains(it.id)) }
        allApis = customs + builtins
        refreshVisible()
    }

    private fun refreshVisible() {
        _apis.value = if (showHidden) allApis else allApis.filter { !it.isHidden }
    }

    fun setShowHidden(show: Boolean) {
        showHidden = show
        refreshVisible()
    }

    fun isShowingHidden(): Boolean = showHidden

    fun hiddenCount(): Int = allApis.count { it.isHidden }

    fun totalCount(): Int = allApis.size

    /** Secure: validate + persist a user-supplied API. Returns error message or null on success. */
    fun addCustomApi(
        context: Context,
        name: String,
        url: String,
        category: String,
        description: String
    ): String? {
        val n = name.trim().take(60)
        var u = url.trim().take(500)
        if (n.isEmpty()) return "Give your API a name ❤"
        if (u.isEmpty()) return "Paste an API URL ❤"
        if (!u.startsWith("http://") && !u.startsWith("https://")) {
            u = "https://$u"
        }
        if (!(u.startsWith("http://") || u.startsWith("https://"))) {
            return "URL must start with http:// or https://"
        }
        if (u.length < 12) return "That URL looks too short"
        val c = category.trim().take(30).ifEmpty { "Custom" }
        val d = description.trim().take(300).ifEmpty { "My custom API ❤" }
        val ctx = context.applicationContext
        val api = ApiItem(
            id = "custom_${System.currentTimeMillis()}",
            name = n,
            description = d,
            baseUrl = u,
            category = c,
            icon = "✨",
            lastChecked = 0,
            tags = listOf("custom", c.lowercase()),
            isCustom = true,
            isHidden = false
        )
        PrefsManager.addCustomApi(ctx, api)
        PrefsManager.setHidden(ctx, api.id, false)
        loadApis(ctx)
        return null
    }

    /** Toggle show/hide for ANY item (built-in or custom). */
    fun toggleVisibility(context: Context, api: ApiItem) {
        val ctx = context.applicationContext
        PrefsManager.setHidden(ctx, api.id, !api.isHidden)
        allApis = allApis.map {
            if (it.id == api.id) it.copy(isHidden = !api.isHidden) else it
        }
        refreshVisible()
    }

    /** Delete custom APIs. Built-ins cannot be deleted (hide them instead). Returns true if deleted. */
    fun deleteApi(context: Context, api: ApiItem): Boolean {
        if (!api.isCustom) return false
        val ctx = context.applicationContext
        val ok = PrefsManager.removeCustomApi(ctx, api.id)
        PrefsManager.setHidden(ctx, api.id, false)
        loadApis(ctx)
        return ok
    }

    private fun loadGptProviders() {
        _gptProviders.value = ApiConstants.GPT_PROVIDERS
    }

    fun testApi(api: ApiItem) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.testApi(api.baseUrl)
                }
                val responseTime = System.currentTimeMillis() - startTime

                val apiResponse = if (response.isSuccessful) {
                    ApiResponse(
                        success = true,
                        statusCode = response.code(),
                        responseTime = responseTime,
                        body = response.body()?.toString()?.take(500) ?: "Empty response"
                    )
                } else {
                    ApiResponse(
                        success = false,
                        statusCode = response.code(),
                        responseTime = responseTime,
                        body = null,
                        error = "HTTP ${response.code()}"
                    )
                }

                _testResult.value = apiResponse
                updateApiStatus(api, apiResponse.success, responseTime)
            } catch (e: Exception) {
                _testResult.value = ApiResponse(
                    success = false,
                    statusCode = 0,
                    responseTime = 0,
                    body = null,
                    error = e.message ?: "Unknown error"
                )
                updateApiStatus(api, false, 0)
            }
            _isLoading.value = false
        }
    }

    private fun updateApiStatus(api: ApiItem, isLive: Boolean, responseTime: Long) {
        allApis = allApis.map {
            if (it.id == api.id) it.copy(
                isLive = isLive,
                responseTime = responseTime,
                lastChecked = System.currentTimeMillis()
            ) else it
        }
        refreshVisible()
    }

    fun selectGptProvider(provider: GptProvider) {
        _selectedProvider.value = provider
    }

    fun generateTestPrompt(api: ApiItem) {
        val prompt = """
            Generate a curl command or HTTP request to test the following API:
            
            API Name: ${api.name}
            Base URL: ${api.baseUrl}
            Description: ${api.description}
            
            Please provide:
            1. A simple GET request example
            2. Expected response format
            3. Any required parameters
        """.trimIndent()
        _gptPrompt.value = prompt
    }

    fun sendGptRequest(prompt: String, apiKey: String) {
        val provider = _selectedProvider.value ?: return
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val request = com.example.myapplication.models.GptRequest(
                    model = provider.model,
                    messages = listOf(
                        com.example.myapplication.models.GptMessage(role = "user", content = prompt)
                    )
                )
                
                val authHeader = if (provider.id == "google") {
                    "Bearer $apiKey"
                } else {
                    "Bearer $apiKey"
                }

                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.sendGptRequest(
                        url = "${provider.baseUrl}/chat/completions",
                        auth = authHeader,
                        request = request
                    )
                }

                if (response.isSuccessful) {
                    val gptResponse = response.body()
                    val content = gptResponse?.choices?.firstOrNull()?.message?.content
                    _gptResponse.value = content ?: "No response"
                } else {
                    _gptResponse.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _gptResponse.value = "Error: ${e.message}"
            }
            _isLoading.value = false
        }
    }

    fun searchGithub(query: String, token: String) {
        if (query.isBlank()) return
        _isSearchingGithub.value = true
        viewModelScope.launch {
            try {
                val auth = PrefsManager.authHeader(token)
                val response = withContext(Dispatchers.IO) {
                    GithubClient.githubService.searchRepos(
                        query = "$query in:name,description,topics",
                        auth = auth
                    )
                }
                _githubRepos.value = if (response.isSuccessful) {
                    response.body()?.items ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                _githubRepos.value = emptyList()
            }
            _isSearchingGithub.value = false
        }
    }

    fun checkRateLimit(token: String) {
        viewModelScope.launch {
            try {
                val auth = PrefsManager.authHeader(token)
                val response = withContext(Dispatchers.IO) {
                    GithubClient.githubService.getRateLimit(auth = auth)
                }
                _rateLimit.value = if (response.isSuccessful) {
                    response.body()?.resources?.core
                        ?: response.body()?.rate
                } else {
                    null
                }
            } catch (e: Exception) {
                _rateLimit.value = null
            }
        }
    }

    fun clearGithubResults() {
        _githubRepos.value = emptyList()
    }
}
