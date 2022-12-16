package com.example.tubes.room

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Review (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val review: String,
    val saran: String,
)