package com.sky.notes.ui.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.notes.model.UserRequest
import com.sky.notes.model.UserResponse
import com.sky.notes.repository.UserRepository
import com.sky.notes.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveDta: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            val response = userRepository.registerUser(userRequest)

        }

    }

     fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            val response = userRepository.loginUser(userRequest)
        }

    }

    fun validateUserCredentials(
        emailAddress: String,
        passWord: String,
        userName: String,
        isLogin:Boolean
    ): Pair<Boolean, String> {
        var result = Pair(false, "")
        if (emailAddress.isEmpty() || passWord.isEmpty() || (!isLogin && userName.isEmpty())) {
            result = Pair(false, "Please provide valid credentials")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            result = Pair(false, "Please provide valid email format")
        } else if (passWord.length < 5) {
            result = Pair(false, "Your password length is not proper")
        }
        return result
    }
}