package com.jaudzems.edgars.moviesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaudzems.edgars.moviesapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.themoviedb.org/"

class MainActivity : AppCompatActivity(),MovieAdapter.OnItemClickListener {

    lateinit var binding: ActivityMainBinding
    lateinit var movieAdapter: MovieAdapter
    lateinit private var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewSetup()
        getMyData()
    }

    private fun recyclerViewSetup() {
        binding.recyclerViewList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewList.layoutManager = linearLayoutManager
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

                movieAdapter = MovieAdapter(baseContext, responseBody.results, this@MainActivity)
                movieAdapter.notifyDataSetChanged()
                binding.recyclerViewList.adapter = movieAdapter
            }

            override fun onFailure(call: Call<MovieData>, t: Throwable) {
                Log.d("MainActivity", "onFailure"+t.message)
            }
        })
    }

    override fun onItemClick(movie: Result) {
        Toast.makeText(this, "${movie.title}", Toast.LENGTH_SHORT).show()

        val intent = Intent(this,DetailActivity::class.java)
        startActivity(intent)
    }
}