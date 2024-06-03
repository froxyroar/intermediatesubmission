// di/Injection.kt
package com.dicoding.picodiploma.loginwithanimation.di

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.retrofit.ApiConfig

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context)
        val apiService = ApiConfig.createApiService()
        return UserRepository.getInstance(userPreference, apiService)
    }

    fun provideUserPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context)
    }

    fun provideApiService(): ApiService {
        return ApiConfig.createApiService()
    }
}
