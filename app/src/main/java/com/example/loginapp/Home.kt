package com.example.loginapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.loginapp.Api.ApiService
import com.example.loginapp.Api.RetrofitHelper
import com.example.loginapp.databinding.ActivityHomeBinding
import com.example.loginapp.repository.ProductRepository
import com.example.loginapp.viewmodel.ProductViewModel
import com.example.loginapp.viewmodel.ProductViewModelFactory
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.log

class Home : AppCompatActivity(),TopBarTitleChangeListener  {
    lateinit var productViewModel: ProductViewModel
    lateinit var binding: ActivityHomeBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val REQUEST_PERMISSION_CODE= 123
    private  val BACKGROUND_IMAGE_CODE=101
    private  val PROFILE_IMAGE_CODE=102
    private var selectedField:Int =0
    private lateinit var imageView: ImageView
    private lateinit var backgound: FloatingActionButton
    private lateinit var profileImage: CircleImageView
    private lateinit var profileButton: FloatingActionButton
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var sharedPreferences: SharedPreferences
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
        sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE)
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
        val navigationHeader: View = navigationView.getHeaderView(0)

         imageView=navigationHeader.findViewById(R.id.imageView2)
         backgound=navigationHeader.findViewById(R.id.floatingActionButton)

        profileImage=navigationHeader.findViewById(R.id.profile_image)

        profileButton=navigationHeader.findViewById(R.id.floatingActionButton2)
        name=navigationHeader.findViewById(R.id.userName)
        email=navigationHeader.findViewById(R.id.email)
        val savedUsername = sharedPreferences.getString("userName", null)
        val savedEmail = sharedPreferences.getString("email", null)
        name.setText(savedUsername)
        email.setText(savedEmail)
        backgound.setOnClickListener {
            if (checkPermissions()) {


                openImagePicker(BACKGROUND_IMAGE_CODE)
            } else {
                selectedField=BACKGROUND_IMAGE_CODE
                Log.d("hello",selectedField.toString())
                requestPermissions()
            }
        }
       profileButton.setOnClickListener {
            if (checkPermissions()) {

                openImagePicker(PROFILE_IMAGE_CODE)
            } else {
                selectedField=PROFILE_IMAGE_CODE
                requestPermissions()
            }
        }

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
    private fun checkPermissions(): Boolean {
        val permissionCamera =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val permissionStorage =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return permissionCamera == PackageManager.PERMISSION_GRANTED && permissionStorage == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }
    private fun openImagePicker(code:Int) {
        Log.d("onclick",code.toString())
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(code)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== REQUEST_PERMISSION_CODE){
            if (grantResults.isNotEmpty() &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.d("selectedField",selectedField.toString())
                openImagePicker(selectedField)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri: Uri? =data?.data
        if (resultCode == RESULT_OK) {
            if (requestCode == BACKGROUND_IMAGE_CODE) {
                Glide.with(this)
                    .load(uri)
                    .into(imageView)
            } else if (requestCode == PROFILE_IMAGE_CODE) {
                Glide.with(this)
                    .load(uri)
                    .into(profileImage)
            }
        }



    }
}
