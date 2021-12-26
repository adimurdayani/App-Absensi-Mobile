package com.example.absensiguru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.absensiguru.ui.absen.AbsensiFragment
import com.example.absensiguru.ui.home.HomeFragment
import com.example.absensiguru.ui.inputdataguru.InputData
import com.example.absensiguru.ui.profile.ProfileFragment
import com.example.absensiguru.util.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    val fragmentHome: Fragment = HomeFragment()
    val fragmentAbsen: Fragment = AbsensiFragment()
    val fragmentProfile: Fragment = ProfileFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = fragmentHome
    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var s: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        s = SharedPref(this)
        setOptButtonNav()
    }

    private fun setOptButtonNav() {
        val user = s.getUser()!!
        if (user.kelamin == "") {
            startActivity(Intent(this, InputData::class.java))
        } else {
            fm.beginTransaction().add(R.id.frm_home, fragmentHome).show(fragmentHome).commit()
            fm.beginTransaction().add(R.id.frm_home, fragmentAbsen).hide(fragmentAbsen).commit()
            fm.beginTransaction().add(R.id.frm_home, fragmentProfile).hide(fragmentProfile).commit()
        }

        bottomNavigationView = findViewById(R.id.btn_navigasi)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    callFragment(0, fragmentHome)
                }
                R.id.item_keranjang -> {
                    callFragment(1, fragmentAbsen)
                }
                R.id.item_riwayat -> {
                    callFragment(2, fragmentProfile)
                }
            }
            false
        }
    }

    private fun callFragment(int: Int, fragment: Fragment) {
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }
}