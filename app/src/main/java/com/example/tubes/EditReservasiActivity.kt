package com.example.tubes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes.api.ReservasiApi
import com.example.tubes.room.Constant

import com.example.tubes.room.ReservasiDB
import kotlinx.android.synthetic.main.activity_edit_reservasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.tubes.databinding.ActivityEditReservasiBinding
import com.example.tubes.models.Reservasi
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class EditReservasiActivity : AppCompatActivity() {
    private var reservasiId: Int = 0

    private var binding: ActivityEditReservasiBinding? = null
    private val CHANNEL_ID_reservasi = "channel_notification_02"
    private val notificationIdReservasi = 102
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        queue =  Volley.newRequestQueue(this)

        binding = ActivityEditReservasiBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        createNotificationChannel()

        setupView()
        setupListener()

        FancyToast.makeText(this,
            reservasiId.toString(),
            FancyToast.LENGTH_LONG,
            FancyToast.SUCCESS,true).show()
//        Toast.makeText(this,
//            reservasiId.toString(), Toast.LENGTH_SHORT).show()


    }
    fun setupView(){
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getReservasi()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getReservasi()
            }
        }
    }
    private fun setupListener() {
        button_save.setOnClickListener {
            sendNotificationReservasi()
            addReservasi()
        }
        button_update.setOnClickListener {
            editReservasi(reservasiId)
            finish()
        }
    }
    fun getReservasi() {
        reservasiId = intent.getIntExtra("intent_id", 0)
        getReservasiById(reservasiId)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channelReservasi =  NotificationChannel(CHANNEL_ID_reservasi, name, NotificationManager.IMPORTANCE_DEFAULT ).apply {
                description = descriptionText
            }

            val notificationManager : NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelReservasi)
        }
    }

    private fun sendNotificationReservasi(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_reservasi)
            .setSmallIcon(R.drawable.ic_baseline_build_circle_24)
            .setStyle(
                NotificationCompat.InboxStyle()
                    .setBigContentTitle("Berhasil Menambahkan Data Reservasi :")
                    .addLine("Nama : "  + binding?.editNama?.text.toString())
                    .addLine("Plat Nomor : "+ binding?.editNoPlat?.text.toString())
                    .addLine("Jenis Kendaraan : "+ binding?.editJenisKendaraan?.text.toString())
                    .addLine("Keluhan : " + binding?.editKeluhan?.text.toString())

            )
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(this)){
            notify(notificationIdReservasi, builder.build())
        }
    }

    private fun addReservasi(){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.POST, ReservasiApi.ADD_URL, Response.Listener { response ->
                finish()
                Toast.makeText(this, "Berhsasil Tambah Data", Toast.LENGTH_SHORT ).show()
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

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["nama"] = edit_nama.text.toString()
                params["noPlat"] = edit_noPlat.text.toString()
                params["jenisKendaraan"] = edit_jenisKendaraan.text.toString()
                params["keluhan"] = edit_keluhan.text.toString()
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    private fun editReservasi(id: Int){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, ReservasiApi.UPDATE_URL+id , Response.Listener { response ->
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

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["nama"] = edit_nama.text.toString()
                params["noplat"] = edit_noPlat.text.toString()
                params["jeniskendaraan"] = edit_jenisKendaraan.text.toString()
                params["keluhan"] = edit_keluhan.text.toString()
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    private fun getReservasiById(id: Int){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, ReservasiApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val reservasi : Array<com.example.tubes.room.Reservasi> = gson.fromJson(jsonData.toString(),Array<com.example.tubes.room.Reservasi>::class.java)

                edit_nama.setText(reservasi[0].nama)
                edit_noPlat.setText(reservasi[0].noplat)
                edit_jenisKendaraan.setText(reservasi[0].jeniskendaraan)
                edit_keluhan.setText(reservasi[0].keluhan)

                if(!reservasi.isEmpty())
                    FancyToast.makeText(this, "Data Berhasil Diambil!",
                        FancyToast.LENGTH_LONG,
                        FancyToast.ERROR,true).show()
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
}