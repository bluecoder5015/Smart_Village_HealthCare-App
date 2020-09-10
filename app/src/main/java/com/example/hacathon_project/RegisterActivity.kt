package com.example.hacathon_project

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hacathon_project.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val cities_spin: Spinner = findViewById(R.id.cities_spinner)
        val village_spin: Spinner = findViewById(R.id.village_spinner)


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
                        this@RegisterActivity,
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
                        this@RegisterActivity,
                        R.array.villages_mumbai,
                        android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        village_spin.adapter = adapter
                    }

                }

                val  USERNAME_PATTERN:String= "[a-zA-Z.\\s]+"

                var pattern:Pattern
                pattern = Pattern.compile(USERNAME_PATTERN)

                submitreg_button.setOnClickListener {

                    val progress = ProgressDialog(this@RegisterActivity)
                    progress.setMessage("Verifying Credentials :) ")
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                    progress.isIndeterminate = true
                    progress.show()
                    val edit_email = regis_email.editText?.text.toString()
                    val edit_name = regis_name.editText?.text.toString()
                    val edit_password = regis_password.editText?.text.toString()
                    val edit_phone = regis_phone.editText?.text.toString()
                    val city = cities_spin.selectedItem.toString()
                    val village= village_spin.selectedItem.toString()


                    when {
                        (!pattern.matcher(edit_name).matches()) -> {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Enter Valid Name",
                                Toast.LENGTH_SHORT
                            ).show()
                            progress.dismiss()
                            return@setOnClickListener
                        }
                        (!Patterns.EMAIL_ADDRESS.matcher(edit_email).matches()) -> {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Invalid Email Address",
                                Toast.LENGTH_SHORT
                            ).show()
                            progress.dismiss()
                            return@setOnClickListener
                        }
                        (!Patterns.PHONE.matcher(edit_phone).matches()) -> {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Enter Valid Phone Number",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            progress.dismiss()
                            return@setOnClickListener
                        }
                        (edit_phone.length != 10) -> {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Enter Valid 10 Digit Phone Number",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            progress.dismiss()
                            return@setOnClickListener
                        }

                        (TextUtils.isEmpty(edit_password)) -> {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Password can`t be Empty",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            progress.dismiss()
                            return@setOnClickListener
                        }
                        (edit_password.length < 8) -> {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Enter minimum 8 digit password",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            progress.dismiss()
                            return@setOnClickListener
                        }

                    }


                    //val BASE_URL = "http://192.168.43.208:3000"
                    val BASE_URL = "https://smartvillagehacksagon.herokuapp.com/"

                    val retrofit: Retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val retrofitInterface: RetrofitInterface =
                        retrofit.create(RetrofitInterface::class.java)


                    val STRING_LENGTH = 6;
                    val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
                    val randomString = (1..STRING_LENGTH)
                        .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
                        .map(charPool::get)
                        .joinToString("");
                    val code=randomString
                    val map: HashMap<String?, String?> = HashMap()
                    map["code"]=code
                    map["email"]=edit_email


                    val call: Call<Void?>? = retrofitInterface.executeVerifyemail(map)
                    call!!.enqueue(object : Callback<Void?> {
                        override fun onResponse(
                            call: Call<Void?>?,
                            response: Response<Void?>
                        ) {
                            if (response.code() == 200) {
                                //var result = response.body()
                                Toast.makeText(
                                    this@RegisterActivity, "Email Sent Sucessfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                progress.dismiss()
                                Intent(this@RegisterActivity,EmailVerification::class.java).also {
                                    it.putExtra("text",code)
                                    it.putExtra("email",edit_email)
                                    it.putExtra("name",edit_name)
                                    it.putExtra("password",edit_password)
                                    it.putExtra("city",city)
                                    it.putExtra("village",village)
                                    it.putExtra("phone",edit_phone)
                                    startActivity(it)
                                }
                                regis_email.editText?.text?.clear()
                                regis_name.editText?.text?.clear()
                                regis_password.editText?.text?.clear()
                                regis_phone.editText?.text?.clear()

                            } else if (response.code() == 400) {
                                Toast.makeText(
                                    this@RegisterActivity, "Please try Again!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progress.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<Void?>, t: Throwable) {
                            Toast.makeText(
                                this@RegisterActivity, "Try Again!",
                                Toast.LENGTH_SHORT
                            ).show()
                            progress.dismiss()
                        }
                    })

                }

            }
        }
    }
}
