package com.example.loginapp.models

data class ProductInfo(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)