package com.example.tubes.room

import androidx.room.*

@Dao
interface ReservasiDao {
    @Insert
    suspend fun addReservasi(reservasi: Reservasi)
    @Update
    suspend fun updateReservasi(reservasi: Reservasi)
    @Delete
    suspend fun deleteReservasi(reservasi: Reservasi)
    @Query("SELECT * FROM reservasi")
    suspend fun getReservasi(): List<Reservasi>
    @Query("SELECT * FROM reservasi WHERE id =:reservasiId")
    suspend fun getReservasiById(reservasiId: Int) : List<Reservasi>
}