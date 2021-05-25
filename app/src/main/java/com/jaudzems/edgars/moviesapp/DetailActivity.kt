package com.jaudzems.edgars.moviesapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.jaudzems.edgars.moviesapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadIntentData()
    }

    private fun loadIntentData() {
        //Header
        supportActionBar!!.title = intent.getStringExtra("intent_movie_title")

        //Textview intents
        binding.movieTitleText.text = intent.getStringExtra("intent_movie_title")
        binding.movieOverviewText.text = intent.getStringExtra("intent_movie_overview")
        binding.movieReleaseDateText.text = intent.getStringExtra("intent_movie_release_date")

        binding.movieVoteCountText.text = intent.getDoubleExtra("intent_movie_vote_average",10.00).toString()
        binding.moviePopularityText.text = intent.getDoubleExtra("intent_movie_popularity", 10.00).toString()

        //Images
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
            val movieId = intent.getIntExtra("intent_movie_id",578701).toString()
            val url = MOVIE_BASE + movieId

            val buttonIntent = Intent(Intent.ACTION_VIEW)
            buttonIntent.data = Uri.parse(url)
            startActivity(buttonIntent)
        }
    }
}