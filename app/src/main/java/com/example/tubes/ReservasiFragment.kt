package com.example.tubes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ReservasiFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnMasuk: Button = view.findViewById(R.id.button)
        btnMasuk.setOnClickListener(View.OnClickListener {
            val moveReservasi = Intent(this@ReservasiFragment.context, ReservasiActivity::class.java)
            startActivity(moveReservasi)
        })
    }
}