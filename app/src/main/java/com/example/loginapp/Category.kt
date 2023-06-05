package com.example.loginapp
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.loginapp.Api.ApiService
import com.example.loginapp.Api.RetrofitHelper
import com.example.loginapp.databinding.FragmentCategoryBinding
import com.example.loginapp.models.Categories
import com.example.loginapp.repository.ProductRepository
import com.example.loginapp.viewmodel.ProductViewModel
import com.example.loginapp.viewmodel.ProductViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Category : Fragment(),CategoryAdapter.OnItemClickListener {
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryAdapter:CategoryAdapter
    private lateinit var productViewModel: ProductViewModel
    private lateinit var categoryList: Categories
    private var topBarTitleChangeListener: TopBarTitleChangeListener? = null
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(binding.root.context, R.anim.rotate_open)
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(binding.root.context, R.anim.rotate_close)
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(binding.root.context, R.anim.from_bottom)
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(binding.root.context, R.anim.to_bottom)
    }
    private var clicked =false
    private lateinit var add_btn: FloatingActionButton
    private lateinit var atoZ: FloatingActionButton
    private lateinit var Ztoa: FloatingActionButton
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TopBarTitleChangeListener) {
            topBarTitleChangeListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        topBarTitleChangeListener = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topBarTitleChangeListener?.updateTopBarTitle("Shadhin")
        categoryList = Categories()

        val apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
        val repository = ProductRepository(apiService)

        productViewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(repository)
        ).get(ProductViewModel::class.java)

        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        productViewModel.category.observe(viewLifecycleOwner, Observer { categories ->
            categories?.let {
                categoryList = it
                initializeRecyclerView()
                progressBar.visibility = View.GONE
            }
        })
        binding.addBtn.setOnClickListener {
            onAddButtonClick()
        }
    }

    private fun initializeRecyclerView() {
        binding.atoz.setOnClickListener {
            categoryList.sort()
            categoryAdapter.notifyDataSetChanged()
            Log.d("sort", categoryList.toString())
        }
        binding.ztoa.setOnClickListener {
            categoryList.sortDescending()
            categoryAdapter.notifyDataSetChanged()
            Log.d("sort", categoryList.toString())
        }

        categoryAdapter = CategoryAdapter(categoryList,this)
        val recyclerView: androidx.recyclerview.widget.RecyclerView = binding.recyclerView
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.adapter = categoryAdapter

    }

    override fun onItemClick(category: String) {
        var intent=Intent( context,ProductList::class.java).apply {
            Log.d("productlist", category.toString())
            putExtra("Category",category)
        }
        startActivity(intent)
    }
    private fun onAddButtonClick(){
        setVisiblity(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setAnimation(click: Boolean) {
        if(!click){
            binding.atoz.startAnimation(fromBottom)
            binding.ztoa.startAnimation(fromBottom)
            binding.addBtn.startAnimation(rotateOpen)
        }
        else{
            binding.atoz.startAnimation(toBottom)
            binding.ztoa.startAnimation(toBottom)
            binding.addBtn.startAnimation(rotateClose)
        }
    }

    private fun setVisiblity(click: Boolean) {
        if(!click){
            binding.atoz.visibility= View.VISIBLE
            binding.ztoa.visibility= View.VISIBLE
            binding.atoz.startAnimation(rotateOpen)
            binding.ztoa.startAnimation(rotateOpen)
        }
        else
        {
           binding.atoz.visibility=View.INVISIBLE
            binding.ztoa.visibility=View.INVISIBLE
        }
    }
    private fun setClickable(click: Boolean){
        binding.atoz.isClickable = !click
        binding.ztoa.isClickable = !click
    }

}
