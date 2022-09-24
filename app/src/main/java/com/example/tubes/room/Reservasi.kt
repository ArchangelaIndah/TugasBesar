package com.example.tubes.room

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
class Reservasi (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nama: String,
    val noPlat: String,
    val jenisKendaraan: String,
    val keluhan: String
)