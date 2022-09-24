package com.example.tubes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tubes.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    val db by lazy { UserDB (this) }
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    lateinit var mBundle: Bundle
    var sharedPreferences: SharedPreferences? = null

    var tUsername : String = ""
    var tPassword : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        setTitle("User Login")

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)
        val btnMasuk: Button = findViewById(R.id.btnMasuk)
        val btnDaftar: Button = findViewById(R.id.btnDaftar)

        if(intent.getBundleExtra("register")!=null){
            mBundle = intent.getBundleExtra("register")!!
            tUsername = mBundle.getString("Username").toString()
            tPassword = mBundle.getString("Password").toString()
            inputUsername.getEditText()?.setText(tUsername)
            inputPassword.getEditText()?.setText(tPassword)

        }

        btnMasuk.setOnClickListener{
            inputUsername.getEditText()?.setText("")
            inputPassword.getEditText()?.setText("")

            Snackbar.make(mainLayout,"Text Cleared Success", Snackbar.LENGTH_LONG).show()
        }

        btnMasuk.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            var username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()


            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }
            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if(username == "admin" && password == "admin")
                checkLogin = true

            val user = db.userDao().getUser(username, password)
            if(user != null){

                checkLogin = true
                val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putString("id",user.id.toString())
                editor.apply()
            }

            if(intent.getBundleExtra("register")!=null){
                if(username== tUsername && password == tPassword)
                    checkLogin= true
            }

            if(!checkLogin)
                return@OnClickListener


            val moveHome = Intent(this, Menu::class.java)
            startActivity(moveHome)
        })

        btnDaftar.setOnClickListener{
            val moveRegis = Intent(this, Register::class.java)
            startActivity(moveRegis)
        }
    }
}