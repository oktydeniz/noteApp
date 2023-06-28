package com.example.noteapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.database.NotesDatabase
import com.example.noteapp.models.Note
import kotlinx.coroutines.launch

class CreateNoteViewModel(application: Application) : BaseViewModel(application = application ) {

    var isSuccess = MutableLiveData<Boolean>()
    var isLoading = MutableLiveData<Boolean>()

    fun saveNote(note: Note) {
        isLoading.value = true
        launch {
            try {
                val dao = NotesDatabase.invoke(getApplication()).noteDao()
                dao.insertNote(note)
                isLoading.value = false
                isSuccess.value = true
            } catch (e: java.lang.Exception) {
                isSuccess.value = false
                isLoading.value = false
            }
        }
    }
}