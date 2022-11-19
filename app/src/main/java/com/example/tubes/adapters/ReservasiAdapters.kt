package com.example.tubes.adapters
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.tubes.AddEditReservasiActivity
import com.example.tubes.models.Reservasi
import com.example.tubes.R
import com.example.tubes.ReservasiActivity1
import java.util.*
import kotlin.collections.ArrayList

class ReservasiAdapters(private var reservasiList: List<Reservasi>, context: Context) :
    RecyclerView.Adapter<ReservasiAdapters.ViewHolder>(), Filterable {

    private var filteredReservasiList: MutableList<Reservasi>
    private val context: Context

    init{
        filteredReservasiList = ArrayList(reservasiList)
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_reservasi, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredReservasiList.size
    }

    fun setReservasiList(reservasiList: Array<Reservasi>){
        this.reservasiList = reservasiList.toList()
        filteredReservasiList = reservasiList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val reservasi = filteredReservasiList[position]
        holder.tvNama.text = reservasi.nama
        holder.tvNoPlat.text = reservasi.noPlat
        holder.tvJenisKendaraan.text = reservasi.jenisKendaraan
        holder.tvKeluhan.text = reservasi.keluhan

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data Reservasi ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_, _ ->
                    if(context is ReservasiActivity1) reservasi.id?.let{ it1 ->
                        context.deleteReservasi(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvReservasi.setOnClickListener{
            val i = Intent(context, AddEditReservasiActivity::class.java)
            i.putExtra("id", reservasi.id)
            if(context is ReservasiActivity1)
                context.startActivityForResult(i, ReservasiActivity1.LAUNCH_ADD_ACTIVITY)

        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered : MutableList<Reservasi> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(reservasiList)
                }else{
                    for(reservasi in reservasiList){
                        if(reservasi.nama.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(reservasi)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredReservasiList.clear()
                filteredReservasiList.addAll((filterResults.values as List<Reservasi>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNama: TextView
        var tvNoPlat: TextView
        var tvJenisKendaraan: TextView
        var tvKeluhan: TextView
        var btnDelete: ImageButton
        var cvReservasi: CardView

        init{
            tvNama = itemView.findViewById(R.id.tv_nama)
            tvNoPlat = itemView.findViewById(R.id.tv_noPlat)
            tvJenisKendaraan = itemView.findViewById(R.id.tv_jenisKendaraan)
            tvKeluhan = itemView.findViewById(R.id.tv_keluhan)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvReservasi = itemView.findViewById(R.id.cv_reservasi)
        }
    }

}