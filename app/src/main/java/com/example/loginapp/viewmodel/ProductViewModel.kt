package com.example.loginapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.ProductCallback
import com.example.loginapp.models.Categories
import com.example.loginapp.models.ProductInfo
import com.example.loginapp.models.ProductResponse
import com.example.loginapp.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductViewModel(val repository: ProductRepository) : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategories()
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProducts()
        }
    }
    fun createProduct(product: com.example.loginapp.models.Product, callback: ProductCallback) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: Response<ProductResponse> = repository.createProduct(product)

                if (response.isSuccessful) {

                    Log.d("response",response.code().toString())
                    withContext(Dispatchers.Main) {
                        callback.onSuccess()
                        _toastMessage.value = "Product created successfully viewModel!"
                    }
                } else {

                    val errorMessage = "Failed to create product: ${response.errorBody()?.string()}"
                    withContext(Dispatchers.Main) {
                        callback.onFailure(errorMessage)
                        _toastMessage.value = errorMessage
                    }
                }
            } catch (e: Exception) {
                val errorMessage = "Failed to create product: ${e.message}"
                withContext(Dispatchers.Main) {
                    callback.onFailure(errorMessage)
                    _toastMessage.value = errorMessage
                }
            }
        }
    }
    val category:LiveData<Categories> get()=repository.categoryLiveData
    val products:LiveData<ProductInfo> get()=repository.productsLiveData
}