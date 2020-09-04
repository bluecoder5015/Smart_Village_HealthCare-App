package com.example.hacathon_project

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_dashboard_doctor.*
import java.text.SimpleDateFormat
import java.util.*

class Dashboard_Doctor : AppCompatActivity() {

    private lateinit var docpreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_doctor)

       docpreferences = getSharedPreferences("LOGIN_DOCTOR", Context.MODE_PRIVATE)
        val email = docpreferences.getString("EMAIL","")
       email_doc.text = email
        val cities_spin: Spinner = findViewById(R.id.spinner1)
        val village_spin: Spinner = findViewById(R.id.spinner2)


        cities_spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                        this@Dashboard_Doctor,
                        R.array.villages_bhopal,
                        android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        village_spin.adapter = adapter
                    }

                }
                if (parent.getItemAtPosition(position)?.equals("Mumbai")!!) {
                    ArrayAdapter.createFromResource(
                        this@Dashboard_Doctor,
                        R.array.villages_mumbai,
                        android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        village_spin.adapter = adapter
                    }

                }

                val city = cities_spin.selectedItem.toString()
                val village = village_spin.selectedItem.toString()
                var date: String = ""

                var formate = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                val cal = Calendar.getInstance()
                materialButton.setOnClickListener {
                    val now = Calendar.getInstance()
                    val datePicker = DatePickerDialog(
                        this@Dashboard_Doctor,
                        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                            val selectedDate = Calendar.getInstance()
                            selectedDate.set(Calendar.YEAR, year)
                            selectedDate.set(Calendar.MONTH, month)
                            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            date = formate.format(selectedDate.time)
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                    )
                    datePicker.show()
                }
                showissue_button.setOnClickListener {
                    val city = cities_spin.selectedItem.toString()
                    val village = village_spin.selectedItem.toString()
                    val datex = date.toString()

                    if (TextUtils.isEmpty(datex)) {
                        Toast.makeText(
                            this@Dashboard_Doctor,
                            "Please Select Date",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }
                    Intent(this@Dashboard_Doctor, IssueRecycler_Activity::class.java).also {
                        it.putExtra("message", datex)
                        it.putExtra("city", city)
                        it.putExtra("village", village)
                        startActivity(it)
                    }
                }

                logout_doctor.setOnClickListener {
                    val editor:SharedPreferences.Editor = docpreferences.edit()
                    editor.clear()
                    editor.apply()

                    Intent(this@Dashboard_Doctor,DoctorLogin::class.java).also{
                        startActivity(it)
                        finish()
                    }
                }
                planshedule_button.setOnClickListener {
                    Intent(this@Dashboard_Doctor,Timesheduler::class.java).also {
                        startActivity(it)
                    }
                }

    }

        }
    }

}