package com.example.tubes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes.api.ProfilApi
import com.example.tubes.databinding.ActivityRegisterBinding
import com.example.tubes.models.Profil
import com.example.tubes.room.User
import com.example.tubes.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class Register : AppCompatActivity() {
    private val notificationId = 101
    private val CHANNEL_ID = "channel_notification"

    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputTanggalLahir: TextInputLayout
    private lateinit var inputNomorTelepon: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var binding : ActivityRegisterBinding

    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val db by lazy { UserDB(this) }
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        queue = Volley.newRequestQueue(this)
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

//            if(username.isEmpty()) {
//                binding.inputLayoutUsername.setError("Username must be filled with text")
//                akses=false
//            }
//
//            if(password.isEmpty()) {
//                binding.inputLayoutPassword.setError("Password must be filled with text")
//                akses=false
//            }
//
//            if(email.isEmpty()) {
//                binding.inputLayoutEmail.setError("Email must be filled with text")
//                akses=false
//            }
//
//            if(tanggalLahir.isEmpty()) {
//                binding.inputLayoutTanggalLahir.setError("Tanggal Lahir must be filled with text")
//                akses=false
//            }
//
//            if(nomorTelepon.isEmpty()) {
//                binding.inputLayoutNomorTelepon.setError("No Telepon must be filled with text")
//                akses=false
//            }
//
//            if(binding.inputLayoutUsername.getEditText()?.getText()==null){
//                binding.inputLayoutUsername.getEditText()?.setText("")
//            }
//
//            if(binding.inputLayoutPassword.getEditText()?.getText()==null){
//                binding.inputLayoutPassword.getEditText()?.setText("")
//            }
            binding.inputLayoutUsername.error = null
            binding.inputLayoutPassword.error = null
            binding.inputLayoutEmail.error = null
            binding.inputLayoutTanggalLahir.error = null
            binding.inputLayoutNomorTelepon.error = null

            val stringRequest: StringRequest =
                    object: StringRequest(Method.POST, ProfilApi.ADD_URL, Response.Listener { response->
                        val gson = Gson()
                        val profil = gson.fromJson(response, Profil::class.java)

                        if(profil!=null)
                            FancyToast.makeText(this@Register, "Data Berhasil Ditambahkan",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show()

                        val moveHome = Intent(this@Register, MainActivity::class.java)

                        mBundle.putString("Username",binding.inputLayoutUsername.getEditText()?.getText().toString())
                        mBundle.putString("Password",binding.inputLayoutPassword.getEditText()?.getText().toString())
                        moveHome.putExtra("register", mBundle)
                        createNotificationChannel()
                        sendNotification()

                        FancyToast.makeText(applicationContext, "Berhasil Diregistrasi!!!",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show()
                        startActivity(moveHome)

                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()

//                        setLoading(false)
                    }, Response.ErrorListener { error->

                        try{
                            val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                            if(error.networkResponse.statusCode == 404){
                                val gson = Gson()
                                val jsonObject = JSONObject(responseBody)
                                val jsonObject1 = jsonObject.getJSONObject("message")
                                for(i in jsonObject1.keys()){
                                    if(i == "username"){
                                        if (jsonObject1[i] != ""){
                                            binding.inputLayoutUsername.setError("Username must be filled with text")
                                        }
                                    }

                                    if (i == "password"){
                                        if (jsonObject1[i] != ""){
                                            binding.inputLayoutPassword.setError("Password must be filled with text")
                                        }
                                    }

                                    if (i == "email"){
                                        if (jsonObject1[i] != "") {
                                            binding.inputLayoutEmail.setError("Email must be filled with text")
                                        }
                                    }

                                    if (i == "tanggallhr"){
                                        if (jsonObject1[i] != ""){
                                            binding.inputLayoutTanggalLahir.setError("Tanggal lahir must be filled with text")
                                        }
                                    }

                                    if (i == "notelepon"){
                                        if (jsonObject1[i] != ""){
                                            binding.inputLayoutNomorTelepon.setError("No telepon must be filled with text")
                                        }
                                    }
                                }

                            }else {
                                val errors = JSONObject(responseBody)
                                FancyToast.makeText(this@Register,
                                    errors.getString("message"),
                                    FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show()
                            }
                        }catch (e:Exception){
                            FancyToast.makeText(this@Register, e.message,FancyToast.LENGTH_LONG,FancyToast.INFO,true).show()
                        }
                    })
                    {
                        @Throws(AuthFailureError::class)
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Accept"] = "application/json"
                            return headers
                        }
                        override fun getParams(): Map<String, String> {
                            val params = HashMap<String, String>()
                            params["username"] = binding.inputLayoutUsername.getEditText()?.getText().toString()
                            params["password"] = binding.inputLayoutPassword.getEditText()?.getText().toString()
                            params["email"] = binding.inputLayoutEmail.getEditText()?.getText().toString()
                            params["tanggallhr"] = binding.inputLayoutTanggalLahir.getEditText()?.getText().toString()
                            params["notelepon"] = binding.inputLayoutNomorTelepon.getEditText()?.getText().toString()
                            return params
                        }

                    }
                queue!!.add(stringRequest)

        })
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT).apply{
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotification(){
        val intent: Intent = Intent(this, MainActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent: Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", "Login first to access menu")
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.logo)
        val bigPictureStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(largeIcon)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setStyle(bigPictureStyle)
            .setContentTitle("Register Success")
            .setContentText("Thankyou for registering at Bengkelaz")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.GREEN)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Access Menu", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        //Toast.makeText(applicationContext, "Register with your new Account first", Toast.LENGTH_SHORT).show()
        FancyToast.makeText(applicationContext, "Register with your new Account first", FancyToast.LENGTH_LONG, FancyToast.SUCCESS,true).show()
        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }
    }

}