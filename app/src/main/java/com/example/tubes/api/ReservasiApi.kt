package com.example.tubes.api

class ReservasiApi {
    companion object{
        val BASE_URL ="http://192.168.1.6:8082/tubespbp-apiserver/public/"

        val GET_ALL_URL = BASE_URL + "reservasi"
        val GET_BY_ID_URL = BASE_URL + "reservasi/"
        val ADD_URL = BASE_URL + "reservasi"
        val UPDATE_URL = BASE_URL + "reservasi/"
        val DELETE_URL = BASE_URL + "reservasi/"
    }
}