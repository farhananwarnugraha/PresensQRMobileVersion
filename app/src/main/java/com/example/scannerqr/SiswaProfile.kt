package com.example.scannerqr

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivityGuruprofileBinding
import com.example.scannerqr.databinding.ActivitySiswaprofilBinding
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.example.scannerqr.misc.PreferenceHelper.userToken
import com.example.scannerqr.misc.ProfilSiswa
import com.example.scannerqr.misc.ResponsePengumuman
import com.example.scannerqr.misc.ResponseProfilSiswa
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SiswaProfile:AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySiswaprofilBinding
    val CUSTOM_PREF_NAME = "Data Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiswaprofilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
    }
    override fun onClick(v: View?) {
        when(v?.id) {

        }
    }

    private fun loadData() {
        setProgress(true)
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        Log.d("Siswa Profil :", "Mulai")
        val client = ApiConfig.getApiService().getSiswaProfil(prefs.userToken.toString(),prefs.userID.toString())
        client.enqueue(object : Callback<ResponseProfilSiswa> {
            override fun onResponse(
                call: Call<ResponseProfilSiswa>,
                response: Response<ResponseProfilSiswa>
            ) {
                Log.d("Siswa Profil :", "dapat")
                Log.d("Profil -body :", response.body().toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if(responseBody.success) {
                            setProf(responseBody.data)
                            setProgress(false)
                        } else {
                            errorMessage(responseBody.message)
                        }
                    } else {
                        errorMessage("Gagal memproses")
                    }
                } else {
                    val gson = Gson()
                    val message: CallResponse = gson.fromJson(
                        response.errorBody()!!.charStream(),
                        CallResponse::class.java
                    )
                    errorMessage(message.toString())
                }
            }

            override fun onFailure(call: Call<ResponseProfilSiswa>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                errorMessage(t.message.toString())
            }

        })
        setProgress(false)
    }

    private fun setProf(profil: ProfilSiswa) {
        binding.tvNama.text = profil.namaSiswa
        binding.tvAlamat.text = profil.alamatSiswa
        binding.tvNis.text = profil.nisSiswa
        binding.tvKota.text = profil.kotaSiswa
        binding.tvTanggalLahir.text = profil.tglLahir
        binding.tvTelp.text = profil.noTlp
    }

    private fun errorMessage(message:String) {
        setProgress(false)
        binding.tvWarning2.visibility = View.VISIBLE
        binding.tvWarning2.text = message
    }

    private fun setProgress(cond:Boolean) {
        if(cond==true) {
            binding.progressBar3.visibility = View.VISIBLE
        } else {
            binding.progressBar3.visibility = View.GONE
        }
    }

}