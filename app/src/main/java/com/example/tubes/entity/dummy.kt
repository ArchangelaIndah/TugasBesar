package com.example.tubes.entity

class dummy (var namaBarang: String, var harga: Int){
    companion object{
        @JvmField
        var listOfDummy = arrayOf(
            dummy("Ban", 50000),
            dummy("Spion", 40000),
            dummy("Lampu", 10000),
            dummy("Kampas Rem", 40000),
            dummy("Rantai", 50000),
            dummy("Handgrip motor", 50000),
            dummy("Knalpot", 60000),
            dummy("Footstep Motor", 50000),
            dummy("Oli", 40000),
            dummy("velg motor", 30000)
        )
    }
}