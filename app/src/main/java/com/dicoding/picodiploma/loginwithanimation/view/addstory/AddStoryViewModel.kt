package com.dicoding.picodiploma.loginwithanimation.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.pref.AddStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddStoryViewModel : ViewModel() {
    private val _uploadResult = MutableLiveData<AddStoryResponse>()
    val uploadResult: LiveData<AddStoryResponse> = _uploadResult

    suspend fun uploadStory(token: String, file: File, description: String) {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val filePart = MultipartBody.Part.createFormData("photo", file.name, requestFile)
        val descriptionPart = MultipartBody.Part.createFormData("description", description)

        try {
            val response = ApiConfig.createApiService().uploadStory(filePart, descriptionPart)
            _uploadResult.postValue(response)
        } catch (e: Exception) {
            _uploadResult.postValue(AddStoryResponse(error = true, message = e.message))
        }
    }
}