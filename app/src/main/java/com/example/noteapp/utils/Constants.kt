package com.example.noteapp.utils

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.example.noteapp.R
import java.text.SimpleDateFormat
import java.util.*

class Constants {

    companion object {
         var REQUEST_CODE_ADD_NOTE = 1


        fun getCurrentDate(): String =
            SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(
                Date()
            )

        fun isNullEditText(view: EditText): Boolean = view.text.toString().trim().isEmpty()

        fun showToastS(context: Context, str:String) = Toast.makeText(
            context,
            str,
            Toast.LENGTH_SHORT
        ).show()

        fun showToastL(context: Context, str: String) = Toast.makeText(
            context,
            str,
            Toast.LENGTH_LONG
        ).show()
    }
}