package com.example.tubes.room

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Reservasi (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nama: String,
    val noplat: String,
    val jeniskendaraan: String,
    val keluhan: String
){

}