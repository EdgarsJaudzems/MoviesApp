package com.jaudzems.edgars.moviesapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.jaudzems.edgars.moviesapp.R
import com.jaudzems.edgars.moviesapp.databinding.ActivityActorProfileBinding
import com.jaudzems.edgars.moviesapp.network.ActorDetailData
import com.jaudzems.edgars.moviesapp.network.RetrofitInstance
import com.jaudzems.edgars.moviesapp.network.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActorProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActorProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actorId = intent.getIntExtra("actorId", 578701).toString()


        setupHeaderToolbar()
        getActorData(actorId)
        loadBackPoster()

    }

    private fun loadBackPoster() {
        val movieBackPoster = intent.getStringExtra("backPoster")
        Glide.with(this@ActorProfileActivity)
            .load(movieBackPoster)
            .into(binding.actorBackPoster)
    }

    private fun getActorData(actorId: String) {
        val retrofitInstance =
            RetrofitInstance.getRetrofitInstance().create(RetrofitInterface::class.java)
        val call = retrofitInstance.getActorDetails(actorId)

        call.enqueue(object : Callback<ActorDetailData?> {
            override fun onResponse(
                call: Call<ActorDetailData?>,
                response: Response<ActorDetailData?>
            ) {
                val actorData = response.body()!!

                //Name
                binding.actorNameLastname.text = actorData.name

                //Actors Image Load
                val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"
                val actorProfilePhoto = IMAGE_BASE + actorData.profile_path

                Glide.with(this@ActorProfileActivity)
                    .load(actorProfilePhoto)
                    .into(binding.actorImage)

                //Birthday
                binding.actorBirthdayText.text = actorData.birthday

                //Deathday
                if (actorData.deathday != null) {
                    binding.actorDeathdayText.text = actorData.deathday.toString()
                } else {
                    binding.actorDeathday.visibility = View.GONE
                    binding.actorDeathdayText.visibility = View.GONE
                }

                //Place
                binding.actorPlaceOfBirthText.text = actorData.place_of_birth

                //Biography
                binding.actorBiographyText.text = actorData.biography

            }

            override fun onFailure(call: Call<ActorDetailData?>, t: Throwable) {
                Log.d("ActorProfileActivity", "onFailure" + t.message)
            }
        })
    }

    private fun setupHeaderToolbar() {
        supportActionBar!!.title = intent.getStringExtra("actorName")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}