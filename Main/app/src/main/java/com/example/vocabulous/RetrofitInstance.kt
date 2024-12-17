package com.example.vocabulous

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance  {
    private const val API_RUL = "https://api.dictionaryapi.dev/api/v2/entries/"
    private fun getInstance() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(API_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val dictionaryApi : DictionaryApi = getInstance().create(DictionaryApi::class.java)
}