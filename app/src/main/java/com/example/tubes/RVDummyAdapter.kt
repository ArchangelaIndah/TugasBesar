package com.example.tubes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes.entity.dummy

class RVDummyAdapter(private val data: Array<dummy>) : RecyclerView.Adapter<RVDummyAdapter.viewHolder>() {
    private val images = intArrayOf(
        R.drawable.ban,
        R.drawable.footstep,
        R.drawable.handgrip,
        R.drawable.oli,
        R.drawable.knalpot,
        R.drawable.lampu,
        R.drawable.rantai,
        R.drawable.spion,
        R.drawable.velg,
        R.drawable.kampasrem)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_sukucadang, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int){
        val currentItem = data[position]
        holder.nama_barang.text = currentItem.namaBarang
        holder.harga.text = currentItem.harga.toString()
        holder.barang.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nama_barang : TextView = itemView.findViewById(R.id.nama_barang)
        val harga : TextView = itemView.findViewById(R.id.harga)
        val barang : ImageView = itemView.findViewById(R.id.iv_barang)
    }
}