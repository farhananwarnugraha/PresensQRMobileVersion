package com.example.scannerqr

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivityGurujadwalBinding
import com.example.scannerqr.databinding.ActivityGurupengumumanBinding
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.Pengumuman
import com.example.scannerqr.misc.PengumumanAdapter
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.userToken
import com.example.scannerqr.misc.ResponsePengumuman
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuruPengumuman : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityGurupengumumanBinding
    private val CUSTOM_PREF_NAME = "Data Login"

    private var listdata : List<Pengumuman> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGurupengumumanBinding.inflate(layoutInflater)
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
        Log.d("Siswa Informasi :", "Mulai")
        val client = ApiConfig.getApiService().getPengumuman(prefs.userToken.toString())
        client.enqueue(object : Callback<ResponsePengumuman> {
            override fun onResponse(
                call: Call<ResponsePengumuman>,
                response: Response<ResponsePengumuman>
            ) {
                Log.d("Guru Informasi :", "dapat")
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

        binding.rvPengumumanGuru.setHasFixedSize(true)
        binding.rvPengumumanGuru.adapter = adapter
    }

    private fun errorMessage(message:String) {
        setProgress(false)
        //binding.tvWarning.visibility = View.VISIBLE
        //binding.tvWarning.text = message
    }

    private fun setProgress(cond:Boolean) {
        if(cond==true) {
            binding.progressBar9.visibility = View.VISIBLE
        } else {
            binding.progressBar9.visibility = View.GONE
        }
    }
}