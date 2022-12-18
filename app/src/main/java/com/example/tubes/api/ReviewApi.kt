package com.example.tubes.api

class ReviewApi {
    companion object{
        val BASE_URL ="http://192.168.1.6:8082/tubespbp-apiserver/public/"

        val GET_ALL_URL = BASE_URL + "review"
        val GET_BY_ID_URL = BASE_URL + "review/"
        val ADD_URL = BASE_URL + "review"
        val UPDATE_URL = BASE_URL + "review/"
        val DELETE_URL = BASE_URL + "review/"
    }
}