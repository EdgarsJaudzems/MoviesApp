package com.jaudzems.edgars.moviesapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.GenericTransitionOptions
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
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

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
        val actorMovieBackPoster = IMAGE_BASE + movieBackPoster
        if (movieBackPoster == "") {
            binding.actorBackPoster.setImageResource(R.drawable.dummy)
        } else {
            Glide.with(this@ActorProfileActivity)
                .load(actorMovieBackPoster)
                .into(binding.actorBackPoster)
        }
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
                val topToBottomAnimation = AnimationUtils.loadAnimation(
                    this@ActorProfileActivity,
                    R.anim.top_to_bottom_actor
                )
                binding.actorNameLastname.startAnimation(topToBottomAnimation)

                //Actors Image Load
                val actorProfilePhoto = IMAGE_BASE + actorData.profile_path

                if (actorData.profile_path == null) {
                    binding.actorImage.setImageResource(R.drawable.dummy_profile_image)
                } else {
                    Glide.with(this@ActorProfileActivity)
                        .load(actorProfilePhoto)
                        .transition(GenericTransitionOptions.with(R.anim.glide_image))
                        .into(binding.actorImage)
                }

                //Birthday
                if (actorData.birthday != null) {
                    binding.actorBirthdayText.text = actorData.birthday
                } else {
                    binding.actorBirthdayText.text = "-"
                }

                //Birth Place
                if (actorData.place_of_birth != null) {
                    binding.actorPlaceOfBirthText.text = actorData.place_of_birth
                }

                //Biography
                if (actorData.biography == "") {
                    binding.actorBiographyText.text = "No information"
                } else {
                    binding.actorBiographyText.text = actorData.biography
                    val topToBottomAnimation = AnimationUtils.loadAnimation(
                        this@ActorProfileActivity,
                        R.anim.top_to_bottom_actor_2
                    )
                    binding.actorBiographyText.startAnimation(topToBottomAnimation)
                }

                //Button to movie website actors page
                val MOVIE_BASE = "https://www.themoviedb.org/person/"
                val buttonAnimation = AnimationUtils.loadAnimation(this@ActorProfileActivity, R.anim.button_anim)

                binding.actorButton.setOnClickListener {
                    binding.actorButton.startAnimation(buttonAnimation)
                    val url = MOVIE_BASE + actorId

                    val buttonIntent = Intent(Intent.ACTION_VIEW)
                    buttonIntent.data = Uri.parse(url)
                    startActivity(buttonIntent)
                    activityAnimation()
                }
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

    fun activityAnimation() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}