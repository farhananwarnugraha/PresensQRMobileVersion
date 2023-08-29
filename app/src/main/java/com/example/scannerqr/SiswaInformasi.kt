package com.example.scannerqr

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivityGuruprofileBinding
import com.example.scannerqr.databinding.ActivitySiswainformasiBinding
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.Pengumuman
import com.example.scannerqr.misc.PengumumanAdapter
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.example.scannerqr.misc.PreferenceHelper.userToken
import com.example.scannerqr.misc.ResponsePengumuman
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SiswaInformasi:AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySiswainformasiBinding
    private val CUSTOM_PREF_NAME = "Data Login"

    private var listdata : List<Pengumuman> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiswainformasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
        //Log.d("Pengumuman :",listdata.toString())

    }
    override fun onClick(v: View?) {
        when(v?.id) {

        }
    }

    private fun loadData() {
        setProgress(true)
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        Log.d("Siswa Informasi :", "Mulai")
        val client = ApiConfig.getApiService().getPengumuman(prefs.userToken.toString())
        client.enqueue(object : Callback<ResponsePengumuman> {
            override fun onResponse(
                call: Call<ResponsePengumuman>,
                response: Response<ResponsePengumuman>
            ) {
                Log.d("Siswa Informasi :", "dapat")
                Log.d("Informasi -body :", response.body().toString())
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

            override fun onFailure(call: Call<ResponsePengumuman>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                errorMessage(t.message.toString())
            }

        })

        setProgress(false)
    }

    private fun setAdapter() {
        val adapter = PengumumanAdapter(listdata)

        binding.rvPengumuman.setHasFixedSize(true)
        binding.rvPengumuman.adapter = adapter
    }

    private fun errorMessage(message:String) {
        setProgress(false)
        //binding.tvWarning.visibility = View.VISIBLE
        //binding.tvWarning.text = message
    }

    private fun setProgress(cond:Boolean) {
        if(cond==true) {
            binding.progressBar6.visibility = View.VISIBLE
        } else {
            binding.progressBar6.visibility = View.GONE
        }
    }

}