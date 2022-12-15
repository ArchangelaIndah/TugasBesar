package com.example.tubes.room

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Pemesanan (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val namaBarang: String,
    val jumlah: String,
    val alamatBengkel: String,
)