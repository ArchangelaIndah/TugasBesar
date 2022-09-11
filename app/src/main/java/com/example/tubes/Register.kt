package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class Register : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputTanggalLahir: TextInputLayout
    private lateinit var inputNomorTelepon: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        inputEmail = findViewById(R.id.inputLayoutEmail)
        inputTanggalLahir = findViewById(R.id.inputLayoutTanggalLahir)
        inputNomorTelepon = findViewById(R.id.inputLayoutNomorTelepon)

        val btnDaftar: Button = findViewById(R.id.btnDaftar)
        val btnMasuk: Button = findViewById(R.id.btnMasuk)
        var akses= true

        btnMasuk.setOnClickListener{
            val moveLogin = Intent(this@Register, MainActivity::class.java)
            startActivity(moveLogin)
        }

        btnDaftar.setOnClickListener(View.OnClickListener {
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()
            val email: String = inputEmail.getEditText()?.getText().toString()
            val tanggalLahir: String = inputTanggalLahir.getEditText()?.getText().toString()
            val nomorTelepon: String = inputNomorTelepon.getEditText()?.getText().toString()

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