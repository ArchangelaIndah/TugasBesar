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
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes.api.ProfilApi
import com.example.tubes.api.ReservasiApi
import com.example.tubes.models.Profil
import com.example.tubes.room.User
import com.example.tubes.room.UserDB
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.HashMap

class AkunFragment  : Fragment() {
    val db by lazy{activity?.let { UserDB(it )}  }
    var sharedPreferences: SharedPreferences? = null
    lateinit var profilAdapter: ProfilAdapter
    private var queue: RequestQueue? = null
    private var namaTxt : TextView? = null
    private var emailTxt : TextView? = null



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
        namaTxt  =  view.findViewById(R.id.nama)
        emailTxt =  view.findViewById(R.id.email)
        val btnEdit : Button = view.findViewById(R.id.btnEdit)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        val id = sharedPreferences?.getString("id", "")
        queue =  Volley.newRequestQueue(requireActivity())

        println(id)
        getProfilById(id!!.toInt())


        btnEdit.setOnClickListener(){
            (activity as Menu).setActivity(EditProfileActivity())
        }

        btnLogout.setOnClickListener {
            val moveToLogin = Intent(this@AkunFragment.context, MainActivity::class.java)
            startActivity(moveToLogin)
        }

        val btnCam: ImageButton = view.findViewById(R.id.imageCam)
        btnCam.setOnClickListener{
            val moveCam = Intent(this@AkunFragment.context, CameraActivity::class.java)
            startActivity(moveCam)
        }

    }

    private fun getProfilById(id: Int){
        //srReservasi!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, ProfilApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonData = jsonObject.getJSONArray("data")
                val profil : Array<Profil> = gson.fromJson(jsonData.toString(),Array<Profil>::class.java)

                namaTxt!!.setText(profil[0].username)
                emailTxt!!.setText(profil[0].email)

                if(!profil.isEmpty())
                    Toast.makeText(requireActivity(), "Data Berhasil Diambil!", Toast.LENGTH_SHORT ).show()
                else
                    Toast.makeText(requireActivity(), "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                //srReservasi!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        requireActivity(),
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

}