package com.example.tubes.api

class ProfilApi {
    companion object{
        val BASE_URL ="http://192.168.253.169:8080/tubespbp-apiserver/public/"

        val GET_ALL_URL = BASE_URL + "profil"
        val GET_BY_ID_URL = BASE_URL + "profil/"
        val ADD_URL = BASE_URL + "profil"
        val UPDATE_URL = BASE_URL + "profil/"
        val DELETE_URL = BASE_URL + "profil/"
    }
}