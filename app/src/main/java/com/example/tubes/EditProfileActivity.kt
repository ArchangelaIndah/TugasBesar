package com.example.tubes

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tubes.databinding.ActivityEditProfileBinding
import com.example.tubes.room.User
import com.example.tubes.room.UserDB
import com.google.android.material.snackbar.Snackbar
import java.util.*


class EditProfileActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    var binding: ActivityEditProfileBinding? = null
    var sharedPreferences: SharedPreferences? = null
    private lateinit var editProfileLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")
        binding?.etNama?.setText(db?.userDao()?.getUser(id!!.toInt())?.nama)
        binding?.etEmail?.setText(db?.userDao()?.getUser(id!!.toInt())?.email)
        binding?.etPhoneNumber?.setText(db?.userDao()?.getUser(id!!.toInt())?.noTelp)
        binding?.etTglLahir?.setText(db?.userDao()?.getUser(id!!.toInt())?.tglLahir)


        binding?.btnSave?.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, MainActivity::class.java)

            val Name: String = binding?.ilName?.editText?.getText().toString()
            val NoTelp: String = binding?.ilPhoneNumber?.editText?.getText().toString()
            val Email: String = binding?.ilEmail?.editText?.getText().toString()
            val BirthDate: String = binding?.etTglLahir?.getText().toString()

            var checkSave = true

            if (Name.isEmpty()) {
                binding?.ilName?.setError("Name must be filled with text")
                checkSave = false
            }

            if (NoTelp.isEmpty()) {
                binding?.ilPhoneNumber?.setError("Phone Number must be filled with text")
                checkSave = false
            }

            if (Email.isEmpty()) {
                binding?.ilEmail?.setError("E-mail must be filled with text")
                checkSave = false
            }

            if (!Email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"))) {
                binding?.ilEmail?.setError("Email tidak valid")
                checkSave = false
            }

            if (BirthDate.isEmpty()) {
                binding?.etEmail?.setError("Birth Date must be filled with text")
                checkSave = false
            }

            if (checkSave == true) {
                setupListener()
                Toast.makeText(
                    applicationContext,
                    "Your Profile Changed",
                    Toast.LENGTH_SHORT
                ).show()
                val moveMenu = Intent(this, Menu::class.java)
                startActivity(moveMenu)
            } else {
                return@OnClickListener
            }
        })
    }


    private fun setupListener() {
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")

        db.userDao().updateUser(
            User(
                id!!.toInt(),
                binding?.etNama?.getText().toString(),
                binding?.etPhoneNumber?.text.toString(),
                binding?.etEmail?.text.toString(),
                binding?.etTglLahir?.text.toString(),
                db?.userDao()?.getUser(id!!.toInt())?.password.toString()
            )
        )
        finish()
    }

}