package com.example.tubes

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes.room.Constant
import com.example.tubes.room.Reservasi
import com.example.tubes.room.ReservasiDB
import kotlinx.android.synthetic.main.activity_reservasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservasiActivity : AppCompatActivity() {
    val db by lazy { ReservasiDB(this) }
    lateinit var reservasiAdapter: ReservasiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservasi)
        setupListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        reservasiAdapter = ReservasiAdapter(arrayListOf(), object :
            ReservasiAdapter.OnAdapterListener{
            override fun onClick(reservasi: Reservasi) {
                Toast.makeText(applicationContext, reservasi.nama,
                    Toast.LENGTH_SHORT).show()
                intentEdit(reservasi.id, Constant.TYPE_READ)
            }
            override fun onUpdate(reservasi: Reservasi) {
                intentEdit(reservasi.id, Constant.TYPE_UPDATE)
            }
            override fun onDelete(reservasi: Reservasi) {
                deleteDialog(reservasi)
            }
        })
        list_reservasi.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = reservasiAdapter
        }
    }
    private fun deleteDialog(reservasi: Reservasi){
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
                CoroutineScope(Dispatchers.IO).launch {
                    db.reservasiDao().deleteReservasi(reservasi)
                    loadData()
                }
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
        CoroutineScope(Dispatchers.IO).launch {
            val reservasi_val = db.reservasiDao().getReservasi()
            Log.d("ReservasiActivity","dbResponse: $reservasi_val")
            withContext(Dispatchers.Main){
                reservasiAdapter.setData( reservasi_val )
            }
        }
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
}