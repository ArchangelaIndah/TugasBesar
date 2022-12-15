package com.example.tubes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_pemesanan_adapter.view.*

class PemesananAdapter (private val pemesanans: ArrayList<com.example.tubes.models.Pemesanan>, private val listener: OnAdapterListener) :
    RecyclerView.Adapter<PemesananAdapter.PemesananViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            PemesananViewHolder {
        return PemesananViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_pemesanan_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: PemesananViewHolder, position:
        Int
    ) {
        val pemesanan = pemesanans[position]
        holder.view.text_nama.text = pemesanan.namaBarang
        holder.view.text_jumlah.text = pemesanan.jumlah
        holder.view.text_alamatBengkel.text = pemesanan.alamatBengkel

        holder.view.text_nama.setOnClickListener {
            listener.onClick(pemesanan)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(pemesanan)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(pemesanan)
        }
    }

    override fun getItemCount() = pemesanans.size
    inner class PemesananViewHolder(val view: View) :
        RecyclerView.ViewHolder(view)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: Array<com.example.tubes.models.Pemesanan>) {
        pemesanans.clear()
        pemesanans.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(pemesanan: com.example.tubes.models.Pemesanan)
        fun onUpdate(pemesanan: com.example.tubes.models.Pemesanan)
        fun onDelete(pemesanan: com.example.tubes.models.Pemesanan)
    }
}