package com.jaudzems.edgars.moviesapp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaudzems.edgars.moviesapp.*
import com.jaudzems.edgars.moviesapp.adapters.MovieAdapter
import com.jaudzems.edgars.moviesapp.databinding.ActivityMainBinding
import com.jaudzems.edgars.moviesapp.network.RetrofitInstance
import com.jaudzems.edgars.moviesapp.network.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), MovieAdapter.OnItemClickListener {

    lateinit var binding: ActivityMainBinding
    lateinit var movieAdapter: MovieAdapter
    lateinit private var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewSetup()
        getPopularMovieData()

    }

    private fun recyclerViewSetup() {
        binding.recyclerViewList.setHasFixedSize(true)
        linearLayoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewList.layoutManager = linearLayoutManager
    }

    private fun getPopularMovieData() {
        val retrofitInstance =
            RetrofitInstance.getRetrofitInstance().create(RetrofitInterface::class.java)
        val call = retrofitInstance.getPopularMovieData()

        call.enqueue(object : Callback<MovieData> {
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
                Log.d("MainActivity", "onFailure" + t.message)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu,menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView

        menu?.findItem(R.id.popular).setVisible(false)

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()

                startActivity(
                    Intent(this@MainActivity, SearchActivity::class.java)
                        .putExtra("search_query", query)
                )
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.toprated -> {
                val intent = Intent(this@MainActivity, TopRatedActivity::class.java)
                startActivity(intent)
            }
            R.id.upcoming -> {
                val intent = Intent(this@MainActivity, UpcomingActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}