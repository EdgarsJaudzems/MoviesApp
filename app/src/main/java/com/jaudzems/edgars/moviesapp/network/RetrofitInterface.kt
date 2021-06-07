package com.jaudzems.edgars.moviesapp.network

import com.jaudzems.edgars.moviesapp.MovieData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {

    @GET("3/movie/popular?api_key=73619d549f33ccdf0116452a1f3f9427")
    fun getPopularMovieData(): Call<MovieData>

    @GET("3/movie/top_rated?api_key=73619d549f33ccdf0116452a1f3f9427")
    fun getTopRatedMovieData(): Call<MovieData>

    @GET("3/movie/upcoming?api_key=73619d549f33ccdf0116452a1f3f9427")
    fun getUpcomingMovieData(): Call<MovieData>

    @GET("3/movie/{movieId}?api_key=73619d549f33ccdf0116452a1f3f9427")
    fun getSingleMovieData(@Path("movieId") movieId: String): Call<SingleMoviedata>

    @GET("3/movie/{movieId}/videos?api_key=73619d549f33ccdf0116452a1f3f9427")
    fun getTrailer(@Path("movieId") movieId: String): Call<SingleMovieTrailer>

    @GET("3/search/movie")
    fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String) : Call<MovieData>

    @GET("3/movie/{movieId}/credits?api_key=73619d549f33ccdf0116452a1f3f9427")
    fun getCrew(@Path("movieId") movieId: String): Call<SingleMovieCrew>

    @GET("3/person/{actorId}?api_key=73619d549f33ccdf0116452a1f3f9427")
    fun getActorDetails(@Path("actorId") actorId: String): Call<ActorDetailData>
}
