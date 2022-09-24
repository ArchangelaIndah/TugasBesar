package com.example.tubes.room

import androidx.room.*

@Dao
interface ReservasiDao {
    @Insert
    fun addReservasi(reservasi: Reservasi)
    @Update
    fun updateReservasi(reservasi: Reservasi)
    @Delete
    fun deleteReservasi(reservasi: Reservasi)
    @Query("SELECT * FROM reservasi")
    fun getReservasi(): List<Reservasi>
    @Query("SELECT * FROM reservasi WHERE id =:reservasiId")
    suspend fun getReservasiById(reservasiId: Int) : List<Reservasi>
}