package com.example.loginapp

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.loginapp.Api.ApiService
import com.example.loginapp.Api.RetrofitHelper
import com.example.loginapp.databinding.ActivityHomeBinding
import com.example.loginapp.repository.ProductRepository
import com.example.loginapp.viewmodel.ProductViewModel
import com.example.loginapp.viewmodel.ProductViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import kotlin.math.log

class Home : AppCompatActivity(),TopBarTitleChangeListener  {
    lateinit var productViewModel: ProductViewModel
    lateinit var binding: ActivityHomeBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    override fun updateTopBarTitle(newTitle: String) {
        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        toolbar.title = newTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView

        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawer(GravityCompat.START)

            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(Home_Page())
                }
                R.id.trash -> {
                   delate()
                }
                R.id.settings -> {
                   replaceFragment(Settings())
                }
                R.id.nav_logout -> {
                   logout()
                }
                R.id.nav_share -> {
                    Toast.makeText(this, "Share is clicked", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    return@setNavigationItemSelectedListener true
                }
            }
            true
        }

        val apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
        val repository = ProductRepository(apiService)
        productViewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(repository)
        ).get(ProductViewModel::class.java)
        productViewModel.category.observe(this) {
            it?.let {
                Log.d("masud", it.toString())
            }
        }
        productViewModel.products.observe(this) {
            it.products.let {
                Log.d("masud 1", it.toString())
            }
        }

        binding.bottomNavigationBar.background = null
        replaceFragment(Category())

        binding.bottomNavigationBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.category -> {
                    replaceFragment(Category())
                }
                R.id.product -> {
                    replaceFragment(Product())
                }
            }
            true // Return true to indicate that the selection event has been handled
        }
        sharedPreferencesHelper = SharedPreferencesHelper(this)


    }
    private fun logout() {
        val sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)

        editor.apply()

        // Start the login activity and finish the current activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun delate() {
        val sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userIdKey",null)
        editor.putString("passwordKey",null)

        editor.apply()

        // Start the login activity and finish the current activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_Layout, fragment)
        fragmentTransaction.commit()
    }
}
