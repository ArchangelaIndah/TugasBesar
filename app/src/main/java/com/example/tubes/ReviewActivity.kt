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
import com.example.tubes.api.ReviewApi
import com.example.tubes.models.Pemesanan
import com.example.tubes.models.Review
import com.example.tubes.room.Constant
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.activity_review.button_create
import kotlinx.android.synthetic.main.activity_reservasi.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class ReviewActivity : AppCompatActivity() {
    lateinit var ReviewAdapter: ReviewAdapter
    private var queue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        queue =  Volley.newRequestQueue(this)
        setContentView(R.layout.activity_review)
        setupListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        ReviewAdapter = ReviewAdapter(arrayListOf(), object :
            ReviewAdapter.OnAdapterListener{
            override fun onClick(Review : Review) {
//                Toast.makeText(applicationContext, reservasi.nama,
//                    Toast.LENGTH_SHORT).show()
                FancyToast.makeText(applicationContext, Review.review,
                    FancyToast.LENGTH_SHORT,
                    FancyToast.SUCCESS,true).show()
                intentEdit(Review.id!!, Constant.TYPE_READ)
            }
            override fun onUpdate(Review : Review) {
                intentEdit(Review.id!!, Constant.TYPE_UPDATE)
            }
            override fun onDelete(Review : Review) {
                deleteDialog(Review)
            }
        })
        list_review.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = ReviewAdapter
        }
    }
    private fun deleteDialog(Review: com.example.tubes.models.Review){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From${Review.review}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()

                deleteReview(Review.id!!)
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
        allReview()
    }
    fun setupListener() {
        button_create.setOnClickListener{
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }
    //pick data dari Id yang sebagai primary key
    fun intentEdit(ReviewId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, AddEditReviewActivity::class.java)
                .putExtra("intent_id", ReviewId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun allReview(){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, ReviewApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val Review : Array<com.example.tubes.models.Review> = gson.fromJson(jsonData.toString(),Array<com.example.tubes.models.Review>::class.java)

                ReviewAdapter.setData( Review )

                if(!Review.isEmpty())
                    FancyToast.makeText(this@ReviewActivity, "Data Berhasil Diambil!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS,true).show()
                //Toast.makeText(this@ReservasiActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT ).show()
                else
                    FancyToast.makeText(this@ReviewActivity, "Data Kosong!",
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
                    FancyToast.makeText(this@ReviewActivity,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,true).show()
                }catch (e: Exception){
                    FancyToast.makeText(this@ReviewActivity, e.message,
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

    private fun deleteReview(id: Int){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, ReviewApi.DELETE_URL + id, Response.Listener { response ->
                //Toast.makeText(this@ReservasiActivity, "Data Berhasil Dihapus!", Toast.LENGTH_SHORT ).show()
                FancyToast.makeText(this@ReviewActivity, "Data Berhasil Dihapus!",
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
                    FancyToast.makeText(this@ReviewActivity,
                        errors.getString("message"),
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,true).show()
                }catch (e: Exception){
                    FancyToast.makeText(this@ReviewActivity, e.message,
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