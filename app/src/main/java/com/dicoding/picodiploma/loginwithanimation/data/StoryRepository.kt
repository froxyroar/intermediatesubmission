package com.dicoding.picodiploma.loginwithanimation.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.pref.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.view.main.paging.StoryPagingSource
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

    suspend fun getStoriesWithLocation(token: String): StoryResponse {
        return try {
            apiService.getStoriesWithLocation(token)
        } catch (e: HttpException) {
            Log.e("StoryRepository", "HTTP error: ${e.code()} ${e.message()}", e)
            throw e
        } catch (e: Exception) {
            Log.e("StoryRepository", "Error fetching stories with location", e)
            throw e
        }
    }


    suspend fun getToken(): String? {
        return userPreference.getToken()
    }

    fun getPagingStories(token: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { StoryPagingSource(apiService, token) }
    ).liveData

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
