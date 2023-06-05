package com.example.loginapp

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
    fun clearUserSession() {
        TODO("Not yet implemented")
    }
}