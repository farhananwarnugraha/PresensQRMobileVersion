package com.example.scannerqr

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.scannerqr.databinding.ActivityMainBinding
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.isType
import com.example.scannerqr.misc.PreferenceHelper.islogin

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    val CUSTOM_PREF_NAME = "Data Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogGuru.setOnClickListener(this)
        binding.btnLogSIswa.setOnClickListener(this)
    }

    override fun onStart() {
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        super.onStart()
        if(prefs.islogin == true) {

            if (prefs.isType.toString()== "Guru") {
                val i = Intent(this, GuruDashboard::class.java)
                startActivity(i)
                finish()
            } else {
                val i = Intent(this, SiswaDashboard::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogGuru -> {
                val i = Intent(this, LoginGuru::class.java)
                startActivity(i)
                finish()
            }
            R.id.btnLogSIswa -> {
                val i = Intent(this, LoginSiswa::class.java)
                startActivity(i)
                finish()
            }
        }
    }
}