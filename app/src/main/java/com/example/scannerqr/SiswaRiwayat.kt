package com.example.scannerqr

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivitySiswariwayatBinding
import com.example.scannerqr.misc.AdapterMapel
import com.example.scannerqr.misc.AdapterRiwayat
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.example.scannerqr.misc.PreferenceHelper.userToken
import com.example.scannerqr.misc.ResponseMapel
import com.example.scannerqr.misc.ResponseRiwayat
import com.example.scannerqr.misc.Riwayat
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SiswaRiwayat:AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySiswariwayatBinding
    private val CUSTOM_PREF_NAME = "Data Login"

    private var listdata: List<Riwayat> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiswariwayatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()

        setAdapter()

    }

    override fun onClick(v: View?) {
        when(v?.id) {

        }
    }

    private fun loadData() {
        setProgress(true)
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        Log.d("Siswa Riwayat :", "Mulai")
        val client = ApiConfig.getApiService().getRiwayat(prefs.userToken.toString(),prefs.userID.toString())
        client.enqueue(object : Callback<ResponseRiwayat> {
            override fun onResponse(
                call: Call<ResponseRiwayat>,
                response: Response<ResponseRiwayat>
            ) {
                Log.d("Siswa Riwayat :", "dapat")
                Log.d("Riwayat -body :", response.body().toString())
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

            override fun onFailure(call: Call<ResponseRiwayat>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                errorMessage(t.message.toString())
            }

        })

        setProgress(false)
    }

    private fun setAdapter() {
        val adapter = AdapterRiwayat(listdata)

        binding.rvRiwayat.setHasFixedSize(true)
        binding.rvRiwayat.adapter = adapter
    }

    private fun errorMessage(message:String) {
        setProgress(false)
        //binding.tvWarning4.visibility = View.VISIBLE
        //binding.tvWarning4.text = message
    }

    private fun setProgress(cond:Boolean) {
        if(cond==true) {
            binding.progressBar7.visibility = View.VISIBLE
        } else {
            binding.progressBar7.visibility = View.GONE
        }
    }

}