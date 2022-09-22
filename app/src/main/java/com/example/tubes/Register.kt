package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tubes.databinding.ActivityRegisterBinding
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
                inputUsername.setError("Username must be filled with text")
                akses=false
            }

            if(password.isEmpty()) {
                inputPassword.setError("Password must be filled with text")
                akses=false
            }

            if(email.isEmpty()) {
                inputEmail.setError("Email must be filled with text")
                akses=false
            }

            if(tanggalLahir.isEmpty()) {
                inputTanggalLahir.setError("Tanggal Lahir must be filled with text")
                akses=false
            }

            if(nomorTelepon.isEmpty()) {
                inputNomorTelepon.setError("No Telepon must be filled with text")
                akses=false
            }

            if(inputUsername.getEditText()?.getText()==null){
                inputUsername.getEditText()?.setText("")
            }

            if(inputPassword.getEditText()?.getText()==null){
                inputPassword.getEditText()?.setText("")
            }


            if(akses==true){
                val moveHome = Intent(this@Register, MainActivity::class.java)

                mBundle.putString("Username",inputUsername.getEditText()?.getText().toString())
                mBundle.putString("Password",inputPassword.getEditText()?.getText().toString())
                moveHome.putExtra("register", mBundle)
                startActivity(moveHome)
            }

        })
    }
}