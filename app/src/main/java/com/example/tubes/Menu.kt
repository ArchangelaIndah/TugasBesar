package com.example.tubes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_menu.*
import nl.joery.animatedbottombar.AnimatedBottomBar

class Menu : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val profileFragment = AkunFragment()
    private val sukucadangFragment = SukuCadangFragment()
    private val reservasiFragment = ReservasiFragment()
//    private val mahasiswaFragment = MahasiswaFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        getSupportActionBar()?.hide()

        setThatFragments(homeFragment)
//        val bottom: BottomNavigationView = findViewById(R.id.bottom_navigation)
//        bottom.setOnItemSelectedListener {
//            when(it.itemId){
//                R.id.itemHome ->
//                    setThatFragments(homeFragment)
//                R.id.itemAkun->
//                    setThatFragments(profileFragment)
//                R.id.itemBuild->
//                setThatFragments(sukucadangFragment)
//                R.id.itemReservasi->
//                    setThatFragments(reservasiFragment)
////                R.id.itemMahasiswa->
////                    setThatFragments(mahasiswaFragment)
//            }
//            true
//        }

        bottom_navigation.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {

                //redirecting fragments
                when(newIndex){
                    0 -> setThatFragments(homeFragment);
                    1 -> setThatFragments(sukucadangFragment);
                    2 -> setThatFragments(reservasiFragment);
                    3 -> setThatFragments(profileFragment);
                }

                Log.d("bottom_bar", "Selected index: $newIndex, title: ${newTab.title}")


            }

            // An optional method that will be fired whenever an already selected tab has been selected again.
            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
            }
        });

    }


    private fun setThatFragments(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container,fragment)
            commit()
        }
    }

    fun setActivity(activity: AppCompatActivity){
        val move= Intent(this, activity::class.java)
        startActivity(move)
    }

}