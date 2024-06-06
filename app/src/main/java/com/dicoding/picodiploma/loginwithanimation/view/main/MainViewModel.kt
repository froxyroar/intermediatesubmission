package com.dicoding.picodiploma.loginwithanimation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun getStories() = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = storyRepository.getStories()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            Log.e("MainViewModel", "HTTP error: ${e.code()} ${e.message()}", e)
            emit(Result.Error("HTTP error: ${e.code()} ${e.message()}"))
        } catch (e: IOException) {
            Log.e("MainViewModel", "Network error: ${e.message}", e)
            emit(Result.Error("Network error: ${e.message}"))
        } catch (e: Exception) {
            Log.e("MainViewModel", "Unknown error: ${e.message}", e)
            emit(Result.Error("Unknown error: ${e.message}"))
        }
    }
}
