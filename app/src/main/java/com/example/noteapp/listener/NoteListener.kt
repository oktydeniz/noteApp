package com.example.noteapp.listener

import com.example.noteapp.models.Note

interface NoteListener {
    fun onNoteClicked(note: Note, position:Int)
}