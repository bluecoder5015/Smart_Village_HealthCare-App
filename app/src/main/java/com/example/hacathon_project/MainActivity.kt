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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private var isRemember =false
    //private val BASE_URL = "http://192.168.43.208:3000"
    private val BASE_URL = "https://smartvillagehacksagon.herokuapp.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences= getSharedPreferences("LOGIN_USER", Context.MODE_PRIVATE)
        isRemember = sharedPreferences.getBoolean("CHECKBOX",false)
        if(isRemember)
        {
            Intent(this@MainActivity,Dashboard::class.java).also {
                startActivity(it)
                finish()
            }
        }

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var retrofitInterface: RetrofitInterface = retrofit
            .create(RetrofitInterface::class.java)

        //event
        login_button.setOnClickListener {
            loginUser(
                login_email.editText?.text.toString(),
                login_password.editText?.text.toString(),
                checkbox_user.isChecked
            )
        }

        regis_page.setOnClickListener {
            Intent(this@MainActivity,RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun loginUser(email: String, password: String,ischeck:Boolean) {


        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitInterface: RetrofitInterface = retrofit.create(RetrofitInterface::class.java)

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Email Can`t be Empty",Toast.LENGTH_SHORT).show()
            return
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Password Can`t be Empty",Toast.LENGTH_SHORT).show()
        }
        val progress = ProgressDialog(this)
        progress.setMessage("Verifying Credentials :) ")
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress.isIndeterminate = true
        progress.show()
        val map: HashMap<String?, String?> = HashMap()
        map["email"]=email
        map["password"]=password

        val call: Call<Void?>? = retrofitInterface.executeLogin(map)
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
                        editor.putBoolean("CHECKBOX",ischeck)
                        editor.apply()
                        Toast.makeText(
                            this@MainActivity, "Login Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.dismiss()

                        Intent(this@MainActivity,Dashboard::class.java).also{
                            startActivity(it)
                            finish()
                        }
                    }
                    response.code() == 404 -> {
                        Toast.makeText(
                            this@MainActivity, "Wrong Credentials",
                            Toast.LENGTH_LONG
                        ).show()
                        progress.dismiss()
                    }
                    response.code() == 400 -> {
                        Toast.makeText(
                            this@MainActivity, "Wrong Credentials",
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
                    this@MainActivity, t.message,
                    Toast.LENGTH_LONG
                ).show()
                progress.dismiss()
            }
        })
    }
}


