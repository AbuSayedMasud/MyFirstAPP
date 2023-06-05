package com.example.loginapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.loginapp.Api.ApiService
import com.example.loginapp.models.Categories
import com.example.loginapp.models.Product
import com.example.loginapp.models.ProductInfo
import com.example.loginapp.models.ProductResponse
import retrofit2.Response

class ProductRepository(private val apiService: ApiService) {
    private val _categoryLiveData = MutableLiveData<Categories>()
    val categoryLiveData: LiveData<Categories> get() = _categoryLiveData

    private val _productsLiveData = MutableLiveData<ProductInfo>()
    val productsLiveData: LiveData<ProductInfo> get() = _productsLiveData

    suspend fun getCategories() {
        val result = apiService.getCategories()
        result?.body().let {
            _categoryLiveData.postValue(result.body())
        }
    }

    suspend fun getProducts() {
        val result = apiService.getProducts()
        result?.body().let {
            _productsLiveData.postValue(result.body())
        }
    }
    suspend fun createProduct(product:Product): Response<ProductResponse> {
        return apiService.createProduct(product)
    }
}
