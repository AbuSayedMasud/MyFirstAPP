package com.example.loginapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.loginapp.Api.ApiService
import com.example.loginapp.Api.RetrofitHelper
import com.example.loginapp.models.Product
import com.example.loginapp.repository.ProductRepository
import com.example.loginapp.viewmodel.ProductViewModel
import com.example.loginapp.viewmodel.ProductViewModelFactory

class Add : AppCompatActivity() {
    private lateinit var textViewPostedData: TextView
    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        textViewPostedData = findViewById(R.id.textViewPostedData)

        val apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
        val repository = ProductRepository(apiService)
        viewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(repository)
        ).get(ProductViewModel::class.java)

        val buttonCreateProduct = findViewById<Button>(R.id.buttonCreateProduct)
        buttonCreateProduct.setOnClickListener {
            val brand = findViewById<EditText>(R.id.editTextBrand).text.toString()
            val category = findViewById<EditText>(R.id.editTextCategory).text.toString()
            val description = findViewById<EditText>(R.id.editTextDescription).text.toString()
            val discountPercentage =
                findViewById<EditText>(R.id.editTextDiscountPercentage).text.toString().toDouble()
            val price = findViewById<EditText>(R.id.editTextPrice).text.toString().toInt()
            val rating = findViewById<EditText>(R.id.editTextRating).text.toString().toDouble()
            val stock = findViewById<EditText>(R.id.editTextStock).text.toString().toInt()
            val thumbnail = findViewById<EditText>(R.id.editTextThumbnail).text.toString()
            val title = findViewById<EditText>(R.id.editTextTitle).text.toString()

            val product = Product(
                brand,
                category,
                description,
                discountPercentage,
                101,
                emptyList(),
                price,
                rating,
                stock,
                thumbnail,
                title
            )

            viewModel.createProduct(product, object : ProductCallback {
                override fun onSuccess() {
                    showPostedData(product)
                    showToast("Product created successfully!")
                }

                override fun onFailure(error: String) {
                    showToast("Failed to create product: $error")
                }
            })
        }

        viewModel.toastMessage.observe(this, Observer { message ->
            showToast(message)
        })
    }
    private fun showPostedData(product: Product) {
        val postedData = "Posted Data:\n" +
                "id:${product.id}\n"+
                "Brand: ${product.brand}\n" +
                "Category: ${product.category}\n" +
                "Description: ${product.description}\n"
        // Add other fields as needed

        textViewPostedData.text = postedData
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}