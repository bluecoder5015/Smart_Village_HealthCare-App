package com.example.hacathon_project

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.hacathon_project.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.activity_healthissue__form.*
import kotlinx.android.synthetic.main.activity_timesheduler.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

class Timesheduler : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheduler)

        val BASE_URL = "http://192.168.43.208:3000"


        var formate = SimpleDateFormat("dd/ MM /YYYY",Locale.getDefault())
        val formate2= SimpleDateFormat("yyyyMMdd",Locale.getDefault())
        var timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
        var localdate:String=""
        var localtime:String=""
        var date:String=""

        button2.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    date = formate.format(selectedDate.time)
                    date_time.text=date
                    localdate = formate2.format(selectedDate.time).toString()

                    Toast.makeText(this, "date : $date", Toast.LENGTH_SHORT).show()
                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        button.setOnClickListener {
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)
                    localtime=timeFormat.format(selectedTime.time)
                    hour_time.text = localtime
                },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false
            )
            timePicker.show()
        }


        val  USERNAME_PATTERN:String= "[a-zA-Z.\\s]+"

        var pattern: Pattern
        pattern = Pattern.compile(USERNAME_PATTERN)

        submit_time.setOnClickListener {

            val progress = ProgressDialog(this@Timesheduler)
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

            val formname =  name_time.editText?.text?.toString()
            val formspec =  specilities_time.editText?.text?.toString()
            val formcity = cities_time.selectedItem.toString()
            val formvillage = village_time.selectedItem.toString()

            when{
                TextUtils.isEmpty(formname) ->{
                    Toast.makeText(this,"Enter Name",Toast.LENGTH_SHORT).show()
                    progress.dismiss()
                    return@setOnClickListener
                }
                (!pattern.matcher(formname.toString()).matches())-> {
                    Toast.makeText(
                        this,
                        "Enter Valid Name",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                    return@setOnClickListener
                }
            }


            val map: HashMap<String?, String?> = HashMap()
            map["name"] = formname
            map["specilities"] =formspec
            map["village"]=formvillage
            map["city"]=formcity
            map["date"]=localdate
            map["datedisplay"]=date
            map["time"]=localtime


            val call: Call<Void?>? = retrofitInterface.executeTimeinsert(map)
            call!!.enqueue(object : Callback<Void?> {
                override fun onResponse(
                    call: Call<Void?>?,
                    response: Response<Void?>
                ) {
                    if (response.code() == 200) {
                        //var result = response.body()
                        Toast.makeText(
                            this@Timesheduler, "Time Scheduled Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.dismiss()
                        name_time.editText?.text?.clear()
                        specilities_time.editText?.text?.clear()

                    } else if (response.code() == 404) {
                        Toast.makeText(
                            this@Timesheduler, "Try Again!",
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.dismiss()
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Toast.makeText(
                        this@Timesheduler, "Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                }
            })
        }
    }
}
