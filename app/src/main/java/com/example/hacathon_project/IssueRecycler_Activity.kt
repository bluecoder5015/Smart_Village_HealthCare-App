package com.example.hacathon_project

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hacathon_project.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.activity_issuerecycler.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class IssueRecycler_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issuerecycler)

        val date = intent.getStringExtra("message")
        val city =intent.getStringExtra("city")
        val village =intent.getStringExtra("village")


        val BASE_URL = "http://192.168.43.208:3000"

        val progress = ProgressDialog(this@IssueRecycler_Activity)
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

        val call: Call<Array<Todos>?>? = retrofitInterface.executeGetissue(map)
        call!!.enqueue(object : Callback<Array<Todos>?> {
            override fun onResponse(
                call: Call<Array<Todos>?>?,
                response: Response<Array<Todos>?>
            ) {
                if (response.code() == 200) {
                    val result: Array<Todos>? = response.body()
                    Toast.makeText(
                        this@IssueRecycler_Activity, "Issue List",
                        Toast.LENGTH_SHORT
                    ).show()
                    val adapt = result?.toList()?.let { TodoAdapter(it) }
                    rv_item.adapter =adapt
                    rv_item.layoutManager = LinearLayoutManager(this@IssueRecycler_Activity)
                    progress.dismiss()


                } else if (response.code() == 404) {
                    Toast.makeText(
                        this@IssueRecycler_Activity, "Try Again!",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                }
            }

            override fun onFailure(call: Call<Array<Todos>?>, t: Throwable) {
                Toast.makeText(
                    this@IssueRecycler_Activity, "Try Again",
                    Toast.LENGTH_SHORT
                ).show()
                progress.dismiss()
            }
        })
    }
}