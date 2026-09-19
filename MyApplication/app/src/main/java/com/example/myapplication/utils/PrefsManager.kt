package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.myapplication.models.ApiItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PrefsManager {

    private const val PREFS_NAME = "api_dashboard_prefs"
    private const val KEY_GITHUB_TOKEN = "github_token"
    private const val KEY_AUTO_SCAN = "auto_scan"
    private const val KEY_CUSTOM_APIS = "custom_apis_json"
    private const val KEY_HIDDEN_IDS = "hidden_api_ids"
    private val gson = Gson()

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun gptKeyName(providerId: String): String = "gpt_key_$providerId"

    fun getGptKey(context: Context, providerId: String): String =
        prefs(context).getString(gptKeyName(providerId), "") ?: ""

    fun setGptKey(context: Context, providerId: String, key: String) {
        prefs(context).edit { putString(gptKeyName(providerId), key) }
    }

    fun getGithubToken(context: Context): String =
        prefs(context).getString(KEY_GITHUB_TOKEN, "") ?: ""

    fun setGithubToken(context: Context, token: String) {
        prefs(context).edit { putString(KEY_GITHUB_TOKEN, token) }
    }

    fun hasGithubToken(context: Context): Boolean =
        getGithubToken(context).isNotBlank()

    fun isAutoScan(context: Context): Boolean =
        prefs(context).getBoolean(KEY_AUTO_SCAN, true)

    fun setAutoScan(context: Context, enabled: Boolean) {
        prefs(context).edit { putBoolean(KEY_AUTO_SCAN, enabled) }
    }

    fun authHeader(token: String): String? =
        token.takeIf { it.isNotBlank() }?.let { "Bearer $it" }

    // ---- Custom APIs (persisted as JSON) ----
    fun getCustomApis(context: Context): MutableList<ApiItem> {
        val json = prefs(context).getString(KEY_CUSTOM_APIS, null) ?: return mutableListOf()
        return try {
            val type = object : TypeToken<MutableList<ApiItem>>() {}.type
            gson.fromJson<MutableList<ApiItem>>(json, type) ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    private fun saveCustomApis(context: Context, list: List<ApiItem>) {
        prefs(context).edit { putString(KEY_CUSTOM_APIS, gson.toJson(list)) }
    }

    fun addCustomApi(context: Context, api: ApiItem) {
        val list = getCustomApis(context)
        list.removeAll { it.id == api.id }
        list.add(0, api)
        saveCustomApis(context, list)
    }

    /** @return true if a custom API was removed */
    fun removeCustomApi(context: Context, apiId: String): Boolean {
        val list = getCustomApis(context)
        val removed = list.removeAll { it.id == apiId }
        if (removed) saveCustomApis(context, list)
        return removed
    }

    // ---- Hidden (show/hide per item, works for built-in + custom) ----
    fun getHiddenIds(context: Context): MutableSet<String> =
        prefs(context).getStringSet(KEY_HIDDEN_IDS, emptySet())
            ?.toMutableSet() ?: mutableSetOf()

    fun isHidden(context: Context, apiId: String): Boolean =
        getHiddenIds(context).contains(apiId)

    fun setHidden(context: Context, apiId: String, hidden: Boolean) {
        val set = getHiddenIds(context)
        if (hidden) set.add(apiId) else set.remove(apiId)
        prefs(context).edit { putStringSet(KEY_HIDDEN_IDS, set) }
    }
}
