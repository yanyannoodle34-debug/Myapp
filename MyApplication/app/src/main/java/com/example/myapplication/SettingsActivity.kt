package com.example.myapplication

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.utils.ApiConstants
import com.example.myapplication.utils.PrefsManager
import com.example.myapplication.viewmodels.DashboardViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var containerKeys: LinearLayout
    private lateinit var etGithubToken: TextInputEditText
    private val keyFields = mutableMapOf<String, TextInputEditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        containerKeys = findViewById(R.id.containerKeys)
        etGithubToken = findViewById(R.id.etGithubToken)

        buildKeyFields()
        loadSavedKeys()

        findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.switchAutoScan).apply {
            isChecked = PrefsManager.isAutoScan(this@SettingsActivity)
            setOnCheckedChangeListener { _, checked ->
                PrefsManager.setAutoScan(this@SettingsActivity, checked)
                Toast.makeText(
                    this@SettingsActivity,
                    if (checked) "Auto-scan ON" else "Auto-scan OFF",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        findViewById<MaterialButton>(R.id.btnSave).setOnClickListener { saveAll() }
        findViewById<MaterialButton>(R.id.btnCheckLimit).setOnClickListener { checkLimit() }
        findViewById<MaterialButton>(R.id.btnBack).setOnClickListener { finish() }

        viewModel.rateLimit.observe(this) { rate ->
            val tvLimit = findViewById<android.widget.TextView>(R.id.tvRateLimit)
            tvLimit.text = rate?.let {
                "Limit: ${it.limit} | Used: ${it.used} | Left: ${it.remaining}"
            } ?: "Could not fetch limit — check token"
        }
    }

    private fun buildKeyFields() {
        ApiConstants.GPT_PROVIDERS.forEach { provider ->
            val til = TextInputLayout(this, null).apply {
                hint = "${provider.name} key" + if (!provider.apiKeyRequired) " (not needed)" else ""
                boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { topMargin = 16 }
            }
            val et = TextInputEditText(til.context).apply {
                inputType = android.text.InputType.TYPE_CLASS_TEXT or
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                isEnabled = provider.apiKeyRequired
            }
            til.addView(et)
            containerKeys.addView(til)
            keyFields[provider.id] = et
        }
    }

    private fun loadSavedKeys() {
        keyFields.forEach { (id, field) ->
            field.setText(PrefsManager.getGptKey(this, id))
        }
        etGithubToken.setText(PrefsManager.getGithubToken(this))
    }

    private fun saveAll() {
        keyFields.forEach { (id, field) ->
            PrefsManager.setGptKey(this, id, field.text.toString().trim())
        }
        PrefsManager.setGithubToken(this, etGithubToken.text.toString().trim())
        Toast.makeText(this, "Keys saved", Toast.LENGTH_SHORT).show()
    }

    private fun checkLimit() {
        val token = etGithubToken.text.toString().trim()
        viewModel.checkRateLimit(token)
        Toast.makeText(this, "Checking rate limit...", Toast.LENGTH_SHORT).show()
    }
}
