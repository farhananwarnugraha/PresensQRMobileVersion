package com.example.scannerqr

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivityGurujadwalBinding
import com.example.scannerqr.databinding.ActivityGuruprofileBinding
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.example.scannerqr.misc.PreferenceHelper.userToken
import com.example.scannerqr.misc.ProfilGuru
import com.example.scannerqr.misc.ProfilSiswa
import com.example.scannerqr.misc.ResponseProfilGuru
import com.example.scannerqr.misc.ResponseProfilSiswa
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuruProfile : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityGuruprofileBinding
    private val CUSTOM_PREF_NAME = "Data Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuruprofileBinding.inflate(layoutInflater)
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
        Log.d("Guru Profil :", "Mulai")
        val client = ApiConfig.getApiService().getGuruProfil(prefs.userToken.toString(),prefs.userID.toString())
        client.enqueue(object : Callback<ResponseProfilGuru> {
            override fun onResponse(
                call: Call<ResponseProfilGuru>,
                response: Response<ResponseProfilGuru>
            ) {
                Log.d("Guru Profil :", "dapat")
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

            override fun onFailure(call: Call<ResponseProfilGuru>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                errorMessage(t.message.toString())
            }

        })
        setProgress(false)
    }

    private fun setProf(profil: ProfilGuru) {
        binding.tvNama2.text = profil.namaGuru
        binding.tvAlamat2.text = profil.alamatGuru
        binding.tvNIP.text = profil.nipGuru
        binding.tvKota2.text = profil.kotaGuru
        binding.tvJenisKelamin.text = profil.jenisKelamin
        binding.tvTelp2.text = profil.noTlp
    }

    private fun errorMessage(message:String) {
        setProgress(false)
        binding.tvWarning.visibility = View.VISIBLE
        binding.tvWarning.text = message
    }

    private fun setProgress(cond:Boolean) {
        if(cond==true) {
            binding.progressBar10.visibility = View.VISIBLE
        } else {
            binding.progressBar10.visibility = View.GONE
        }
    }

}