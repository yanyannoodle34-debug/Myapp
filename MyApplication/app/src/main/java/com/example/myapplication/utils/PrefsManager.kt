package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object PrefsManager {

    private const val PREFS_NAME = "api_dashboard_prefs"
    private const val KEY_GITHUB_TOKEN = "github_token"

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

    fun authHeader(token: String): String? =
        token.takeIf { it.isNotBlank() }?.let { "Bearer $it" }
}
