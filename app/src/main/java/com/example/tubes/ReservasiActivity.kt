package com.example.tubes

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes.api.ReservasiApi
import com.example.tubes.room.Constant
import com.example.tubes.room.Reservasi
import com.example.tubes.room.ReservasiDB
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_reservasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class ReservasiActivity : AppCompatActivity() {
    lateinit var reservasiAdapter: ReservasiAdapter
    private var queue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        queue =  Volley.newRequestQueue(this)
        setContentView(R.layout.activity_reservasi)
        setupListener()
        setupRecyclerView()


    }

    private fun setupRecyclerView() {
        reservasiAdapter = ReservasiAdapter(arrayListOf(), object :
            ReservasiAdapter.OnAdapterListener{
            override fun onClick(reservasi: com.example.tubes.models.Reservasi) {
                Toast.makeText(applicationContext, reservasi.nama,
                    Toast.LENGTH_SHORT).show()
                intentEdit(reservasi.id!!, Constant.TYPE_READ)
            }
            override fun onUpdate(reservasi: com.example.tubes.models.Reservasi) {
                intentEdit(reservasi.id!!, Constant.TYPE_UPDATE)
            }
            override fun onDelete(reservasi: com.example.tubes.models.Reservasi) {
                deleteDialog(reservasi)
            }
        })
        list_reservasi.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = reservasiAdapter
        }
    }
    private fun deleteDialog(reservasi: com.example.tubes.models.Reservasi){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From${reservasi.nama}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()

                deleteReservasi(reservasi.id!!)
                    loadData()

            })
        }
        alertDialog.show()
    }
    override fun onStart() {
        super.onStart()
        loadData()
    }
    //untuk load data yang tersimpan pada database yang sudah create data

    fun loadData() {
        allReservasi()
    }
    fun setupListener() {
        button_create.setOnClickListener{
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }
    //pick data dari Id yang sebagai primary key
    fun intentEdit(reservasiId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, EditReservasiActivity::class.java)
                .putExtra("intent_id", reservasiId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun allReservasi(){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, ReservasiApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val reservasi : Array<com.example.tubes.models.Reservasi> = gson.fromJson(jsonData.toString(),Array<com.example.tubes.models.Reservasi>::class.java)

                reservasiAdapter.setData( reservasi )

                if(!reservasi.isEmpty())
                    Toast.makeText(this@ReservasiActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT ).show()
                else
                    Toast.makeText(this@ReservasiActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                //srReservasi!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@ReservasiActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@ReservasiActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun deleteReservasi(id: Int){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, ReservasiApi.DELETE_URL + id, Response.Listener { response ->
                Toast.makeText(this@ReservasiActivity, "Data Berhasil Dihapus!", Toast.LENGTH_SHORT ).show()

            }, Response.ErrorListener { error ->
                //srReservasi!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@ReservasiActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@ReservasiActivity, e.message, Toast.LENGTH_SHORT).show()
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