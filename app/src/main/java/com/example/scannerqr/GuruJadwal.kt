package com.example.scannerqr

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivityGurujadwalBinding
import com.example.scannerqr.misc.AdapterMapel
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.Mapel
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.example.scannerqr.misc.PreferenceHelper.userToken
import com.example.scannerqr.misc.ResponseMapel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuruJadwal : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityGurujadwalBinding
    private val CUSTOM_PREF_NAME = "Data Login"

    private var listdata: List<Mapel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGurujadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
        setAdapter()

    }
    override fun onClick(v: View?) {

    }

    private fun loadData() {
        setProgress(true)
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        Log.d("Guru Jadwal :", "Mulai")
        val client = ApiConfig.getApiService().getGuruMapel(prefs.userToken.toString(),prefs.userID.toString())
        client.enqueue(object : Callback<ResponseMapel> {
            override fun onResponse(
                call: Call<ResponseMapel>,
                response: Response<ResponseMapel>
            ) {
                Log.d("Guru Jadwal :", "dapat")
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

        binding.rvJadwalGuru.setHasFixedSize(true)
        binding.rvJadwalGuru.adapter = adapter
    }

    private fun errorMessage(message:String) {
        setProgress(false)
        //binding.tvWarning3.visibility = View.VISIBLE
        //binding.tvWarning3.text = message
    }

    private fun setProgress(cond:Boolean) {
        if(cond==true) {
            binding.progressBar8.visibility = View.VISIBLE
        } else {
            binding.progressBar8.visibility = View.GONE
        }
    }
}