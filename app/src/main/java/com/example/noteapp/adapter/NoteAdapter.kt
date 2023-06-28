package com.example.noteapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.ItemContainerNoteBinding
import com.example.noteapp.models.Note

class NoteAdapter(private val notes: List<Note>) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private lateinit var context: Context
    inner class ViewHolder(var view: ItemContainerNoteBinding): RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = ItemContainerNoteBinding.inflate(inflater)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notes[position]
        holder.view.note = item
        holder.view.executePendingBindings()
    }
}