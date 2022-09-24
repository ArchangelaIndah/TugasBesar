package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tubes.databinding.ActivityRegisterBinding
import com.example.tubes.room.User
import com.example.tubes.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class Register : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputTanggalLahir: TextInputLayout
    private lateinit var inputNomorTelepon: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val db by lazy { UserDB(this) }
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var akses= true

        binding.btnMasuk.setOnClickListener{
            val moveLogin = Intent(this@Register, MainActivity::class.java)
            startActivity(moveLogin)
        }

        binding.btnDaftar.setOnClickListener(View.OnClickListener {

            val username: String = binding.inputLayoutUsername.getEditText()?.getText().toString()
            val password: String = binding.inputLayoutPassword.getEditText()?.getText().toString()
            val email: String = binding.inputLayoutEmail.getEditText()?.getText().toString()
            val tanggalLahir: String = binding.inputLayoutTanggalLahir.getEditText()?.getText().toString()
            val nomorTelepon: String = binding.inputLayoutNomorTelepon.getEditText()?.getText().toString()

            val intent = Intent (this, MainActivity::class.java)
            val mBundle = Bundle()

            if(username.isEmpty()) {
                binding.inputLayoutUsername.setError("Username must be filled with text")
                akses=false
            }

            if(password.isEmpty()) {
                binding.inputLayoutPassword.setError("Password must be filled with text")
                akses=false
            }

            if(email.isEmpty()) {
                binding.inputLayoutEmail.setError("Email must be filled with text")
                akses=false
            }

            if(tanggalLahir.isEmpty()) {
                binding.inputLayoutTanggalLahir.setError("Tanggal Lahir must be filled with text")
                akses=false
            }

            if(nomorTelepon.isEmpty()) {
                binding.inputLayoutNomorTelepon.setError("No Telepon must be filled with text")
                akses=false
            }

            if(binding.inputLayoutUsername.getEditText()?.getText()==null){
                binding.inputLayoutUsername.getEditText()?.setText("")
            }

            if(binding.inputLayoutPassword.getEditText()?.getText()==null){
                binding.inputLayoutPassword.getEditText()?.setText("")
            }


            if(akses==true){
                db.userDao().addUser(User(0,username,email,tanggalLahir,nomorTelepon,password))
                println(db.userDao().getUsers())
                val moveHome = Intent(this@Register, MainActivity::class.java)

                mBundle.putString("Username",binding.inputLayoutUsername.getEditText()?.getText().toString())
                mBundle.putString("Password",binding.inputLayoutPassword.getEditText()?.getText().toString())
                moveHome.putExtra("register", mBundle)
                startActivity(moveHome)
            }

        })
    }
}