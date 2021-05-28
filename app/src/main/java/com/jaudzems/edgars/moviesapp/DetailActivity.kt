package com.jaudzems.edgars.moviesapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.jaudzems.edgars.moviesapp.databinding.ActivityDetailBinding
import com.jaudzems.edgars.moviesapp.network.RetrofitInstance
import com.jaudzems.edgars.moviesapp.network.RetrofitInterface
import com.jaudzems.edgars.moviesapp.network.SingleMovieTrailer
import com.jaudzems.edgars.moviesapp.network.SingleMoviedata
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //MovieId Intent
        val movieId = intent.getIntExtra("intent_movie_id", 578701).toString()

        loadIntentData()

        getSingleMovieDetailData(movieId)
        getSingleMovieTrailerData(movieId)

    }

    private fun getSingleMovieTrailerData(movieId: String) {
        val retrofitInstance =
            RetrofitInstance.getRetrofitInstance().create(RetrofitInterface::class.java)
        val call = retrofitInstance.getTrailer(movieId)

        call.enqueue(object : Callback<SingleMovieTrailer?> {
            override fun onResponse(
                call: Call<SingleMovieTrailer?>,
                response: Response<SingleMovieTrailer?>
            ) {
                val trailerMovieResponse = response.body()!!
                binding.movieTrailerText.text = trailerMovieResponse.results[0].name


                binding.yotubeButton.setOnClickListener {
                    val youtubeUrl = "https://www.youtube.com/watch?v="
                    val movieTrailerKey = trailerMovieResponse.results[0].key
                    val trailerUrl = youtubeUrl + movieTrailerKey

                    val youtubeIntent = Intent(Intent.ACTION_VIEW)
                    youtubeIntent.data = Uri.parse(trailerUrl)
                    startActivity(youtubeIntent)
                }

            }

            override fun onFailure(call: Call<SingleMovieTrailer?>, t: Throwable) {
                    Log.d("DetailActivity", "onFailure" + t.message)
            }
        })
    }

    private fun getSingleMovieDetailData(movieId: String) {
        val retrofitInstance =
            RetrofitInstance.getRetrofitInstance().create(RetrofitInterface::class.java)
        val call = retrofitInstance.getSingleMovieData(movieId)

        call.enqueue(object : Callback<SingleMoviedata?> {
            override fun onResponse(
                call: Call<SingleMoviedata?>,
                response: Response<SingleMoviedata?>
            ) {
                val singleMovieResponse = response.body()!!

                //Single movie EXTRA data -------------
                //Tagline
                if (singleMovieResponse.tagline != "") {
                    binding.movieTaglineText.text = singleMovieResponse.tagline
                } else {
                    binding.movieTagline.visibility = (View.GONE)
                    binding.movieTaglineText.visibility = (View.GONE)
                }
                //Genre
                var genre = ""
                for (value in singleMovieResponse.genres) {
                    genre += value.name + ", "
                }
                var movieGenres = genre.dropLast(2)

                binding.movieGenreText.text = movieGenres

                //Language
                var languages = ""
                for (value in singleMovieResponse.spoken_languages) {
                    languages += value.name + ", "
                }
                var movieLanguages = languages.dropLast(2)

                binding.movieLanguagesText.text = movieLanguages

                //Budget
                if (singleMovieResponse.budget != 0) {
                    binding.movieBudgetText.text = ("${singleMovieResponse.budget.toString()}$")
                } else {
                    binding.movieBudgetText.text = "-"
                }

                //Revenue
                if (singleMovieResponse.revenue != 0) {
                    binding.movieRevenueText.text = ("${singleMovieResponse.revenue.toString()}$")
                } else {
                    binding.movieRevenueText.text = "-"
                }

                //Runtime
                val runtime = singleMovieResponse.runtime
                val runtimeHours = runtime / 60
                val runtimeMinutes = runtime % 60
                binding.movieRuntimeText.text = ("${runtimeHours}.${runtimeMinutes}h").toString()
            }

            override fun onFailure(call: Call<SingleMoviedata?>, t: Throwable) {
                Log.d("DetailActivity", "onFailure" + t.message)
            }
        })
    }

    private fun loadIntentData() {
        // Header
        supportActionBar!!.title = intent.getStringExtra("intent_movie_title")

        // Textview intents
        binding.movieTitleText.text = intent.getStringExtra("intent_movie_title")
        binding.movieReleaseDateShort.text = "(${
            intent.getStringExtra("intent_movie_release_date")
                ?.take(4)
        })"

        binding.movieReleaseDateText.text = intent.getStringExtra("intent_movie_release_date")
        binding.movieOverviewText.text = intent.getStringExtra("intent_movie_overview")

        binding.movieVoteCountText.text =
            intent.getDoubleExtra("intent_movie_vote_average", 10.00).toString()
        binding.moviePopularityText.text =
            intent.getDoubleExtra("intent_movie_popularity", 10.00).toString()

        // Image base url
        val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        val movieBackPoster = IMAGE_BASE + intent.getStringExtra("intent_movie_backdrop_poster")
        val moviePoster = IMAGE_BASE + intent.getStringExtra("intent_movie_poster")

        Glide.with(this)
            .load(movieBackPoster)
            .into(binding.movieBackPoster)

        Glide.with(this)
            .load(moviePoster)
            .into(binding.movieFrontPoster)

        //Button to movie website
        val MOVIE_BASE = "https://www.themoviedb.org/movie/"

        binding.movieButton.setOnClickListener {
            val movieId = intent.getIntExtra("intent_movie_id", 578701).toString()
            val url = MOVIE_BASE + movieId

            val buttonIntent = Intent(Intent.ACTION_VIEW)
            buttonIntent.data = Uri.parse(url)
            startActivity(buttonIntent)
        }
    }
}