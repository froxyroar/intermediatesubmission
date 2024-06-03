// data/retrofit/ApiConfig.kt
package com.dicoding.picodiploma.loginwithanimation.retrofit

import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

    fun createApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
