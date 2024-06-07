package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                if (response != null && response.error == false && response.loginResult != null) {
                    _loginResult.postValue(true)
                    response.loginResult.token?.let { token ->
                        repository.saveSession(UserModel(email, token))
                    }
                } else {
                    _loginResult.postValue(false)
                }
            } catch (e: Exception) {
                _loginResult.postValue(false)
            }
        }
    }
}