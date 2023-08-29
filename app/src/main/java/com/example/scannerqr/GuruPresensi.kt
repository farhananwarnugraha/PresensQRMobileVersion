package com.example.scannerqr

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivityGurupresensiBinding
import com.example.scannerqr.misc.AdapterMapel
import com.example.scannerqr.misc.AdapterSpinnerMapel
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
import java.lang.String


class GuruPresensi : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityGurupresensiBinding
    private val CUSTOM_PREF_NAME = "Data Login"
    private lateinit var spinner: Spinner

    private var _datamapel: List<Mapel> = emptyList()
    private var _selectedMapel = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGurupresensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnScanGuru.setOnClickListener(this)
        loadMapel()
        spinner = binding.spinner

        loadMapel()
        setSpinner()


    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_scan_guru -> {
                _selectedMapel = _datamapel[spinner.selectedItemPosition].idMapel
                val intent = Intent(this@GuruPresensi, GuruPresensiScan::class.java)
                intent.putExtra("mapel", _selectedMapel)
                Log.d("GuruPresensi", _selectedMapel)
                startActivity(intent)
            }
        }
    }

    private fun setSpinner() {
        val customDropDownAdapter = AdapterSpinnerMapel(this, _datamapel)
        spinner.adapter = customDropDownAdapter
    }

    private fun loadMapel() {
        setProgress(true)
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        Log.d("Guru Jadwal :", "Mulai")
        val client = ApiConfig.getApiService().getGuruMapel(prefs.userToken.toString(),prefs.userID.toString())
        client.enqueue(object : Callback<ResponseMapel> {
            override fun onResponse(
                call: Call<ResponseMapel>,
                response: Response<ResponseMapel>
            ) {
                Log.d("Guru Presensi :", "dapat")
                Log.d("Mapel -body :", response.body().toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if(responseBody.success) {
                            _datamapel = responseBody.data
                            setSpinner()
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

    private fun errorMessage(message: kotlin.String) {
        setProgress(false)
        //binding.tvWarning3.visibility = View.VISIBLE
        //binding.tvWarning3.text = message
    }

    private fun setProgress(cond:Boolean) {
        if(cond==true) {
            binding.progressBar11.visibility = View.VISIBLE
        } else {
            binding.progressBar11.visibility = View.GONE
        }
    }

}