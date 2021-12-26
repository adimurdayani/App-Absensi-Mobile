package com.example.absensiguru

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.absensiguru.ui.auth.LoginFragment
import com.example.absensiguru.ui.home.HomeFragment

class LoginActivity : AppCompatActivity() {
    val fragmentLogin: Fragment = LoginFragment()
    val fm: FragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        fm.beginTransaction().add(R.id.frm_login, fragmentLogin).show(fragmentLogin).commit()
    }
}