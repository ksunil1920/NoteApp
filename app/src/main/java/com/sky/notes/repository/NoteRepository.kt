package com.sky.notes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sky.notes.api.NoteApi
import com.sky.notes.model.NoteRequest
import com.sky.notes.model.NoteResponse
import com.sky.notes.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteApi: NoteApi) {
    private var _noteMutableLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val noteLiveData: LiveData<NetworkResult<List<NoteResponse>>>
        get() = _noteMutableLiveData

    private var _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getNotes() {
        _noteMutableLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.getNotes()
        if (response.isSuccessful && response.body() != null) {
            _noteMutableLiveData.postValue(NetworkResult.Success(response.body()!!))
        }  else {
            _noteMutableLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val  response = noteApi.createNote(noteRequest)
        handleResponse(response,"Note created")
    }


    suspend fun updateNote(noteId: String, noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
       val response = noteApi.updateNote(noteId, noteRequest)
        handleResponse(response,"Note updated")
    }

    suspend fun deleteNote(noteId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
       val response = noteApi.deleteNote(noteId)
        handleResponse(response,"Note deleted")
    }

       private fun handleResponse(response: Response<NoteResponse>, message:String) {
           if (response.isSuccessful && response.body() != null) {
               _statusLiveData.postValue(NetworkResult.Success(message))
           } else  {
               _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
           }
       }
}