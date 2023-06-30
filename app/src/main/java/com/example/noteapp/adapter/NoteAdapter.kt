package com.example.noteapp.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
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

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notes[position]
        holder.view.note = item
        holder.view.executePendingBindings()

        if (item.imagePath != "") {
            holder.view.roundedImageNote.setImageBitmap(BitmapFactory.decodeFile(item.imagePath))
            holder.view.roundedImageNote.visibility = View.VISIBLE
        } else {
            holder.view.roundedImageNote.visibility = View.GONE
        }

        val gradientDrawable = holder.view.layoutNote.background as GradientDrawable
        val color = if (item.color != "") Color.parseColor(item.color)
            else Color.parseColor("#333333")
        gradientDrawable.setColor(color)
    }
}