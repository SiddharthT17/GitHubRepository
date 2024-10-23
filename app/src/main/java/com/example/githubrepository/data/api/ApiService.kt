package com.example.githubrepository.data.api

import com.example.githubrepository.data.model.ResponseRepos
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private val BASE_URL = "https://api.github.com/"

interface ApiService {
    @GET("users/{username}/repos")
    suspend fun getRepos(@Path("username") username: String) : List<ResponseRepos>
}

object RetrofitInstance {
    val retrofitApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
