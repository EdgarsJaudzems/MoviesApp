package com.jaudzems.edgars.moviesapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(
    val context: Context,
    val movieList: List<Result>
): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        var title = itemView.findViewById<TextView>(R.id.movie_title)
        var releaseDate = itemView.findViewById<TextView>(R.id.movie_release_date)
        var poster = itemView.findViewById<ImageView>(R.id.movie_image)

        fun bind(movie: Result) {
            title.text = movie.title
            releaseDate.text = movie.release_date
            Glide.with(itemView).load(IMAGE_BASE + movie.poster_path).into(poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.movie_item,parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(movieList[position])
//        holder.title.text = movieList[position].title
//        holder.releaseDate.text = movieList[position].release_date
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}