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
    val movieList: List<Result>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return movieList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var title = itemView.findViewById<TextView>(R.id.movie_title)
//        var releaseDate = itemView.findViewById<TextView>(R.id.movie_release_date)
        var poster = itemView.findViewById<ImageView>(R.id.movie_image)

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(movieList[position])
            }
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        val movie = movieList[position]

        holder.title.text = movie.title
//        holder.releaseDate.text = movie.release_date
        Glide.with(holder.itemView)
            .load(IMAGE_BASE + movie.poster_path)
            .into(holder.poster)
    }

    interface OnItemClickListener {
        fun onItemClick(movie: Result)
    }

}
