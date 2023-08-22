package com.example.gallery.utils.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(
    @PrimaryKey (autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val image: String,
    val category: String,
    val isFav: Boolean
)
