package com.example.scannerqr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.scannerqr.databinding.ActivityGurudashboardBinding
import com.example.scannerqr.databinding.ActivitySiswadashboardBinding
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.isType
import com.example.scannerqr.misc.PreferenceHelper.islogin
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.example.scannerqr.misc.PreferenceHelper.userJuru
import com.example.scannerqr.misc.PreferenceHelper.userKela
import com.example.scannerqr.misc.PreferenceHelper.userMapel
import com.example.scannerqr.misc.PreferenceHelper.userNama
import com.example.scannerqr.misc.PreferenceHelper.userToken

class GuruDashboard : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityGurudashboardBinding
    private val CUSTOM_PREF_NAME = "Data Login"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGurudashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.conJadwal.setOnClickListener(this)
        binding.conLogout.setOnClickListener(this)
        binding.conPresensi.setOnClickListener(this)
        binding.conProfile.setOnClickListener(this)
        binding.conPengumuman.setOnClickListener(this)

        loadData()

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.con_logout -> {
                logout()
            }
            R.id.con_presensi -> {
                startActivity(Intent(this, GuruPresensi::class.java))
            }
            R.id.con_jadwal -> {
                startActivity(Intent(this, GuruJadwal::class.java))
            }
            R.id.con_pengumuman -> {
                startActivity(Intent(this, GuruPengumuman::class.java))
            }
            R.id.con_profile -> {
                startActivity(Intent(this, GuruProfile::class.java))
            }
        }
    }

    private fun loadData() {
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        binding.nip.text = prefs.userID
        binding.textNamaGuru.text = prefs.userNama
    }

    private fun logout() {
        Log.d("Logout :","Saatnya clear")
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        prefs.islogin = false
        prefs.isType = "Siswa"
        prefs.userNama = ""
        prefs.userJuru = ""
        prefs.userKela = ""
        prefs.userID = ""
        prefs.userToken = ""
        prefs.userMapel = ""
        moveLogin()
    }
    private fun moveLogin() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}