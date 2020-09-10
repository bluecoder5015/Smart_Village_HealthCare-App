package com.example.hacathon_project

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.hacathon_project.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.activity_doctorlogin.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DoctorLogin : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private var isRemember =false
    //private val BASE_URL = "http://192.168.43.208:3000"
    private val BASE_URL = "https://smartvillagehacksagon.herokuapp.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctorlogin)

        sharedPreferences= getSharedPreferences("LOGIN_DOCTOR", Context.MODE_PRIVATE)
        isRemember = sharedPreferences.getBoolean("CHECKBOX",false)
        if(isRemember)
        {
            Intent(this@DoctorLogin,Dashboard_Doctor::class.java).also {
                startActivity(it)
                finish()
            }
        }

        logindoctor_button.setOnClickListener {
                val email=logindoctor_email.editText?.text.toString()
               val password = logindoctor_password.editText?.text.toString()
               val checked = checkbox_doctor.isChecked



            if(TextUtils.isEmpty(email))
            {
                Toast.makeText(this,"Email Can`t be Empty",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password))
            {
                Toast.makeText(this,"Password Can`t be Empty",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val progress = ProgressDialog(this)
            progress.setMessage("Verifying Credentials :) ")
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progress.isIndeterminate = true
            progress.show()
            val map: HashMap<String?, String?> = HashMap()
            map["email"]=email
            map["password"]=password

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitInterface: RetrofitInterface = retrofit
                .create(RetrofitInterface::class.java)

            //event
            val call: Call<Void?>? = retrofitInterface.executeLogindoctor(map)
            call!!.enqueue(object : Callback<Void?> {
                override fun onResponse(
                    call: Call<Void?>?,
                    response: Response<Void?>
                ) {
                    when {
                        response.code() == 200 -> {
                            //var result = response.body()

                            val editor:SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putString("EMAIL",email)
                            editor.putString("PASSWORD",password)
                            editor.putBoolean("CHECKBOX",checked)
                            editor.apply()

                            Toast.makeText(
                                this@DoctorLogin, "Login Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            progress.dismiss()

                            Intent(this@DoctorLogin,Dashboard_Doctor::class.java).also{
                                startActivity(it)
                                finish()
                            }
                        }
                        response.code() == 404 -> {
                            Toast.makeText(
                                this@DoctorLogin, "Wrong Password",
                                Toast.LENGTH_LONG
                            ).show()
                            progress.dismiss()
                        }
                        response.code() == 400 -> {
                            Toast.makeText(
                                this@DoctorLogin, "Wrong Email Id",
                                Toast.LENGTH_LONG
                            ).show()
                            progress.dismiss()
                        }
                    }
                }

                override fun onFailure(
                    call: Call<Void?>?,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@DoctorLogin, t.message,
                        Toast.LENGTH_LONG
                    ).show()
                    progress.dismiss()
                }
            })
        }

        regisdoctor_page.setOnClickListener {
            Intent(this@DoctorLogin,DoctorRegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }

}


