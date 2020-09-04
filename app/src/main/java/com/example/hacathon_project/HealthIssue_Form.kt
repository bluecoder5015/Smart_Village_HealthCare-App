package com.example.hacathon_project

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.hacathon_project.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.activity_healthissue__form.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

class HealthIssue_Form : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_healthissue__form)

        val BASE_URL = "http://192.168.43.208:3000"


        val  Name_Pattern:String= "[a-zA-Z.\\s]+"

        var pattern: Pattern
        pattern = Pattern.compile(Name_Pattern)
        submit_form.setOnClickListener {

            val progress = ProgressDialog(this@HealthIssue_Form)
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

            val formname = name_form.editText?.text?.toString()
            val formage = age_form.editText?.text?.toString()
            val formcity = cities_form.selectedItem.toString()
            val formvillage = village_form.selectedItem.toString()
            val formsymptoms = symptoms_form.editText?.text?.toString()
            val formhouse = house_form.editText?.text?.toString()
            val formintake = intake_form.editText?.text?.toString()
            val forminjury = injury_form.editText?.text?.toString()

            when{
                TextUtils.isEmpty(formname) -> {
                    Toast.makeText(this@HealthIssue_Form, "Enter Name", Toast.LENGTH_SHORT).show()
                    progress.dismiss()
                    return@setOnClickListener
                }
                !pattern.matcher(formname.toString()).matches() -> {
                    Toast.makeText(
                        this@HealthIssue_Form,
                        "Enter Valid Name",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                    return@setOnClickListener
                }

                TextUtils.isEmpty(formage) -> {
                    Toast.makeText(
                        this@HealthIssue_Form,
                        "Enter Age",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                    return@setOnClickListener
                }

                !pattern.matcher(formsymptoms.toString()).matches() -> {
                    Toast.makeText(
                        this@HealthIssue_Form,
                        "Enter Valid Symptoms",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                    return@setOnClickListener
                }
            }

            val currentTime: String = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

            val map: HashMap<String?, String?> = HashMap()
            map["name"] = formname
            map["age"] = formage
            map["symptoms"] = formsymptoms
            map["city"] = formcity
            map["landmark"] = formhouse
            map["village"] = formvillage
            map["intake"] = formintake
            map["injury"] = forminjury
            map["date"] = currentTime

            val call: Call<Void?>? = retrofitInterface.executeIssueinsert(map)
            call!!.enqueue(object : Callback<Void?> {
                override fun onResponse(
                    call: Call<Void?>?,
                    response: Response<Void?>
                ) {
                    if (response.code() == 200) {
                        //var result = response.body()
                        Toast.makeText(
                            this@HealthIssue_Form, "Issue Submitted",
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.dismiss()
                        name_form.editText?.text?.clear()
                        age_form.editText?.text?.clear()
                        symptoms_form.editText?.text?.clear()
                        house_form.editText?.text?.clear()
                        intake_form.editText?.text?.clear()
                        injury_form.editText?.text?.clear()

                    } else if (response.code() == 404) {
                        Toast.makeText(
                            this@HealthIssue_Form, "Try Again!",
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.dismiss()
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Toast.makeText(
                        this@HealthIssue_Form, "Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                }
            })
        }
    }
}
