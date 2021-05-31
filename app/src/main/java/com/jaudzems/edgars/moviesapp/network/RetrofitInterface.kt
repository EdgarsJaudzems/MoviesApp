package com.jaudzems.edgars.moviesapp.network

import com.jaudzems.edgars.moviesapp.MovieData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {

    @GET("3/movie/popular?api_key=73619d549f33ccdf0116452a1f3f9427&language=en-US&page=1")
    fun getPopularMovieData(): Call<MovieData>

    @GET("3/movie/{movieId}?api_key=73619d549f33ccdf0116452a1f3f9427")
    fun getSingleMovieData(@Path("movieId") movieId: String): Call<SingleMoviedata>

    @GET("3/movie/{movieId}/videos?api_key=73619d549f33ccdf0116452a1f3f9427")
    fun getTrailer(@Path("movieId") movieId: String): Call<SingleMovieTrailer>

//    @GET("3/search/movie?api_key=73619d549f33ccdf0116452a1f3f9427&query={search}")
//    fun searchMovies(@Path("searchTitle") searchTitle: String): Call<MovieData>
//    fun searchMovies(@Path("search") search: String) : Call<MovieData>

    @GET("3/search/movie")
    fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String) : Call<MovieData>


}
