package com.example.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.models.ApiItem
import com.example.myapplication.utils.PrefsManager
import com.example.myapplication.viewmodels.DashboardViewModel
import com.google.android.material.button.MaterialButton
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
    private lateinit var btnEdit: MaterialButton

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
        btnEdit = findViewById(R.id.btnEdit)

        btnTest.setOnClickListener {
            apiItem?.let { api ->
                viewModel.testApi(api)
                progressBar.visibility = android.view.View.VISIBLE
                tvStatus.text = "Testing..."
            }
        }

        btnEdit.setOnClickListener {
            apiItem?.let { api ->
                if (api.isCustom) showEditDialog(api)
            }
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCopy).setOnClickListener {
            copyResponse()
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnShare).setOnClickListener {
            shareResponse()
        }
    }

    private fun showEditDialog(api: ApiItem) {
        val nameInput = EditText(this).apply {
            setText(api.name)
            hint = "API Name"
            setPadding(48, 32, 48, 8)
            setTextColor(getColor(R.color.text_primary))
        }
        val urlInput = EditText(this).apply {
            setText(api.baseUrl)
            hint = "Base URL"
            setPadding(48, 32, 48, 8)
            setTextColor(getColor(R.color.text_primary))
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_URI
        }
        val catInput = EditText(this).apply {
            setText(api.category)
            hint = "Category"
            setPadding(48, 32, 48, 8)
            setTextColor(getColor(R.color.text_primary))
        }
        val descInput = EditText(this).apply {
            setText(api.description)
            hint = "Description"
            setPadding(48, 32, 48, 8)
            setTextColor(getColor(R.color.text_primary))
            minLines = 2
        }

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 24, 32, 0)
            addView(nameInput)
            addView(urlInput)
            addView(catInput)
            addView(descInput)
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Custom API")
            .setView(container)
            .setPositiveButton("Save") { _, _ ->
                val name = nameInput.text.toString().trim().take(60)
                var url = urlInput.text.toString().trim().take(500)
                if (url.isNotEmpty() && !url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://$url"
                }
                val cat = catInput.text.toString().trim().take(30).ifEmpty { "Custom" }
                val desc = descInput.text.toString().trim().take(300).ifEmpty { "My custom API" }

                if (name.isEmpty()) {
                    Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (url.isEmpty() || url.length < 12) {
                    Toast.makeText(this, "Valid URL required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val updated = api.copy(
                    name = name,
                    baseUrl = url,
                    category = cat,
                    description = desc,
                    tags = listOf("custom", cat.lowercase())
                )
                PrefsManager.addCustomApi(this, updated)
                apiItem = updated
                loadApiData()
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
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
                btnEdit.visibility = if (api.isCustom) View.VISIBLE else View.GONE
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
