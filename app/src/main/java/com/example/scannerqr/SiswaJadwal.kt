package com.example.scannerqr

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivitySiswainformasiBinding
import com.example.scannerqr.databinding.ActivitySiswajadwalBinding
import com.example.scannerqr.databinding.ActivitySiswapresensiQrBinding
import com.example.scannerqr.misc.AdapterMapel
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.Mapel
import com.example.scannerqr.misc.PengumumanAdapter
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.example.scannerqr.misc.PreferenceHelper.userToken
import com.example.scannerqr.misc.ResponseMapel
import com.example.scannerqr.misc.ResponsePengumuman
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SiswaJadwal:AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySiswajadwalBinding
    private val CUSTOM_PREF_NAME = "Data Login"

    private var listdata: List<Mapel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiswajadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
        setAdapter()
        //Log.d("Pengumuman :",listdata.toString())

    }

    override fun onClick(v: View?) {
        when(v?.id) {

        }
    }

    private fun loadData() {
        setProgress(true)
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        Log.d("Siswa Jadwal :", "Mulai")
        val client = ApiConfig.getApiService().getJadwal(prefs.userToken.toString(),prefs.userID.toString())
        client.enqueue(object : Callback<ResponseMapel> {
            override fun onResponse(
                call: Call<ResponseMapel>,
                response: Response<ResponseMapel>
            ) {
                Log.d("Siswa Jadwal :", "dapat")
                Log.d("Jadwal -body :", response.body().toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if(responseBody.success) {
                            listdata = responseBody.data
                            setAdapter()
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

            override fun onFailure(call: Call<ResponseMapel>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                errorMessage(t.message.toString())
            }

        })

        setProgress(false)
    }

    private fun setAdapter() {
        val adapter = AdapterMapel(listdata)

        binding.rvJadwal.setHasFixedSize(true)
        binding.rvJadwal.adapter = adapter
    }

    private fun errorMessage(message:String) {
        setProgress(false)
        //binding.tvWarning3.visibility = View.VISIBLE
        //binding.tvWarning3.text = message
    }

    private fun setProgress(cond:Boolean) {
        if(cond==true) {
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar2.visibility = View.GONE
        }
    }

}