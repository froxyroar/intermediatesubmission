package com.dicoding.picodiploma.loginwithanimation.di

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig

object Injection {
    fun provideUserPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context)
    }

    fun provideApiService(): ApiService {
        return ApiConfig.createApiService()
    }
}
