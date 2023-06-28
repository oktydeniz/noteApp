package com.example.noteapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.database.NotesDatabase
import com.example.noteapp.models.Note
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application): BaseViewModel(application) {

    var isSuccess = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()
    var notes = MutableLiveData<List<Note>>()

    fun fetchNotes() {
        isLoading.value = true
        launch {
            try {
                val dao = NotesDatabase.invoke(getApplication()).noteDao()
                notes.value = dao.getAllNotes()
                isLoading.value = false
                isSuccess.value = true
            } catch (e: java.lang.Exception) {
                isSuccess.value = false
                isLoading.value = false
            }
        }

    }

}