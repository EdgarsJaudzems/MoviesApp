package com.jaudzems.edgars.moviesapp

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("3/movie/popular?api_key=73619d549f33ccdf0116452a1f3f9427&language=en-US&page=1")
    fun getData(): Call<MovieData>
}