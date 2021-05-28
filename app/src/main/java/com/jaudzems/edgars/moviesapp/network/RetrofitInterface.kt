package com.jaudzems.edgars.moviesapp.network

import com.jaudzems.edgars.moviesapp.MovieData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitInterface {

    // https://api.themoviedb.org/3/movie/popular?api_key=9ca86244732b6e729d3bea64447a56d7&language=en-US
    @GET("3/movie/popular?api_key=73619d549f33ccdf0116452a1f3f9427&language=en-US&page=1")
    fun getPopularMovieData(): Call<MovieData>

    // https://api.themoviedb.org/3/movie/{movie_id}?api_key=9ca86244732b6e729d3bea64447a56d7&language=en-US
    @GET("3/movie/{movieId}?api_key=73619d549f33ccdf0116452a1f3f9427")
    fun getSingleMovieData(@Path("movieId") movieId: String): Call<SingleMoviedata>

//    https://www.youtube.com/watch?v=zMPS9JO0p6w
//    https://api.themoviedb.org/3/movie/823855/videos?api_key=73619d549f33ccdf0116452a1f3f9427

    @GET("/3/movie/{movieId}/videos?api_key=73619d549f33ccdf0116452a1f3f9427")
    fun getTrailer(@Path("movieId") movieId: String): Call<SingleMovieTrailer>
}
