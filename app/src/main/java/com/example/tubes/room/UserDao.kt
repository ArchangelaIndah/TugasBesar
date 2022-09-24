package com.example.tubes.room
import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun addUser(user: User)
    @Update
    fun updateUser(user: User)
    @Delete
    fun deleteUser(user: User)
    @Query("SELECT * FROM user")
    fun getUsers() : List<User>
    @Query("SELECT * FROM user WHERE id =:user_id")
    fun getUser(user_id: Int) : User
    @Query("SELECT * FROM user WHERE nama = :user_name and password = :user_password")
    fun getUser(user_name: String, user_password: String) : User
}