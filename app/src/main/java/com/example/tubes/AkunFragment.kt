package com.example.tubes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AkunFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogout : Button = view.findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener {
            activity?.let{it1->
                MaterialAlertDialogBuilder(it1)
                    .setTitle("Apakah anda ingin keluar?")
                    .setNegativeButton("No"){dialog, which ->

                    }
                    .setPositiveButton("Yes"){dialog, which ->
                        activity?.finish()
                    }
                    .show()
            }
        }
    }

}