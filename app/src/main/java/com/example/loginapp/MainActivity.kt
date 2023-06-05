package com.example.loginapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {
    public lateinit var logIn_to_Registation : TextView
    public lateinit var LogIn_to_Home : TextView
    public lateinit var userId : TextInputEditText
    public lateinit var password : TextInputEditText
    public lateinit var userName: String
    public lateinit var passwordid: String
    public lateinit var checkbox: CheckBox
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        logIn_to_Registation = findViewById(R.id.log_to_registration)
        LogIn_to_Home = findViewById(R.id.logIn_Btn)
        logIn_to_Registation.setOnClickListener {
            val intent = Intent(this@MainActivity, Registration::class.java)
            startActivity(intent)
        }


        userId = findViewById(R.id.logIn_Text)
        password = findViewById(R.id.logIn_Password)
        checkbox=findViewById(R.id.logIn_checkbox)

        LogIn_to_Home.setOnClickListener {
            val pass: String = password.text.toString()
            val user: String = userId.text.toString()
            userId.setText("")
            password.setText("")
            if (login(user, pass)) {
                val intent = Intent(this@MainActivity, Home::class.java)
                startActivity(intent)
                if (checkbox.isChecked){
                    userId.setText(user)
                }

            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun login(username: String, password: String): Boolean {
        val sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("userIdKey", null)
        val savedPassword = sharedPreferences.getString("passwordKey", null)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn && savedUsername != null && savedPassword != null && savedUsername == username && savedPassword == password) {
            // User is already logged in and provided correct credentials
            return true
        } else if (!isLoggedIn && savedUsername != null && savedPassword != null && savedUsername == username && savedPassword == password) {
            // User is not logged in, but provided correct credentials
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", true) // Set the isLoggedIn flag to true
            editor.apply()
            return true
        }

        return false
    }

}