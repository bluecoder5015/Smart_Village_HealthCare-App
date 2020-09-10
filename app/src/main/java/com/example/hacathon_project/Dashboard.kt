package com.example.hacathon_project

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_dashboard_doctor.*
import java.text.SimpleDateFormat
import java.util.*

class Dashboard : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        preferences = getSharedPreferences("LOGIN_USER", Context.MODE_PRIVATE)
        val email = preferences.getString("EMAIL","")
        user_email.text = email
        val cityspin: Spinner = findViewById(R.id.spinnercity)
        val villagespin: Spinner = findViewById(R.id.spinnervillage)


        cityspin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent?.getItemAtPosition(position)?.equals("Bhopal")!!) {
                    ArrayAdapter.createFromResource(
                        this@Dashboard,
                        R.array.villages_bhopal,
                        android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        villagespin.adapter = adapter
                    }

                }
                if (parent.getItemAtPosition(position)?.equals("Mumbai")!!) {
                    ArrayAdapter.createFromResource(
                        this@Dashboard,
                        R.array.villages_mumbai,
                        android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        villagespin.adapter = adapter
                    }

                }

                var date: String = ""

                var formate = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                val cal = Calendar.getInstance()
                dateButton.setOnClickListener {
                    val now = Calendar.getInstance()
                    val datePicker = DatePickerDialog(
                        this@Dashboard,
                        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                            val selectedDate = Calendar.getInstance()
                            selectedDate.set(Calendar.YEAR, year)
                            selectedDate.set(Calendar.MONTH, month)
                            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            date = formate.format(selectedDate.time).toString()
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                    )
                    datePicker.show()
                }
                shedule_button.setOnClickListener {
                    val city = cityspin.selectedItem.toString()
                    val village = villagespin.selectedItem.toString()
                    val now = date
                    if(TextUtils.isEmpty(now)){
                        Toast.makeText(this@Dashboard,"Please Select Date",Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    Intent(this@Dashboard, Time_recycleractivity::class.java).also {
                        it.putExtra("message", now)
                        it.putExtra("city", city)
                        it.putExtra("village", village)
                        startActivity(it)
                    }
                }
                logout_user.setOnClickListener {
                    val editor:SharedPreferences.Editor = preferences.edit()
                    editor.clear()
                    editor.apply()

                    Intent(this@Dashboard,MainActivity::class.java).also{
                        startActivity(it)
                        finish()
                    }
                }



                health_button.setOnClickListener {
                    Intent(this@Dashboard, HealthIssue_Form::class.java).also {
                        startActivity(it)
                    }
                }


            }
        }
    }
}