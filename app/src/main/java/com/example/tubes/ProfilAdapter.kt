package com.example.tubes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_reservasi_adapter.view.*
import kotlinx.android.synthetic.main.fragment_akun.view.*

class ProfilAdapter (private val profil: ArrayList<com.example.tubes.models.Profil>, private val listener: OnAdapterListener) :
    RecyclerView.Adapter<ProfilAdapter.ProfilViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ProfilViewHolder {
        return ProfilViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_akun,parent, false)
        )
    }
    override fun onBindViewHolder(holder: ProfilViewHolder, position:
    Int) {
        val profil = profil[position]
        holder.view.nama.text = profil.username
        holder.view.email.text = profil.email
//        holder.view.text_jenisKendaraan.text = profil.jeniskendaraan
//        holder.view.text_keluhan.text = profil.keluhan

//        holder.view.text_nama.setOnClickListener{
//            listener.onClick(profil)
//        }
//        holder.view.icon_edit.setOnClickListener {
//            listener.onUpdate(reservasi)
//        }
//        holder.view.icon_delete.setOnClickListener {
//            listener.onDelete(reservasi)
//        }
    }
    override fun getItemCount() = profil.size
    inner class ProfilViewHolder( val view: View) :
        RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: Array<com.example.tubes.models.Profil>){
        profil.clear()
        profil.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener {
        fun onClick(profil: com.example.tubes.models.Profil)
        fun onUpdate(profil: com.example.tubes.models.Profil)
        fun onDelete(profil: com.example.tubes.models.Profil)
    }
}