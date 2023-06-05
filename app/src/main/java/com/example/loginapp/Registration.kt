package com.example.loginapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class Registration : AppCompatActivity() {
    private lateinit var textInput: TextInputEditText
    private lateinit var spinnerLayout: TextInputLayout
    private lateinit var spinner: AutoCompleteTextView
    private lateinit var resAccName: TextInputEditText
    private lateinit var resPass: TextInputEditText
    private lateinit var submit: AppCompatButton
    lateinit var divisions: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_registration)

        textInput = findViewById(R.id.text_input)
        spinnerLayout=findViewById(R.id.spinner_layout)
        spinner=findViewById(R.id.spinner_id)
        resPass=findViewById(R.id.logIn_Password)
        resAccName=findViewById(R.id.res_acc_name)
        submit=findViewById(R.id.res_btn)

        textInput.setOnClickListener {
            // Get current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create a new DatePickerDialog
            val datePickerDialog = DatePickerDialog(this@Registration,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Handle the selected date
                    val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    textInput.setText(selectedDate)
                }, year, month, day)

            // Show the DatePickerDialog
            datePickerDialog.show()
        }
        divisions=resources.getStringArray(R.array.division_name)
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.spinner_layout,
            R.id.spinner_text,
            divisions
        )
        spinner.setAdapter(adapter)
        submit.setOnClickListener {
            val name:String=resAccName.text.toString()
            val pass:String=resPass.text.toString()
            val sharedPreferences: SharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("userIdKey", name)
            editor.putString("passwordKey",pass)
            editor.apply()
            Toast.makeText(applicationContext, "Data is stored", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@Registration, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
