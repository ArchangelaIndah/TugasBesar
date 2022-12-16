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
import com.example.tubes.api.PemesananApi
import com.example.tubes.api.ReviewApi
import com.example.tubes.databinding.ActivityAddEditReviewBinding
import com.example.tubes.room.Constant
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_add_edit_pemesanan.*
import kotlinx.android.synthetic.main.activity_add_edit_review.*
import kotlinx.android.synthetic.main.activity_edit_reservasi.*
import kotlinx.android.synthetic.main.activity_edit_reservasi.button_save
import kotlinx.android.synthetic.main.activity_edit_reservasi.button_update
import kotlinx.android.synthetic.main.activity_edit_reservasi.edit_nama
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddEditReviewActivity : AppCompatActivity() {

    private var reviewId: Int = 0

    private var binding: ActivityAddEditReviewBinding? = null
    private val CHANNEL_ID_review = "channel_notification_02"
    private val notificationIdReview = 102
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        queue =  Volley.newRequestQueue(this)

        binding = ActivityAddEditReviewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        createNotificationChannel()

        setupView()
        setupListener()

        FancyToast.makeText(this,
            reviewId.toString(),
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
                getReview()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getReview()
            }
        }
    }
    private fun setupListener() {
        button_save.setOnClickListener {
            sendNotificationReview()
            addReview()
        }
        button_update.setOnClickListener {
            editReview(reviewId)
            finish()
        }
    }
    fun getReview() {
        reviewId = intent.getIntExtra("intent_id", 0)
        getReviewById(reviewId)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channelReview =  NotificationChannel(CHANNEL_ID_review, name, NotificationManager.IMPORTANCE_DEFAULT ).apply {
                description = descriptionText
            }

            val notificationManager : NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelReview)
        }
    }

    private fun sendNotificationReview(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_review)
            .setSmallIcon(R.drawable.ic_baseline_build_circle_24)
            .setStyle(
                NotificationCompat.InboxStyle()
                    .setBigContentTitle("Berhasil Menambahkan Data Review :")
                    .addLine("Review : "  + binding?.editReview?.text.toString())
                    .addLine("Saran : "+ binding?.editSaran?.text.toString())

            )
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(this)){
            notify(notificationIdReview, builder.build())
        }
    }

    private fun addReview(){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.POST, ReviewApi.ADD_URL, Response.Listener { response ->
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
                params["review"] = edit_review.text.toString()
                params["saran"] = edit_saran.text.toString()
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    private fun editReview(id: Int){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, ReviewApi.UPDATE_URL+id , Response.Listener { response ->
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
                params["review"] = edit_review.text.toString()
                params["saran"] = edit_saran.text.toString()
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    private fun getReviewById(id: Int){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, ReviewApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val review : Array<com.example.tubes.room.Review> = gson.fromJson(jsonData.toString(),Array<com.example.tubes.room.Review>::class.java)

                edit_review.setText(review[0].review)
                edit_saran.setText(review[0].saran)

                if(!review.isEmpty())
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