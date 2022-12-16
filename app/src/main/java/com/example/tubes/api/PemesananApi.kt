package com.example.tubes.api

class PemesananApi {
    companion object{
        val BASE_URL ="http://192.168.253.169:8080/tubespbp-apiserver/public/"

        val GET_ALL_URL = BASE_URL + "pemesanan"
        val GET_BY_ID_URL = BASE_URL + "pemesanan/"
        val ADD_URL = BASE_URL + "pemesanan"
        val UPDATE_URL = BASE_URL + "pemesanan/"
        val DELETE_URL = BASE_URL + "pemesanan/"
    }
}