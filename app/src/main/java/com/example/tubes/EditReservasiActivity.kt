package com.example.tubes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tubes.room.Constant
import com.example.tubes.room.Reservasi
import com.example.tubes.room.ReservasiDB
import kotlinx.android.synthetic.main.activity_edit_reservasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditReservasiActivity : AppCompatActivity() {
    val db by lazy { ReservasiDB(this) }
    private var reservasiId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_reservasi)
        setupView()
        setupListener()

        Toast.makeText(this,
            reservasiId.toString(), Toast.LENGTH_SHORT).show()
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
            CoroutineScope(Dispatchers.IO).launch {
                db.reservasiDao().addReservasi(
                    Reservasi(0,edit_nama.text.toString(),
                        edit_noPlat.text.toString(),
                        edit_jenisKendaraan.text.toString(),
                        edit_keluhan.text.toString())
                )
                finish()
            }
        }
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.reservasiDao().updateReservasi(
                    Reservasi(reservasiId, edit_nama.text.toString(),
                        edit_noPlat.text.toString(),
                        edit_jenisKendaraan.text.toString(),
                        edit_keluhan.text.toString())
                )
                finish()
            }
        }
    }
    fun getReservasi() {
        reservasiId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val reservasi_val = db.reservasiDao().getReservasiById(reservasiId)[0]
            edit_nama.setText(reservasi_val.nama)
            edit_noPlat.setText(reservasi_val.noPlat)
            edit_jenisKendaraan.setText(reservasi_val.jenisKendaraan)
            edit_keluhan.setText(reservasi_val.keluhan)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}