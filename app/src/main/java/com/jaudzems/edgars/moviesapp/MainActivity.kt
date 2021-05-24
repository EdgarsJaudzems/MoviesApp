package com.jaudzems.edgars.moviesapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.themoviedb.org/"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getMyData()
    }

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback <MovieData> {
            override fun onResponse(
                call: Call<MovieData>,
                response: Response<MovieData>
            ) {

                val responseBody = response.body()!!

                val myStringBuilder = StringBuilder()

                for(myData in responseBody.results) {
                    myStringBuilder.append(myData.title)
                    myStringBuilder.append("\n")
                }

                val txt = findViewById<TextView>(R.id.txt)
                txt.text = myStringBuilder

                println(txt.text)


            }

            override fun onFailure(call: Call<MovieData>, t: Throwable) {
                Log.d("MainActivity", "onFailure"+t.message)
            }
        })
    }
}