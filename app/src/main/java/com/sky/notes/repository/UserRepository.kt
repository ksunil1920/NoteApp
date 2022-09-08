package com.sky.notes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.sky.notes.api.UserAPI
import com.sky.notes.model.UserRequest
import com.sky.notes.model.UserResponse
import com.sky.notes.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository@Inject constructor(private val userAPI: UserAPI) {
    private var _userResponseMutableLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseMutableLiveData

    suspend fun registerUser(userRequest: UserRequest) {
             _userResponseMutableLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signup(userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseMutableLiveData.postValue(NetworkResult.Loading())
        val  response = userAPI.signin(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseMutableLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseMutableLiveData.postValue(NetworkResult.Error(jsonObj.getString("message")))
        } else {
            _userResponseMutableLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}