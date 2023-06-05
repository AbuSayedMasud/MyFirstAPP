package com.example.loginapp

interface ProductCallback {
    fun onSuccess()
    fun onFailure(error: String)
}