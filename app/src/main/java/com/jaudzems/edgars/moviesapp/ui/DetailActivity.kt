package com.jaudzems.edgars.moviesapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jaudzems.edgars.moviesapp.R
import com.jaudzems.edgars.moviesapp.databinding.ActivityDetailBinding
import com.jaudzems.edgars.moviesapp.network.RetrofitInstance
import com.jaudzems.edgars.moviesapp.network.RetrofitInterface
import com.jaudzems.edgars.moviesapp.network.SingleMovieTrailer
import com.jaudzems.edgars.moviesapp.network.SingleMoviedata
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var shareButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //MovieId Intent
        val movieId = intent.getIntExtra("intent_movie_id", 578701).toString()

        //Setup
        setupHeaderToolbar()
        loadIntentData()
        getSingleMovieDetailData(movieId)
        getSingleMovieTrailerData(movieId)

        //Animation
        val topToBottomAnimation = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom)
        binding.movieTitleText.startAnimation(topToBottomAnimation)
        binding.movieReleaseDateShort.startAnimation(topToBottomAnimation)

        val topToBottomAnimation2 = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_2)
        binding.movieDetails.startAnimation(topToBottomAnimation2)

        //
//        shareButton()

    }

    private fun setupHeaderToolbar() {
        supportActionBar!!.title = intent.getStringExtra("intent_movie_title")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadIntentData() {
        // Textview intents
        binding.movieTitleText.text = intent.getStringExtra("intent_movie_title")
        binding.movieReleaseDateShort.text =
            "(${intent.getStringExtra("intent_movie_release_date")?.take(4)})"
        binding.movieReleaseDateText.text = intent.getStringExtra("intent_movie_release_date")
        binding.movieOverviewText.text = intent.getStringExtra("intent_movie_overview")
        binding.movieVoteCountText.text =
            intent.getDoubleExtra("intent_movie_vote_average", 10.00).toString()
        binding.moviePopularityText.text =
            intent.getDoubleExtra("intent_movie_popularity", 10.00).toString()

        // Image Load
        val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        val backPosterIntent = intent.getStringExtra("intent_movie_backdrop_poster")
        val movieBackPoster = IMAGE_BASE + backPosterIntent

        val posterIntent = intent.getStringExtra("intent_movie_poster")
        val moviePoster = IMAGE_BASE + posterIntent

        if (backPosterIntent == null) {
            binding.movieBackPoster.setImageResource(R.drawable.dummy)
        } else {
            Glide.with(this)
                .load(movieBackPoster)
                .transition(GenericTransitionOptions.with(R.anim.glide_image))
                .into(binding.movieBackPoster)
        }

        if (posterIntent == null) {
            binding.movieFrontPoster.setImageResource(R.drawable.dummy)
        } else {
            Glide.with(this)
                .load(moviePoster)
                .transition(GenericTransitionOptions.with(R.anim.glide_image_2))
                .into(binding.movieFrontPoster)

        }

        //Button to movie website
        val MOVIE_BASE = "https://www.themoviedb.org/movie/"

        binding.movieButton.setOnClickListener {
            val movieId = intent.getIntExtra("intent_movie_id", 578701).toString()
            val url = MOVIE_BASE + movieId

            val buttonIntent = Intent(Intent.ACTION_VIEW)
            buttonIntent.data = Uri.parse(url)
            startActivity(buttonIntent)
            activityAnimation()
        }
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
                val movieGenres = genre.dropLast(2)
                binding.movieGenreText.text = movieGenres

                //Language
                var languages = ""
                for (value in singleMovieResponse.spoken_languages) {
                    languages += value.name + ", "
                }
                val movieLanguages = languages.dropLast(2)
                binding.movieLanguagesText.text = movieLanguages

                //Budget
                if (singleMovieResponse.budget != 0) {
                    val budget = singleMovieResponse.budget
                    binding.movieBudgetText.text =
                        ("${NumberFormat.getIntegerInstance().format(budget).toString()}$")
                } else {
                    binding.movieBudgetText.text = "-"
                }

                //Revenue
                if (singleMovieResponse.revenue != 0) {
                    val revenue = singleMovieResponse.revenue
                    binding.movieRevenueText.text =
                        ("${NumberFormat.getIntegerInstance().format(revenue).toString()}$")
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

                if (trailerMovieResponse.results.isEmpty()) {
                    binding.movieTrailerText.text = "No trailer available"
                } else {
                    // Movie Trailer
                    binding.movieTrailerText.text = trailerMovieResponse.results[0].name
                    binding.yotubeButton.setOnClickListener {
                        val youtubeUrl = "https://www.youtube.com/watch?v="
                        val movieTrailerKey = trailerMovieResponse.results[0].key
                        val trailerUrl = youtubeUrl + movieTrailerKey

                        val youtubeIntent = Intent(Intent.ACTION_VIEW)
                        youtubeIntent.data = Uri.parse(trailerUrl)
                        startActivity(youtubeIntent)
                        activityAnimation()
                    }
                }
            }

            override fun onFailure(call: Call<SingleMovieTrailer?>, t: Throwable) {
                Log.d("DetailActivity", "onFailure" + t.message)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)

        val shareButton = menu?.findItem(R.id.shareButton)

        if (shareButton != null) {
            shareButton.setOnMenuItemClickListener {
                val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey Check out this great movie!"
            )
            shareIntent.type = "text/plain"
            startActivity(shareIntent)

                return@setOnMenuItemClickListener true
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun activityAnimation() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}