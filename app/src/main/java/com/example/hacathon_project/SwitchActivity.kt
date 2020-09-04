package com.example.hacathon_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_switch.*

class SwitchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch)

        doctor_page.setOnClickListener {
            Intent(this,DoctorLogin::class.java).also {
                startActivity(it)
            }
        }

        user_page.setOnClickListener {
            Intent(this,MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}