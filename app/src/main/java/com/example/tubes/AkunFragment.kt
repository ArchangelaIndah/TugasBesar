package com.example.tubes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.tubes.room.UserDB

class AkunFragment  : Fragment() {
    val db by lazy{activity?.let { UserDB(it )}  }

    var sharedPreferences: SharedPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Proses menghubungkan layout fragment_mahasiswa.xml dengan fragment ini
        return inflater.inflate(R.layout.fragment_akun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val namaTxt :TextView =  view.findViewById(R.id.nama)
        val emailTxt :TextView =  view.findViewById(R.id.email)
        val btnEdit : Button = view.findViewById(R.id.btnEdit)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        val id = sharedPreferences?.getString("id", "")
        println(id)
        namaTxt.setText(db?.userDao()?.getUser(id!!.toInt())?.nama)
        emailTxt.setText(db?.userDao()?.getUser(id!!.toInt())?.email)

        btnEdit.setOnClickListener(){
            (activity as Menu).setActivity(EditProfileActivity())
        }

        btnLogout.setOnClickListener {
            val moveToLogin = Intent(this@AkunFragment.context, MainActivity::class.java)
            startActivity(moveToLogin)
        }

    }
}