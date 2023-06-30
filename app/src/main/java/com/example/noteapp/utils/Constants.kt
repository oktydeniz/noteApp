package com.example.noteapp.utils

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.EditText
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class Constants {

    companion object {
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

        fun getImgFromURI(intent:Intent, context: Context) : Bitmap? {
            return try {
                val stream = context.contentResolver.openInputStream(intent.data!!)
                BitmapFactory.decodeStream(stream)
            } catch (e: java.lang.Exception) {
                null
            }
        }

        fun getFileFromURI(context: Context, uri: Uri) : String {
            val filePath: String
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            if (cursor == null) {
                filePath = uri.path.toString()
            } else {
                cursor.moveToFirst()
                val index: Int = cursor.getColumnIndex("_data")
                filePath = cursor.getString(index)
                cursor.close()
            }
            return filePath
        }
    }
}