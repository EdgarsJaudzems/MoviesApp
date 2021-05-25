package com.jaudzems.edgars.moviesapp

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
            .load(movieBackPoster.toString())
            .into(binding.movieBackPoster)

//        binding.movieBackPoster.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})

        Glide.with(this)
            .load(moviePoster.toString())
            .into(binding.movieFrontPoster)
    }
}