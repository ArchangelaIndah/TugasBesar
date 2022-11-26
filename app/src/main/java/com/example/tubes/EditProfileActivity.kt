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
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes.api.ProfilApi
import com.example.tubes.api.ReservasiApi
import com.example.tubes.databinding.ActivityEditProfileBinding
import com.example.tubes.models.Profil
import com.example.tubes.room.User
import com.example.tubes.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_edit_reservasi.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*


class EditProfileActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    var binding: ActivityEditProfileBinding? = null
    var sharedPreferences: SharedPreferences? = null
    private lateinit var editProfileLayout: FrameLayout
    private var queue: RequestQueue? = null
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        queue =  Volley.newRequestQueue(this)

        setContentView(binding?.root)

        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")

        getProfilById(id!!.toInt())

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


            if (BirthDate.isEmpty()) {
                binding?.etTglLahir?.setError("Birth Date must be filled with text")
                checkSave = false
            }

            if (checkSave == true) {
                editProfil(id!!.toInt())
//                Toast.makeText(
//                    applicationContext,
//                    "Your Profile Changed",
//                    Toast.LENGTH_SHORT
//                ).show()
                FancyToast.makeText(applicationContext,
                    "Your Profile Changed",
                    FancyToast.LENGTH_LONG,
                    FancyToast.SUCCESS,true).show()
                val moveMenu = Intent(this, Menu::class.java)
                startActivity(moveMenu)
            } else {
                return@OnClickListener
            }
        })
    }

    private fun getProfilById(id: Int){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, ProfilApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val profil : Array<Profil> = gson.fromJson(jsonData.toString(),Array<Profil>::class.java)

                binding?.etNama?.setText(profil[0].username)
                binding?.etEmail?.setText(profil[0].email)
                binding?.etPhoneNumber?.setText(profil[0].notelepon)
                binding?.etTglLahir?.setText(profil[0].tanggallhr)
                password = profil[0].password

                if(!profil.isEmpty())
                    FancyToast.makeText(this, "Data Berhasil Diambil!",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,true).show()
                    //Toast.makeText(this, "Data Berhasil Diambil!", Toast.LENGTH_SHORT ).show()
                else
                    FancyToast.makeText(this, "Data Kosong!",
                        FancyToast.LENGTH_LONG,
                        FancyToast.ERROR,true).show()
                    //Toast.makeText(this, "Data Kosong!", Toast.LENGTH_SHORT).show()
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
                        FancyToast.LENGTH_LONG,
                        FancyToast.ERROR,true).show()
                }catch (e: Exception){
                    FancyToast.makeText(this, e.message,
                        FancyToast.LENGTH_LONG,
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

    private fun editProfil(id: Int){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, ProfilApi.UPDATE_URL+id , Response.Listener { response ->
                finish()
                FancyToast.makeText(this, "Berhsasil Edit Data",
                    FancyToast.LENGTH_LONG,
                    FancyToast.SUCCESS,true).show()
                //Toast.makeText(this, "Berhsasil Edit Data", Toast.LENGTH_SHORT ).show()
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
                        FancyToast.LENGTH_LONG,
                        FancyToast.ERROR,true).show()
                }catch (e: Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = binding?.etNama?.text.toString()
                params["password"] = password
                params["email"] = binding?.etEmail?.text.toString()
                params["tanggallhr"] = binding?.etTglLahir?.text.toString()
                params["notelepon"] = binding?.etPhoneNumber?.text.toString()
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    private fun setupListener() {
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")

        db.userDao().updateUser(
            User(
                id!!.toInt(),
                binding?.etNama?.getText().toString(),
                binding?.etEmail?.text.toString(),
                binding?.etTglLahir?.text.toString(),
                binding?.etPhoneNumber?.text.toString(),
                db?.userDao()?.getUser(id!!.toInt())?.password.toString()
            )
        )
        finish()
    }

}