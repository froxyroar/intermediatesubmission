package com.dicoding.picodiploma.loginwithanimation.data

import android.util.Log
import com.dicoding.picodiploma.loginwithanimation.data.pref.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import retrofit2.HttpException

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun getStories(): StoryResponse {
        val token = userPreference.getToken()
        Log.d("StoryRepository", "Fetching stories with token: $token")

        return if (token != null) {
            try {
                val response = apiService.getStories("Bearer $token")
                Log.d("StoryRepository", "Response: $response")
                response
            } catch (e: HttpException) {
                Log.e("StoryRepository", "HTTP error: ${e.code()} ${e.message()}", e)
                throw e
            } catch (e: Exception) {
                Log.e("StoryRepository", "Error fetching stories", e)
                throw e
            }
        } else {
            Log.e("StoryRepository", "Token is null")
            throw Exception("Token is null")
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(apiService, userPreference)
        }.also { instance = it }
    }
}
