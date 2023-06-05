package com.example.loginapp.Api

import com.example.loginapp.models.Categories
import com.example.loginapp.models.Product
import com.example.loginapp.models.ProductInfo
import com.example.loginapp.models.ProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/products/categories")
    suspend fun getCategories():Response<Categories>
    @GET("/products")
    suspend fun getProducts():Response<ProductInfo>
    @POST("/products/add")
    suspend fun createProduct(@Body product: Product): Response<ProductResponse>

}