package com.example.tubes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Menu : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val profileFragment = AkunFragment()
    private val sukucadangFragment = SukuCadangFragment()
    private val reservasiFragment = ReservasiFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        getSupportActionBar()?.hide()

        setThatFragments(homeFragment)
        val bottom: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottom.setOnItemSelectedListener {
            when(it.itemId){
                R.id.itemHome ->
                    setThatFragments(homeFragment)
                R.id.itemAkun->
                    setThatFragments(profileFragment)
                R.id.itemBuild->
                setThatFragments(sukucadangFragment)
                R.id.itemReservasi->
                    setThatFragments(reservasiFragment)
            }
            true
        }
    }

//    private fun replaceFragment(fragment: Fragment) {
//        if (fragment != null) {
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragment_container, fragment)
//            transaction.commit()
//        }
//    }

    private fun setThatFragments(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container,fragment)
            commit()
        }
    }

    private fun logout(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@Menu)
        builder.setTitle("Log Out")
        builder.setMessage("Are you sure want to exit?")
            .setPositiveButton("Yes"){ dialog, which ->
                finishAndRemoveTask()
            }
            .show()
    }
}