package com.example.scannerqr

import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivitySiswapresensiBinding
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.example.scannerqr.misc.PreferenceHelper.userToken
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SiswaPresensiScan : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySiswapresensiBinding
    val CUSTOM_PREF_NAME = "Data Login"
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiswapresensiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPermissions()
        codeScanner()
        //sendData("1048")
    }

    override fun onClick(v: View?) {
        when(v?.id) {

        }
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding.scn)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    //binding.tvText.text = it.text
                    Log.d("Presensi ID",it.text.toString())
                    sendData(it.text.toString())
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "codeScanner: ${it.message}")

                    binding.tvText.text = "Ulangi Lagi"
                }
            }

            binding.scn.setOnClickListener {
                codeScanner.startPreview()
            }

        }
    }

    private fun sendData(id:String) {
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        Log.d("Check SisPresensiScan :", "Mulai")
        val client = ApiConfig.getApiService().getAbsenMasuk(prefs.userToken.toString(),id,prefs.userID.toString())
        client?.enqueue(object : Callback<CallResponse> {
            override fun onResponse(
                call: Call<CallResponse>,
                response: Response<CallResponse>
            ) {
                Log.d("Check Presensi :", "dapat")
                Log.d("Check Presensi -body :", response.body().toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("responebody :", responseBody.toString())
                    if (responseBody != null) {
                        if(responseBody.success) {
                            errorMessage(responseBody.message)
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

            override fun onFailure(call: Call<CallResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                errorMessage(t.message.toString())
            }
        })
    }

    private fun errorMessage(teks:String) {
        binding.tvText.text = teks
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val CAMERA_REQ = 101
    }
}