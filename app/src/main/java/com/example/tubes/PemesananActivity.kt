package com.example.tubes

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes.api.PemesananApi
import com.example.tubes.models.Pemesanan
import com.example.tubes.room.Constant
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_pemesanan.*
import kotlinx.android.synthetic.main.activity_reservasi.button_create
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class PemesananActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_pemesanan)
//    }
    lateinit var pemesananAdapter: PemesananAdapter
    private var queue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        queue =  Volley.newRequestQueue(this)
        setContentView(R.layout.activity_pemesanan)
        setupListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        pemesananAdapter = PemesananAdapter(arrayListOf(), object :
            PemesananAdapter.OnAdapterListener{
            override fun onClick(pemesanan : Pemesanan) {
//                Toast.makeText(applicationContext, reservasi.nama,
//                    Toast.LENGTH_SHORT).show()
                FancyToast.makeText(applicationContext, pemesanan.namaBarang,
                    FancyToast.LENGTH_SHORT,
                    FancyToast.SUCCESS,true).show()
                intentEdit(pemesanan.id!!, Constant.TYPE_READ)
            }
            override fun onUpdate(pemesanan : Pemesanan) {
                intentEdit(pemesanan.id!!, Constant.TYPE_UPDATE)
            }
            override fun onDelete(pemesanan : Pemesanan) {
                deleteDialog(pemesanan)
            }
        })
        list_pemesanan.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = pemesananAdapter
        }
    }
    private fun deleteDialog(pemesanan: com.example.tubes.models.Pemesanan){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From${pemesanan.namaBarang}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()

                deletePemesanan(pemesanan.id!!)
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
        allPemesanan()
    }
    fun setupListener() {
        button_create.setOnClickListener{
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }
    //pick data dari Id yang sebagai primary key
    fun intentEdit(pemesananId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, AddEditPemesananActivity::class.java)
                .putExtra("intent_id", pemesananId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun allPemesanan(){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PemesananApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val pemesanan : Array<com.example.tubes.models.Pemesanan> = gson.fromJson(jsonData.toString(),Array<com.example.tubes.models.Pemesanan>::class.java)

                pemesananAdapter.setData( pemesanan )

                if(!pemesanan.isEmpty())
                    FancyToast.makeText(this@PemesananActivity, "Data Berhasil Diambil!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS,true).show()
                //Toast.makeText(this@ReservasiActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT ).show()
                else
                    FancyToast.makeText(this@PemesananActivity, "Data Kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,true).show()
                //Toast.makeText(this@ReservasiActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                //srReservasi!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        this@ReservasiActivity,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    FancyToast.makeText(this@PemesananActivity,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,true).show()
                }catch (e: Exception){
                    FancyToast.makeText(this@PemesananActivity, e.message,
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,true).show()
                    //Toast.makeText(this@ReservasiActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun deletePemesanan(id: Int){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, PemesananApi.DELETE_URL + id, Response.Listener { response ->
                //Toast.makeText(this@ReservasiActivity, "Data Berhasil Dihapus!", Toast.LENGTH_SHORT ).show()
                allPemesanan()
                FancyToast.makeText(this@PemesananActivity, "Data Berhasil Dihapus!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,true).show()
            }, Response.ErrorListener { error ->
                //srReservasi!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        this@ReservasiActivity,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    FancyToast.makeText(this@PemesananActivity,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,true).show()
                }catch (e: Exception){
                    FancyToast.makeText(this@PemesananActivity, e.message,
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,true).show()
                    //Toast.makeText(this@ReservasiActivity, e.message, Toast.LENGTH_SHORT).show()
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