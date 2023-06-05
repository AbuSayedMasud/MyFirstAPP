package com.example.loginapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.databinding.CategoryListBinding
import com.example.loginapp.models.Categories

class CategoryAdapter(private var categoryData: Categories,private val categoryClickListener: CategoryAdapter.OnItemClickListener) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(category: String)
    }
    inner class CategoryViewHolder(val binding:CategoryListBinding  ) :RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryListBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = categoryData[position]
        holder.binding.textCategory.text = item
        holder.binding.root.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Item clicked: $item",
                Toast.LENGTH_SHORT
            ).show()
            categoryClickListener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return categoryData.size
    }


}