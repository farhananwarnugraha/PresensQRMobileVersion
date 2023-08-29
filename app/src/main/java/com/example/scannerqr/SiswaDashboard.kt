package com.example.scannerqr

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivitySiswadashboardBinding
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.isType
import com.example.scannerqr.misc.PreferenceHelper.islogin
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.example.scannerqr.misc.PreferenceHelper.userJuru
import com.example.scannerqr.misc.PreferenceHelper.userKela
import com.example.scannerqr.misc.PreferenceHelper.userMapel
import com.example.scannerqr.misc.PreferenceHelper.userNama
import com.example.scannerqr.misc.PreferenceHelper.userToken
import com.example.scannerqr.misc.ResponeHadir
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SiswaDashboard : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySiswadashboardBinding
    private val CUSTOM_PREF_NAME = "Data Login"
    var hadir:String = "0"
    var izin:String = "0"
    var alpa:String = "0"
    var sakit:String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiswadashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.conPresensi.setOnClickListener(this)
        binding.conInformasi.setOnClickListener(this)
        binding.conIzin.setOnClickListener(this)
        binding.conProfile.setOnClickListener(this)
        binding.morePresensi.setOnClickListener(this)
        binding.conJadwal.setOnClickListener(this)
        binding.conLogout.setOnClickListener(this)

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.con_logout -> {
                logout()
            }
            R.id.con_presensi -> {
                startActivity(Intent(this, SiswaPresensi::class.java))
            }
            R.id.con_informasi -> {
                startActivity(Intent(this, SiswaInformasi::class.java))
            }
            R.id.con_izin -> {
                startActivity(Intent(this, SiswaIzin::class.java))
            }
            R.id.con_profile -> {
                startActivity(Intent(this, SiswaProfile::class.java))
            }
            R.id.con_jadwal -> {
                startActivity(Intent(this, SiswaJadwal::class.java))
            }
            R.id.more_presensi -> {
                startActivity(Intent(this, SiswaRiwayat::class.java))
            }
        }
    }

    private fun loadData() {
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        binding.kelasSiswa.text = prefs.userKela
        binding.namaSiswa.text = prefs.userNama

        loadKehadiran()
    }

    private fun loadKehadiran() {
        setProgress(true)


        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        Log.d("Siswa Hadir :", "Mulai")
        val client = ApiConfig.getApiService().getSiswaHadir(prefs.userToken.toString(),prefs.userID.toString())
        client.enqueue(object : Callback<ResponeHadir> {
            override fun onResponse(
                call: Call<ResponeHadir>,
                response: Response<ResponeHadir>
            ) {
                Log.d("Siswa Informasi :", "dapat")
                Log.d("Informasi -body :", response.body().toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if(responseBody.success) {
                            hadir = responseBody.data.Hadir.toString()
                            izin = responseBody.data.Izin.toString()
                            alpa = responseBody.data.Alpa.toString()
                            sakit = responseBody.data.Sakit.toString()

                            binding.jmlhadir.text = hadir
                            binding.jmlalpa.text = alpa
                            binding.jmlsakit.text = sakit
                            binding.jmlIzin.text = izin
                        }
                    }
                } else {
                    val gson = Gson()
                    val message: CallResponse = gson.fromJson(
                        response.errorBody()!!.charStream(),
                        CallResponse::class.java
                    )
                }
            }

            override fun onFailure(call: Call<ResponeHadir>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }

        })
        binding.jmlhadir.text = hadir
        binding.jmlalpa.text = alpa
        binding.jmlsakit.text = sakit
        binding.jmlIzin.text = izin
        setProgress(false)
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

    private fun setProgress(cond:Boolean) {
        if(cond==true) {
            binding.progressBar5.visibility = View.VISIBLE
        } else {
            binding.progressBar5.visibility = View.GONE
        }
    }
}