package com.example.hacathon_project

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hacathon_project.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.dialog_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmailVerification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_activity)

        val text = this.intent.getStringExtra("text")
        val edit_name = this.intent.getStringExtra("name")
        val edit_email = this.intent.getStringExtra("email")
        val edit_password = this.intent.getStringExtra("password")
        val edit_phone = this.intent.getStringExtra("phone")
        val city = this.intent.getStringExtra("city")
        val village = this.intent.getStringExtra("village")

        ok_dialog.setOnClickListener {
            val progress = ProgressDialog(this@EmailVerification)
            progress.setMessage("Verifying Credentials :) ")
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progress.isIndeterminate = true
            progress.show()
            val codeenter= dialog_code.editText?.text.toString()
        if ( codeenter == text) {
            //val BASE_URL = "http://192.168.43.208:3000"
            val BASE_URL = "https://smartvillagehacksagon.herokuapp.com/"

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitInterface: RetrofitInterface =
                retrofit.create(RetrofitInterface::class.java)

            //For Registration

            val map: HashMap<String?, String?> = HashMap()
            map["name"] = edit_name
            map["email"] = edit_email
            map["password"] = edit_password
            map["city"] = city
            map["village"] = village
            map["phone"] = edit_phone

            val call: Call<Void?>? = retrofitInterface.executeSignup(map)
            call!!.enqueue(object : Callback<Void?> {
                override fun onResponse(
                    call: Call<Void?>?,
                    response: Response<Void?>
                ) {
                    if (response.code() == 200) {
                        //var result = response.body()
                        Toast.makeText(
                            this@EmailVerification, "Registered Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog_code.editText?.text?.clear()
                        progress.dismiss()
                        Intent(this@EmailVerification,MainActivity::class.java).also{
                            startActivity(it)
                        }


                    } else if (response.code() == 400) {
                        Toast.makeText(
                            this@EmailVerification, "Already Registered! Login",
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.dismiss()
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Toast.makeText(
                        this@EmailVerification, "Try Again!",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                }
            })
        }
            else{
            Toast.makeText(this,"Wrong 6-Digit code",Toast.LENGTH_SHORT).show()
            progress.dismiss()
        }
        }
        cancel_dialog.setOnClickListener {
            finish()
        }
    }
}