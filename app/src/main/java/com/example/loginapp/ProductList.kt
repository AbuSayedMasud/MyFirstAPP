package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginapp.Api.ApiService
import com.example.loginapp.Api.RetrofitHelper
import com.example.loginapp.databinding.ActivityProductListBinding
import com.example.loginapp.databinding.FragmentProductBinding
import com.example.loginapp.models.Product
import com.example.loginapp.repository.ProductRepository
import com.example.loginapp.viewmodel.ProductViewModel
import com.example.loginapp.viewmodel.ProductViewModelFactory

class ProductList : AppCompatActivity(), ProductAdapter.OnItemClickListener {
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: ActivityProductListBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productViewModel: ProductViewModel
    private var productList: MutableList<Product> = mutableListOf()
    private var filterProduct: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressBar = binding.progressBar
        val category = intent.getStringExtra("Category")
        Log.d("productlist to", category.toString())

        val apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
        val repository = ProductRepository(apiService)
        progressBar.visibility = View.VISIBLE
        productViewModel = ViewModelProvider(this, ProductViewModelFactory(repository)).get(ProductViewModel::class.java)

        productAdapter = ProductAdapter(filterProduct, this)
        binding.recyclerProducts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
        }

        productViewModel.products.observe(this, Observer { products ->
            products?.let {
                productList.addAll(products.products)
                filterProductFunction(category.toString())
            }
        })
    }
    private fun initializeRecyclerView() {
        if (filterProduct.isEmpty()) {
            binding.emptyStateTextView.visibility = View.VISIBLE
            binding.recyclerProducts.visibility = View.GONE
        } else {
            binding.emptyStateTextView.visibility = View.GONE
            binding.recyclerProducts.visibility = View.VISIBLE

            productAdapter = ProductAdapter(filterProduct, this)
            binding.recyclerProducts.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = productAdapter
            }
        }
    }
    private fun filterProductFunction(categoryName: String) {

        for (product in productList) {
            if (product.category == categoryName) {
                filterProduct.add(product)
            }
        }
        initializeRecyclerView()
        progressBar.visibility = View.GONE
        productAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(product: Product) {
        val intent = Intent(this, ProductDescription::class.java).apply {
            putExtra("BRAND", product.brand)
            putExtra("CATEGORY", product.category)
            putExtra("DESCRIPTION", product.description)
            putExtra("PRICE", product.price)
            putExtra("TITLE", product.title)
            putExtra("Image", product.thumbnail)
            putExtra("Stock", product.stock.toString())
            putExtra("Rating", product.rating.toString())
        }
        startActivity(intent)
    }
}
