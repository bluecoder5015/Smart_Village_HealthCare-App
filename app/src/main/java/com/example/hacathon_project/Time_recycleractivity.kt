package com.example.hacathon_project

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hacathon_project.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.activity_time_recycler.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class Time_recycleractivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_recycler)

        val date = intent.getStringExtra("message")
        val city =intent.getStringExtra("city")
        val village =intent.getStringExtra("village")


        val BASE_URL = "http://192.168.43.208:3000"

        val progress = ProgressDialog(this@Time_recycleractivity)
        progress.setMessage("Verifying Credentials :) ")
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress.isIndeterminate = true
        progress.show()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitInterface: RetrofitInterface =
            retrofit.create(RetrofitInterface::class.java)

        val currentTime: String = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        val map: HashMap<String?, String?> = HashMap()
        map["city"] = city
        map["village"] = village
        map["date"] = date

        val call: Call<Array<TimeTodos>?>? = retrofitInterface.executeGettime(map)
        call!!.enqueue(object : Callback<Array<TimeTodos>?> {
            override fun onResponse(
                call: Call<Array<TimeTodos>?>?,
                response: Response<Array<TimeTodos>?>
            ) {
                if (response.code() == 200) {
                    val result: Array<TimeTodos>? = response.body()
                    Toast.makeText(
                        this@Time_recycleractivity, "Scheduled Time",
                        Toast.LENGTH_SHORT
                    ).show()
                    val adapter = result?.toList()?.let { TimeTodoAdapter(it) }
                    rv_timeitem.adapter =adapter
                    rv_timeitem.layoutManager = LinearLayoutManager(this@Time_recycleractivity)
                    progress.dismiss()


                } else if (response.code() == 404) {
                    Toast.makeText(
                        this@Time_recycleractivity, "Try Again!",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                }
            }

            override fun onFailure(call: Call<Array<TimeTodos>?>, t: Throwable) {
                Toast.makeText(
                    this@Time_recycleractivity, "Try Again",
                    Toast.LENGTH_SHORT
                ).show()
                progress.dismiss()
            }
        })
    }
}