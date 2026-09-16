package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.ApiItem

class ApiAdapter(
    private val onTestClick: (ApiItem) -> Unit,
    private val onItemClick: (ApiItem) -> Unit
) : ListAdapter<ApiItem, ApiAdapter.ApiViewHolder>(ApiDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_api, parent, false)
        return ApiViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApiViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ApiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvIcon: TextView = itemView.findViewById(R.id.tvIcon)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val tvResponseTime: TextView = itemView.findViewById(R.id.tvResponseTime)
        private val btnTest: ImageButton = itemView.findViewById(R.id.btnTest)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun bind(api: ApiItem) {
            tvIcon.text = api.icon
            tvName.text = api.name
            tvDescription.text = api.description
            tvCategory.text = api.category
            
            when {
                api.isLive -> {
                    tvStatus.text = "● LIVE"
                    tvStatus.setTextColor(itemView.context.getColor(R.color.status_live))
                    tvResponseTime.text = "${api.responseTime}ms"
                    tvResponseTime.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                api.lastChecked > 0 -> {
                    tvStatus.text = "● DOWN"
                    tvStatus.setTextColor(itemView.context.getColor(R.color.status_down))
                    tvResponseTime.text = "Failed"
                    tvResponseTime.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                else -> {
                    tvStatus.text = "○ UNCHECKED"
                    tvStatus.setTextColor(itemView.context.getColor(R.color.status_unchecked))
                    tvResponseTime.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
            }

            btnTest.setOnClickListener {
                progressBar.visibility = View.VISIBLE
                tvStatus.text = "● TESTING..."
                tvStatus.setTextColor(itemView.context.getColor(R.color.status_testing))
                onTestClick(api)
            }

            itemView.setOnClickListener {
                onItemClick(api)
            }
        }
    }

    class ApiDiffCallback : DiffUtil.ItemCallback<ApiItem>() {
        override fun areItemsTheSame(oldItem: ApiItem, newItem: ApiItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ApiItem, newItem: ApiItem): Boolean {
            return oldItem == newItem
        }
    }
}
