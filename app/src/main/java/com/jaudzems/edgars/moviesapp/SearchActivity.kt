package com.jaudzems.edgars.moviesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaudzems.edgars.moviesapp.databinding.ActivitySearchBinding
import com.jaudzems.edgars.moviesapp.network.RetrofitInstance
import com.jaudzems.edgars.moviesapp.network.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity(), MovieAdapter.OnItemClickListener {

    private lateinit var binding: ActivitySearchBinding
    lateinit var movieAdapter: MovieAdapter
    lateinit private var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchWord = intent.getStringExtra("search_query").toString()
        supportActionBar!!.title = "Search results: ${searchWord}"

        searchMovieData(searchWord)
        setupHeaderToolbar()
        recyclerViewSetup()
    }

    private fun setupHeaderToolbar() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun recyclerViewSetup() {
        binding.recyclerViewList.setHasFixedSize(true)
        linearLayoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewList.layoutManager = linearLayoutManager
    }

    private fun searchMovieData(search: String) {
        val retrofitInstance =
            RetrofitInstance.getRetrofitInstance().create(RetrofitInterface::class.java)
        val call = retrofitInstance.searchMovies("73619d549f33ccdf0116452a1f3f9427", search)

        call.enqueue(object : Callback<MovieData> {
            override fun onResponse(
                call: Call<MovieData>,
                response: Response<MovieData>
            ) {
                val responseBody = response.body()!!

                movieAdapter = MovieAdapter(baseContext, responseBody.results, this@SearchActivity)
                movieAdapter.notifyDataSetChanged()
                binding.recyclerViewList.adapter = movieAdapter
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<MovieData>, t: Throwable) {
                Log.d("SearchActivity", "onFailure" + t.message)
            }
        })
    }

    override fun onItemClick(movie: Result) {
        startActivity(
            Intent(this, DetailActivity::class.java)
                .putExtra("intent_movie_id", movie.id)
                .putExtra("intent_movie_poster", movie.poster_path)
                .putExtra("intent_movie_backdrop_poster", movie.backdrop_path)
                .putExtra("intent_movie_title", movie.title)
                .putExtra("intent_movie_overview", movie.overview)
                .putExtra("intent_movie_release_date", movie.release_date)
                .putExtra("intent_movie_popularity", movie.popularity)
                .putExtra("intent_movie_vote_average", movie.vote_average)
        )
    }
}