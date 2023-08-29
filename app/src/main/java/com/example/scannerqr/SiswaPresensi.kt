package com.example.scannerqr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.scannerqr.databinding.ActivitySiswapresensimainBinding
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.userID

class SiswaPresensi:AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySiswapresensimainBinding
    private val CUSTOM_PREF_NAME = "Data Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiswapresensimainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pilihQr.setOnClickListener(this)
        binding.pilihScan.setOnClickListener(this)

        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        Log.d("Check Presensi :", prefs.userID.toString())

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.pilih_qr -> {
                startActivity(Intent(this, SiswaPresensiQR::class.java))
            }
            R.id.pilih_scan -> {
                startActivity(Intent(this, SiswaPresensiScan::class.java))
            }
        }

    }

}