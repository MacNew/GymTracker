package com.mac.gymtracker.utils

import android.content.Context
import android.content.SharedPreferences
import com.mac.gymtracker.R

class PrefUtils private constructor(context: Context) {
    var preferences: SharedPreferences = context.getSharedPreferences(
        context.resources.getString(R.string.pref_key),
        Context.MODE_PRIVATE
    )
    var editor: SharedPreferences.Editor = preferences.edit()
    fun setInt(key: String?, value: Int): Boolean {
        return editor.putInt(key, value).commit()
    }

    fun setLong(key: String?, value: Long): Boolean {
        return editor.putLong(key, value).commit()
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun setString(key: String?, value: String?): Boolean {
        return editor.putString(key, value).commit()
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return preferences.getString(key, defaultValue)
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun setBoolean(key: String?, value: Boolean): Boolean {
        return editor.putBoolean(key, value).commit()
    }

    companion object {
        fun INSTANCE(context: Context): PrefUtils {
            return PrefUtils(context)
        }
    }

}