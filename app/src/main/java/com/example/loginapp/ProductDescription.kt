package com.example.loginapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.loginapp.databinding.ActivityProductDescriptionBinding

class ProductDescription : AppCompatActivity() {
    private lateinit var  binding:ActivityProductDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val brand= intent.getStringExtra("BRAND")

        val category = intent.getStringExtra("CATEGORY")
        val description = intent.getStringExtra("DESCRIPTION")
        val price = intent.getIntExtra("PRICE",0)
        val title = intent.getStringExtra("TITLE")
        val ivImage = intent.getStringExtra("Image")
        val stock=intent.getStringExtra("Stock")
        val rating = intent.getStringExtra("Rating")
        Log.d("bramnd", rating.toString())
        Glide.with(this)
            .load(ivImage)
            .into(binding.imageView)
        binding.descriptionTextView.text=description.toString()
        binding.categoryTextView.text=category.toString()
        binding.priceTextView.text=price.toString()
        binding.stockTextView.text=stock
        val ratingValue = rating?.toFloat()
        binding.ratingBar.rating = ratingValue ?: 0.0f
        binding.brandTextView.text=brand.toString()


    }
}