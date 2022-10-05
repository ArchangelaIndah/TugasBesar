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
import com.example.tubes.room.Constant
import com.example.tubes.room.Reservasi
import com.example.tubes.room.ReservasiDB
import kotlinx.android.synthetic.main.activity_edit_reservasi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.tubes.databinding.ActivityEditReservasiBinding

class EditReservasiActivity : AppCompatActivity() {
    val db by lazy { ReservasiDB(this) }
    private var reservasiId: Int = 0

    private var binding: ActivityEditReservasiBinding? = null
    private val CHANNEL_ID_reservasi = "channel_notification_02"
    private val notificationIdReservasi = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()
        super.onCreate(savedInstanceState)

        binding = ActivityEditReservasiBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        createNotificationChannel()

        setupView()
        setupListener()

        Toast.makeText(this,
            reservasiId.toString(), Toast.LENGTH_SHORT).show()


    }
    fun setupView(){
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
//            Constant.TYPE_CREATE -> {
//                button_update.visibility = View.GONE
//            }
//            Constant.TYPE_READ -> {
//                button_save.visibility = View.GONE
//                button_update.visibility = View.GONE
//                getReservasi()
//            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getReservasi()
            }
        }
    }
    private fun setupListener() {
        button_save.setOnClickListener {

            sendNotificationReservasi()
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
//        button_update.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                db.reservasiDao().updateReservasi(
//                    Reservasi(reservasiId,edit_nama.text.toString(),
//                        edit_noPlat.text.toString(),
//                        edit_jenisKendaraan.text.toString(),
//                        edit_keluhan.text.toString())
//                )
//                finish()
//            }
//        }
    }
    fun getReservasi() {
        reservasiId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val reservasies = db.reservasiDao().getReservasiById(reservasiId)[0]
            edit_nama.setText(reservasies.nama)
            edit_noPlat.setText(reservasies.noPlat)
            edit_jenisKendaraan.setText(reservasies.jenisKendaraan)
            edit_keluhan.setText(reservasies.keluhan)
        }
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
}