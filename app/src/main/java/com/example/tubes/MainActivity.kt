package com.example.tubes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes.api.ProfilApi
import com.example.tubes.api.ReservasiApi
import com.example.tubes.models.Profil
import com.example.tubes.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.lang.Math.log
import java.nio.charset.StandardCharsets
import java.util.logging.Logger
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger.addLogAdapter
import com.orhanobut.logger.Logger.log
import com.orhanobut.logger.PrettyFormatStrategy
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    lateinit var mBundle: Bundle
    var sharedPreferences: SharedPreferences? = null

    var tUsername : String = ""
    var tPassword : String = ""
    var checkLogin = false

    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        queue =  Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        setTitle("User Login")

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)
        val btnMasuk: Button = findViewById(R.id.btnMasuk)
        val btnDaftar: Button = findViewById(R.id.btnDaftar)

        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(1)
            .methodOffset(5)
            .build()
        com.orhanobut.logger.Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        Timber.plant(object : Timber.DebugTree(){
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?){
                com.orhanobut.logger.Logger.log(priority,"-$tag", message, t)
            }
        })

        Timber.d("onCreate Inside MainActivity")

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


            if(intent.getBundleExtra("register")!=null){
                if(username== tUsername && password == tPassword)
                    checkLogin= true
            }



            loginUser()

        })

        btnDaftar.setOnClickListener{
            val moveRegis = Intent(this, Register::class.java)
            startActivity(moveRegis)
        }
    }

    private fun loginUser(){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, ProfilApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val allProfil : Array<Profil> = gson.fromJson(jsonData.toString(),Array<Profil>::class.java)

                for (i in allProfil){
                    if(i.username == inputUsername.getEditText()?.getText().toString() && i.password == inputPassword.getEditText()?.getText().toString()){
                        val editor : SharedPreferences.Editor = sharedPreferences!!.edit()
                        editor.putString("id",i.id.toString())
                        editor.apply()
                        println(i.id.toString())
                        checkLogin = true
                        break;

                    }

                }

                if(checkLogin == true){

                    val moveHome = Intent(this, Menu::class.java)
                    startActivity(moveHome)
                }else{
                    inputUsername.setError("Username Salah")
                    inputPassword.setError("Password Salah")
                }

            }, Response.ErrorListener { error ->
                //srReservasi!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        this,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    FancyToast.makeText(this,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,true).show()
                }catch (e: Exception){
                    FancyToast.makeText(this, e.message,
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,true).show()
                    //Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }
}