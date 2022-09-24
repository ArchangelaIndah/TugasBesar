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

class ReservasiAdapter (private val reservasi: ArrayList<Reservasi>, private val listener: OnAdapterListener) :
    RecyclerView.Adapter<ReservasiAdapter.ReservasiViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ReservasiViewHolder {
        return ReservasiViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.activity_reservasi_adapter,parent, false)
        )
    }
    override fun onBindViewHolder(holder: ReservasiViewHolder, position:
    Int) {
        val reservasi_val = reservasi[position]
        holder.view.text_nama.text = reservasi_val.nama
        holder.view.text_noPlat.text = reservasi_val.noPlat
        holder.view.text_jenisKendaraan.text = reservasi_val.jenisKendaraan
        holder.view.text_keluhan.text = reservasi_val.keluhan

        holder.view.text_nama.setOnClickListener{
            listener.onClick(reservasi_val)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(reservasi_val)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(reservasi_val)
        }
    }
    override fun getItemCount() = reservasi.size
    inner class ReservasiViewHolder( val view: View) :
        RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Reservasi>){
        reservasi.clear()
        reservasi.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener {
        fun onClick(note: Reservasi)
        fun onUpdate(note: Reservasi)
        fun onDelete(note: Reservasi)
    }
}