package com.jaudzems.edgars.moviesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
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
//        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager = GridLayoutManager(this, 2)
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
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<MovieData>, t: Throwable) {
                Log.d("MainActivity", "onFailure"+t.message)
            }
        })
    }

    override fun onItemClick(movie: Result) {
        startActivity(Intent(this,DetailActivity::class.java)
            .putExtra("intent_movie_poster", movie.poster_path)
            .putExtra("intent_movie_backdrop_poster", movie.backdrop_path)
            .putExtra("intent_movie_title", movie.title)
            .putExtra("intent_movie_overview", movie.overview)
            .putExtra("intent_movie_release_date", movie.release_date)
            .putExtra("intent_movie_popularity", movie.popularity)
            .putExtra("intent_movie_vote_average", movie.vote_average)
            .putExtra("intent_movie_id", movie.id)
        )
    }
}