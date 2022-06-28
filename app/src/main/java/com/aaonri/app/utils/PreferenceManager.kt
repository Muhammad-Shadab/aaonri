package com.aaonri.app.utils

import android.content.Context
import android.preference.PreferenceManager

class PreferenceManager<T>(private val context: Context) {
    /**
     * Set any type of shared preferences value
     * @param key of the shared preferences which the caller is desire to set
     * @param value types:
     * Boolean ,
     * StringSet,
     * String,
     * Float,
     * Long,
     * Int
     */
    operator fun set(key: String?, value: T) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sp.edit()
        if (value is Boolean) {
            editor.putBoolean(key, (value as Boolean))
        } else if (value is Set<*>) {
            editor.putStringSet(key, value as Set<String?>)
        } else if (value is String) {
            editor.putString(key, value as String)
        } else if (value is Float) {
            editor.putFloat(key, (value as Float))
        } else if (value is Long) {
            editor.putLong(key, (value as Long))
        } else if (value is Int) {
            editor.putInt(key, (value as Int))
        }
        editor.apply()
    }

    /**
     * Get any type of value from the shared preference
     * @param key the key of the shared preference
     * @param defaultValue types:
     * Boolean ,
     * StringSet,
     * String,
     * Float,
     * Long,
     * Int
     * @return same type of the default value which has been passed in
     */
    operator fun get(key: String?, defaultValue: T): T? {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        if (defaultValue is Boolean) {
            val ret = sp.getBoolean(key, (defaultValue as Boolean))
            return ret as T
        } else if (defaultValue is Collection<*>) {
            val result = sp.getStringSet(key, HashSet())
            return result as T?
        } else if (defaultValue is String) {
            val ret = sp.getString(key, defaultValue as String)
            return ret as T?
        } else if (defaultValue is Float) {
            val result = sp.getFloat(key, (defaultValue as Float))
            return result as T
        } else if (defaultValue is Long) {
            val result = sp.getLong(key, (defaultValue as Long))
            return result as T
        } else if (defaultValue is Int) {
            val result = sp.getInt(key, (defaultValue as Int))
            return result as T
        }
        return null
    }
}