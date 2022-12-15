package com.example.tubes.api

class PemesananApi {
    companion object{
        val BASE_URL ="http://192.168.0.108:8080/pemesanan-apiserver/public/"

        val GET_ALL_URL = BASE_URL + "pemesanan"
        val GET_BY_ID_URL = BASE_URL + "pemesanan/"
        val ADD_URL = BASE_URL + "pemesanan"
        val UPDATE_URL = BASE_URL + "pemesanan/"
        val DELETE_URL = BASE_URL + "pemesanan/"
    }
}