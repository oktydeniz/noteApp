package com.example.noteapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    private val id:Int,

    @ColumnInfo(name = "title")
    private val title:String,

    @ColumnInfo(name = "date_time")
    private val dateTime:String,

    @ColumnInfo(name = "subtitle")
    private val subTitle:String,

    @ColumnInfo(name = "note_text")
    private val noteText:String,

    @ColumnInfo(name = "image_path")
    private val imagePath:String,

    @ColumnInfo(name = "color")
    private val color:String,

    @ColumnInfo(name = "web_link")
    private val webLink:String
    ): Serializable {

    fun getTitle(): String = this.title
    fun getDatetime(): String = this.dateTime
    fun getSubtitle(): String = this.subTitle
    fun getColor(): String = this.color
    fun getWebLink(): String = this.webLink
    fun getImagePath(): String = this.imagePath
    fun getNoteText(): String = this.noteText
    fun getId(): Int = this.id

    public fun getTitleWithDate(): String {
        return "${getTitle()} : ${getDatetime()}"
    }
}