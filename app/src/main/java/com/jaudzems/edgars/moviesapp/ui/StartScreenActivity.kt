package com.jaudzems.edgars.moviesapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jaudzems.edgars.moviesapp.R

class StartScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)

        val startScreenLogo = findViewById<ImageView>(R.id.tmdb_logo_start)
        val author = findViewById<TextView>(R.id.author)
        val madeBy = findViewById<TextView>(R.id.madeBy)

        author.alpha = 0f
        madeBy.alpha = 0f
        author.animate().setDuration(2000).alpha(1f)
        madeBy.animate().setDuration(2000).alpha(1f)

        startScreenLogo.alpha = 0f
        startScreenLogo.animate().setDuration(1000).alpha(1f).rotationYBy(360f).withEndAction {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}