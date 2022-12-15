//package com.example.tubes
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import android.view.WindowManager
//import android.widget.*
//import com.android.volley.AuthFailureError
//import com.android.volley.RequestQueue
//import com.android.volley.Response
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
//import com.example.tubes.api.ReservasiApi
//import com.example.tubes.models.Reservasi
//import com.google.gson.Gson
//import com.shashank.sony.fancytoastlib.FancyToast
//import org.json.JSONObject
//import java.nio.charset.StandardCharsets
//
//class AddEditReservasiActivity : AppCompatActivity() {
////    companion object{
////        private val FAKULTAS_LIST = arrayOf("FTI", "FT", "FBE", "FISIP", "FH")
////        private val PRODI_LIST = arrayOf(
////            "Informatika",
////            "Artsitektur",
////            "Biologi",
////            "Manajemen",
////            "Ilmu Komunikasi",
////            "Ilmu Hukum",
////        )
////    }
//
//    private var etNama: EditText? = null
//    private var etNoPlat: EditText? = null
//    private var etJenisKendaraan: EditText? = null
//    private var etKeluhan: EditText? = null
////    private var edFakultas: AutoCompleteTextView? = null
////    private var edProdi: AutoCompleteTextView? = null
//    private var layoutLoading: LinearLayout? = null
//    private var queue: RequestQueue? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_edit_reservasi)
//
//        //Pendeklarasian request queue
//        queue = Volley.newRequestQueue(this)
//        etNama = findViewById(R.id.et_nama)
//        etNoPlat = findViewById(R.id.et_noPlat)
//        etJenisKendaraan = findViewById(R.id.et_jenisKendaraan)
//        etKeluhan = findViewById(R.id.et_keluhan)
//        layoutLoading = findViewById(R.id.layout_loading)
//
//
//        val btnCancel = findViewById<Button>(R.id.btn_cancel)
//        btnCancel.setOnClickListener{finish() }
//        val btnSave = findViewById<Button>(R.id.btn_save)
//        val tvTitle =findViewById<TextView>(R.id.tv_title)
//        val id = intent.getLongExtra("id", -1)
//        if(id== -1L){
//            tvTitle.setText("Tambah Reservasi")
//            btnSave.setOnClickListener { createReservasi() }
//        }else{
//            tvTitle.setText("Edit Reservasi")
//            getReservasiById(id)
//
//            btnSave.setOnClickListener { updateReservasi(id) }
//        }
//
//    }
//
//
//    private fun getReservasiById(id: Long){
//        // Fungsi untuk menampilkan data mahasiswa berdasarkan id
//        setLoading(true)
//        val stringRequest: StringRequest =
//            object : StringRequest(Method.GET, ReservasiApi.GET_BY_ID_URL + id, Response.Listener { response ->
//                val gson = Gson()
//                val reservasi = gson.fromJson(response, Reservasi::class.java)
//
//                etNama!!.setText(reservasi.nama)
//                etNoPlat!!.setText(reservasi.noplat)
//                etJenisKendaraan!!.setText(reservasi.jeniskendaraan)
//                etKeluhan!!.setText(reservasi.keluhan)
//
//
//
//                FancyToast.makeText(this@AddEditReservasiActivity, "Data berhasil diambil",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show()
//                //Toast.makeText(this@AddEditReservasiActivity, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
//                setLoading(false)
//            },  Response.ErrorListener { error ->
//                setLoading(false)
//                try{
//                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
//                    val errors = JSONObject(responseBody)
////                    Toast.makeText(
////                        this@AddEditReservasiActivity,
////                        errors.getString("message"),
////                        Toast.LENGTH_SHORT
////                    ).show()
//
//                    FancyToast.makeText(this@AddEditReservasiActivity,errors.getString("message"),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show()
//                }catch (e: Exception){
//                    FancyToast.makeText(this@AddEditReservasiActivity, e.message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show()
//                    //Toast.makeText(this@AddEditReservasiActivity, e.message, Toast.LENGTH_SHORT).show()
//                }
//            }){
//                @Throws(AuthFailureError::class)
//                override fun getHeaders(): MutableMap<String, String> {
//                    val headers = HashMap<String, String>()
//                    headers["Accept"] = "application/json"
//                    return headers
//                }
//            }
//        queue!!.add(stringRequest)
//    }
//
//    private fun createReservasi(){
//        setLoading(true)
//        if(etNama!!.text.toString().isEmpty()) {
//            Toast.makeText(this@AddEditReservasiActivity, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show()
//        }
//        else if(etNoPlat!!.text.toString().isEmpty()) {
//            Toast.makeText(this@AddEditReservasiActivity, "No Plat tidak boleh kosong!", Toast.LENGTH_SHORT).show()
//        }
//        else if(etJenisKendaraan!!.text.toString().isEmpty()) {
//            Toast.makeText(this@AddEditReservasiActivity, "Jenis Kendaraan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
//        }
//        else if(etKeluhan!!.text.toString().isEmpty()) {
//            Toast.makeText(this@AddEditReservasiActivity, "Keluhan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
//        }else{
//            val reservasi = Reservasi(
//                etNama!!.text.toString(),
//                etNoPlat!!.text.toString(),
//                etJenisKendaraan!!.text.toString(),
//                etKeluhan!!.text.toString()
//            )
//            val stringRequest: StringRequest =
//                object: StringRequest(Method.POST, ReservasiApi.ADD_URL, Response.Listener { response->
//                    val gson = Gson()
//                    val reservasi = gson.fromJson(response, Reservasi::class.java)
//
//                    if(reservasi!=null)
//                        FancyToast.makeText(this@AddEditReservasiActivity, "Data Berhasil Ditambahkan",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show()
//                    //Toast.makeText(this@AddEditReservasiActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
//
//                    val returnIntent = Intent()
//                    setResult(RESULT_OK, returnIntent)
//                    finish()
//
//                    setLoading(false)
//                }, Response.ErrorListener { error->
//                    setLoading(false)
//                    try{
//                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
//                        val errors = JSONObject(responseBody)
//                        //                    Toast.makeText(
//                        //                        this@AddEditReservasiActivity,
//                        //                        errors.getString("message"),
//                        //                        Toast.LENGTH_SHORT
//                        //                    ).show()
//                        FancyToast.makeText(this@AddEditReservasiActivity,errors.getString("message"),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show()
//                    }catch (e:Exception){
//                        FancyToast.makeText(this@AddEditReservasiActivity, e.message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show()
//                        //Toast.makeText(this@AddEditReservasiActivity, e.message, Toast.LENGTH_SHORT).show()
//                    }
//                }) {
//                    @Throws(AuthFailureError::class)
//                    override fun getHeaders(): MutableMap<String, String> {
//                        val headers = HashMap<String, String>()
//                        headers["Accept"] = "application/json"
//                        return headers
//
//                    }
//
//                    @Throws(AuthFailureError::class)
//                    override fun getBody(): ByteArray {
//                        val gson = Gson()
//                        val requestBody = gson.toJson(reservasi)
//                        return requestBody.toByteArray(StandardCharsets.UTF_8)
//                    }
//
//                    override fun getBodyContentType(): String {
//                        return "application/json"
//                    }
//                }
//            // Menambahkan request ke request queue
//            queue!!.add(stringRequest)
//        }
//        setLoading(false)
//    }
//
//    private fun updateReservasi(id: Long){
//        setLoading(true)
//
//        val reservasi = Reservasi(
//            etNama!!.text.toString(),
//            etNoPlat!!.text.toString(),
//            etJenisKendaraan!!.text.toString(),
//            etKeluhan!!.text.toString()
//        )
//
//        val stringRequest: StringRequest = object :
//            StringRequest(Method.PUT, ReservasiApi.UPDATE_URL + id, Response.Listener{ response ->
//                val gson = Gson()
//
//                val reservasi = gson.fromJson(response, Reservasi::class.java)
//
//                if(reservasi != null)
//                    FancyToast.makeText(this@AddEditReservasiActivity, "Data Berhasil Diupdate",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show()
//                    //Toast.makeText(this@AddEditReservasiActivity, "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show()
//                val returnIntent = Intent()
//                setResult(RESULT_OK, returnIntent)
//                finish()
//
//                setLoading(false)
//            }, Response.ErrorListener{ error->
//                setLoading(false)
//                try{
//                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
//                    val errors = JSONObject(responseBody)
////                    Toast.makeText(
////                        this@AddEditReservasiActivity,
////                        errors.getString("message"),
////                        Toast.LENGTH_SHORT
////                    ).show()
//
//                    FancyToast.makeText(this@AddEditReservasiActivity, errors.getString("message"),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show()
//                }catch (e:Exception){
//                    FancyToast.makeText(this@AddEditReservasiActivity, e.message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show()
//                    //Toast.makeText(this@AddEditReservasiActivity, e.message, Toast.LENGTH_SHORT).show()
//                }
//            }){
//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val headers = HashMap<String, String>()
//                headers["Accept"] = "application/json"
//                return headers
//
//            }
//
//            @Throws(AuthFailureError::class)
//            override fun getBody(): ByteArray {
//                val gson = Gson()
//                val requestBody = gson.toJson(reservasi)
//                return requestBody.toByteArray(StandardCharsets.UTF_8)
//            }
//
//            override fun getBodyContentType(): String {
//                return "application/json"
//            }
//        }
//        queue!!.add(stringRequest)
//
//    }
//
//    //Fungsi ini digunakan untuk menampilkan layout Loading
//
//    private fun setLoading(isLoading: Boolean){
//        if(isLoading){
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//            )
//            layoutLoading!!.visibility = View.VISIBLE
//        }else{
//            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//            layoutLoading!!.visibility = View.INVISIBLE
//        }
//    }
//}