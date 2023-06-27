package com.example.noteapp.database

import android.content.Context
import androidx.room.*
import com.example.noteapp.dao.NoteDao
import com.example.noteapp.models.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {

    companion object {
        @Volatile
        private var instance: NotesDatabase? = null

        private val lock =  Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: createDB(context).also {
                instance = it
            }
        }

        private fun createDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NotesDatabase::class.java,
                "note_db"
            ).build()

        }

    public abstract fun noteDao () : NoteDao
}