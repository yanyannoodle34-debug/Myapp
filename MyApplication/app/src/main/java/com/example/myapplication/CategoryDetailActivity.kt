package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.ApiAdapter
import com.example.myapplication.models.ApiItem
import com.example.myapplication.utils.PrefsManager

class CategoryDetailActivity : AppCompatActivity() {

    private lateinit var tvCategoryName: TextView
    private lateinit var tvCategoryCount: TextView
    private lateinit var rvApis: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: ApiAdapter
    private var categoryApis: List<ApiItem> = emptyList()

    companion object {
        const val EXTRA_CATEGORY = "category_name"

        fun start(context: Context, category: String) {
            val intent = Intent(context, CategoryDetailActivity::class.java).apply {
                putExtra(EXTRA_CATEGORY, category)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_detail)

        tvCategoryName = findViewById(R.id.tvCategoryName)
        tvCategoryCount = findViewById(R.id.tvCategoryCount)
        rvApis = findViewById(R.id.rvApis)
        progressBar = findViewById(R.id.progressBar)

        val category = intent.getStringExtra(EXTRA_CATEGORY) ?: "All"
        tvCategoryName.text = category

        val icon = PrefsManager.getCategoryIcon(category)
        tvCategoryName.text = "$icon $category"

        setupToolbar(category)
        setupAdapter(category)
        loadCategoryApis(category)
    }

    private fun setupToolbar(category: String) {
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        toolbar.title = "$category APIs"
        toolbar.setTitleTextColor(getColor(R.color.text_primary))
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupAdapter(category: String) {
        adapter = ApiAdapter(
            onTestClick = { api ->
                // Open detail to test
                val intent = Intent(this, ApiDetailActivity::class.java).apply {
                    putExtra("api_id", api.id)
                }
                startActivity(intent)
            },
            onItemClick = { api ->
                val intent = Intent(this, ApiDetailActivity::class.java).apply {
                    putExtra("api_id", api.id)
                }
                startActivity(intent)
            },
            onToggleVisibility = { api ->
                val hidden = PrefsManager.isHidden(this, api.id)
                PrefsManager.setHidden(this, api.id, !hidden)
                loadCategoryApis(category)
                val verb = if (hidden) "shown" else "hidden"
                Toast.makeText(this, "${api.name} $verb ❤", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { api ->
                if (api.isCustom) {
                    AlertDialog.Builder(this)
                        .setTitle("Delete ${api.name}?")
                        .setMessage("Remove your custom API ❤")
                        .setPositiveButton("Delete") { _, _ ->
                            PrefsManager.removeCustomApi(this, api.id)
                            loadCategoryApis(category)
                            Toast.makeText(this, "${api.name} deleted ❤", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Keep", null)
                        .show()
                }
            }
        )
        rvApis.layoutManager = LinearLayoutManager(this)
        rvApis.adapter = adapter
    }

    private fun loadCategoryApis(category: String) {
        val hidden = PrefsManager.getHiddenIds(this)
        val builtins = com.example.myapplication.utils.ApiConstants.PUBLIC_APIS
            .filter { it.category == category }
            .map { it.copy(isHidden = hidden.contains(it.id)) }
        val customs = PrefsManager.getCustomApis(this)
            .filter { it.category == category }
            .map { it.copy(isCustom = true, isHidden = hidden.contains(it.id)) }
        categoryApis = builtins + customs
        adapter.submitList(categoryApis)
        tvCategoryCount.text = "${categoryApis.size} APIs ❤"
    }
}
