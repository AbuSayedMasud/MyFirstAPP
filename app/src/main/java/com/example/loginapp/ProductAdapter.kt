package com.example.loginapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.databinding.ProductsListBinding
import com.example.loginapp.models.Product
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ProductAdapter(private var productData: List<Product>,private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }
    inner class ProductViewHolder(val binding:ProductsListBinding ) :RecyclerView.ViewHolder(binding.root) {
//        fun bind(product: Product) {
//            binding.productTitle.text = product.title
//            binding.productDescription.text = product.description
//            binding.productPrice.text = product.price.toString()
//            Glide.with(itemView)
//                .load(product?.thumbnail)
//                .into(binding.productImage)
//            binding.root.setOnClickListener {
//                Toast.makeText(
//                    binding.root.context,
//                    "Item clicked: ${product.title}",
//                    Toast.LENGTH_SHORT
//                ).show()
//                itemClickListener.onItemClick(product)
//            }
//
//        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductsListBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productData[position]
//        holder.bind(product)
        holder.binding.price.text=product.price.toString()
        holder.binding.productDescription.text=product.description
        holder.binding.productTitle.text=product.title
        Glide.with(holder.itemView)
              .load(product?.thumbnail)
              .into(holder.binding.productImage)
        holder.binding.root.setOnClickListener {
            Toast.makeText(
                holder.binding.root.context,
                "Item clicked: ${product.title}",
                Toast.LENGTH_SHORT
            ).show()
            itemClickListener.onItemClick(product)
        }

    }

    override fun getItemCount(): Int {
        return productData.size
    }


}