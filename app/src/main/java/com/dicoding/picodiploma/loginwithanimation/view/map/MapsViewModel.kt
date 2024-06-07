package com.dicoding.picodiploma.loginwithanimation.view.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStoriesWithLocation(token: String) = liveData(Dispatchers.IO) {
        try {
            val response = storyRepository.getStoriesWithLocation(token)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getToken(): String? = runBlocking {
        storyRepository.getToken()
    }
}
