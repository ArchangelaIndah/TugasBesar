package com.example.tubes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes.entity.dummy

class SukuCadangFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sukucadang, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVDummyAdapter = RVDummyAdapter(dummy.listOfDummy)

        val rvDummy : RecyclerView = view.findViewById(R.id.rv_sukucadang)

        rvDummy.layoutManager = layoutManager

        rvDummy.setHasFixedSize(true)

        rvDummy.adapter =adapter

        val btnPesan: Button = view.findViewById(R.id.btnPesan)
        btnPesan.setOnClickListener(View.OnClickListener {
            val movePesanan = Intent(this@SukuCadangFragment.context, PemesananActivity::class.java)
            startActivity(movePesanan)
        })

    }
}