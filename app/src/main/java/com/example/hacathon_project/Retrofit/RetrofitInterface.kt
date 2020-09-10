package com.example.hacathon_project.Retrofit
import com.example.hacathon_project.TimeTodos
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.*
import com.example.hacathon_project.Todos

interface RetrofitInterface {
    @POST("/login")
    fun executeLogin(@Body map: HashMap<String?, String?>?): Call<Void?>?

    @POST("/register")
    fun executeSignup(@Body map: HashMap<String?, String?>?): Call<Void?>?

    @POST("/registerdoctor")
    fun executeSignupdoctor(@Body map: HashMap<String?, String?>?): Call<Void?>?

    @POST("/logindoctor")
    fun executeLogindoctor(@Body map: HashMap<String?, String?>?): Call<Void?>?

    @POST("/issueinsert")
    fun executeIssueinsert(@Body map: HashMap<String?, String?>?): Call<Void?>?

    @POST("/getissue")
    fun executeGetissue(@Body map: HashMap<String?, String?>?): Call<Array<Todos>?>?

    @POST("/verifyemail")
    fun executeVerifyemail(@Body map:HashMap<String?,String?>?): Call<Void?>?

    @POST("/inserttime")
    fun executeTimeinsert(@Body map: HashMap<String?, String?>?): Call<Void?>?

    @POST("/gettime")
    fun executeGettime(@Body map: HashMap<String?, String?>?): Call<Array<TimeTodos>?>?

}