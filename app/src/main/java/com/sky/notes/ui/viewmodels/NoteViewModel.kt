package com.sky.notes.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.notes.model.NoteRequest
import com.sky.notes.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel@Inject constructor(private val noteRepository: NoteRepository):ViewModel() {
    val notesLiveData get() = noteRepository.noteLiveData
    val statusLiveData get() = noteRepository.statusLiveData

    fun getAllNotes(){
        val job = viewModelScope.async {
            noteRepository.getNotes()
        }
        job.onAwait
    }

    fun createNote(noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.createNote(noteRequest)
        }

    }
    fun updateNote(noteId: String,noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.updateNote(noteId,noteRequest)
        }

    }
    fun deleteNote(noteId: String){
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }

    }
}