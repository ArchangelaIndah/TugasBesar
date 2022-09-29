package com.example.tubes

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes.room.Reservasi
import kotlinx.android.synthetic.main.activity_reservasi_adapter.view.*

class ReservasiAdapter (private val reservasies: ArrayList<Reservasi>, private val
listener: OnAdapterListener) :
    RecyclerView.Adapter<ReservasiAdapter.ReservasiViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ReservasiViewHolder {
        return ReservasiViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.activity_reservasi_adapter,parent, false)
        )
    }
    override fun onBindViewHolder(holder: ReservasiViewHolder, position:
    Int) {
        val reservasi = reservasies[position]
        holder.view.text_nama.text = reservasi.nama
        holder.view.text_noPlat.text = reservasi.noPlat
        holder.view.text_jenisKendaraan.text = reservasi.jenisKendaraan
        holder.view.text_keluhan.text = reservasi.keluhan

        holder.view.text_nama.setOnClickListener{
            listener.onClick(reservasi)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onUpdate(reservasi)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(reservasi)
        }
    }
    override fun getItemCount() = reservasies.size
    inner class ReservasiViewHolder( val view: View) :
        RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Reservasi>){
        reservasies.clear()
        reservasies.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener {
        fun onClick(reservasi: Reservasi)
        fun onUpdate(reservasi: Reservasi)
        fun onDelete(reservasi: Reservasi)
    }
}