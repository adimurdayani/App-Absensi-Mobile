package com.example.absensiguru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.absensiguru.util.SharedPref

class MainActivity : AppCompatActivity() {
    private val handler: Handler = Handler()
    private lateinit var s: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        s = SharedPref(this)
        handler.postDelayed(Runnable {
            if (s.getStatusLogin()) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                startActivity(
                    Intent(
                        this@MainActivity,
                        LoginActivity::class.java
                    )
                )
                finish()
            }
        }, 3000)
    }
}