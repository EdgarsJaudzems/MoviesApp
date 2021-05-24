package com.jaudzems.edgars.moviesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jaudzems.edgars.moviesapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}