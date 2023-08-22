package com.example.gallery.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gallery.utils.entity.Photo

@Database(
    entities = [Photo::class],
    version = 1
)
abstract class MainDB : RoomDatabase() {
}