package com.example.tubes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes.api.PemesananApi
import com.example.tubes.databinding.ActivityAddEditPemesananBinding
import com.example.tubes.room.Constant
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_add_edit_pemesanan.*
import kotlinx.android.synthetic.main.activity_edit_reservasi.*
import kotlinx.android.synthetic.main.activity_edit_reservasi.button_save
import kotlinx.android.synthetic.main.activity_edit_reservasi.button_update
import kotlinx.android.synthetic.main.activity_edit_reservasi.edit_nama
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddEditPemesananActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_edit_pemesanan)
//    }
    private var pemesananId: Int = 0

    private var binding: ActivityAddEditPemesananBinding? = null
    private val CHANNEL_ID_pemesanan = "channel_notification_02"
    private val notificationIdPemesanan = 102
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        queue =  Volley.newRequestQueue(this)

        binding = ActivityAddEditPemesananBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        createNotificationChannel()

        setupView()
        setupListener()

        FancyToast.makeText(this,
            pemesananId.toString(),
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
                getPemesanan()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getPemesanan()
            }
        }
    }
    private fun setupListener() {
        button_save.setOnClickListener {
            sendNotificationPemesanan()
            addPemesanan()
        }
        button_update.setOnClickListener {
            editPemesanan(pemesananId)
            finish()
        }
    }
    fun getPemesanan() {
        pemesananId = intent.getIntExtra("intent_id", 0)
        getPemesananById(pemesananId)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channelReservasi =  NotificationChannel(CHANNEL_ID_pemesanan, name, NotificationManager.IMPORTANCE_DEFAULT ).apply {
                description = descriptionText
            }

            val notificationManager : NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelReservasi)
        }
    }

    private fun sendNotificationPemesanan(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_pemesanan)
            .setSmallIcon(R.drawable.ic_baseline_build_circle_24)
            .setStyle(
                NotificationCompat.InboxStyle()
                    .setBigContentTitle("Berhasil Menambahkan Data Pemesanan :")
                    .addLine("Nama Barang : "  + binding?.editNama?.text.toString())
                    .addLine("Jumlah : "+ binding?.editJumlah?.text.toString())
                    .addLine("Alamat Bengkel : "+ binding?.editAlamatBengkel?.text.toString())

            )
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(this)){
            notify(notificationIdPemesanan, builder.build())
        }
    }

    private fun addPemesanan(){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.POST, PemesananApi.ADD_URL, Response.Listener { response ->
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
                params["namaBarang"] = edit_nama.text.toString()
                params["jumlah"] = edit_jumlah.text.toString()
                params["alamatBengkel"] = edit_alamatBengkel.text.toString()
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    private fun editPemesanan(id: Int){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, PemesananApi.UPDATE_URL+id , Response.Listener { response ->
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
                params["namaBarang"] = edit_nama.text.toString()
                params["jumlah"] = edit_jumlah.text.toString()
                params["alamatBengkel"] = edit_alamatBengkel.text.toString()
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    private fun getPemesananById(id: Int){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PemesananApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val pemesanan : Array<com.example.tubes.room.Pemesanan> = gson.fromJson(jsonData.toString(),Array<com.example.tubes.room.Pemesanan>::class.java)

                edit_nama.setText(pemesanan[0].namaBarang)
                edit_jumlah.setText(pemesanan[0].jumlah)
                edit_alamatBengkel.setText(pemesanan[0].alamatBengkel)

                if(!pemesanan.isEmpty())
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
}