package com.example.scannerqr

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivityLoginSiswaBinding
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.LoginSiswaResponse
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.islogin
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.example.scannerqr.misc.PreferenceHelper.userJuru
import com.example.scannerqr.misc.PreferenceHelper.userKela
import com.example.scannerqr.misc.PreferenceHelper.userNama
import com.example.scannerqr.misc.PreferenceHelper.userToken
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginSiswa : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginSiswaBinding
    val CUSTOM_PREF_NAME = "Data Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginGuru.setOnClickListener(this)
        binding.buttonLogin.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.loginGuru -> {
                val i = Intent(this, LoginGuru::class.java)
                startActivity(i)
                finish()
            }
            R.id.buttonLogin -> {
                val usernam = binding.etUsername.text.toString();
                val passwor = binding.etpassword.text.toString();

                if(passwor!="" && usernam!= "") {
                    checkLogin()
                } else {
                    showNotif("Periksa kembali email dan password yang anda masukan")
                }


            }
        }
    }

    private fun checkLogin() {
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        showLoading(true)

        val usernam : String = binding.etUsername.text.toString()
        val passwor : String = binding.etpassword.text.toString()
        //Log.e(TAG, "onCekForm: "+mail+" - "+pass)
        Log.d("Check Login :", "Mulai")
        val client = ApiConfig.getApiService().getLogin(usernam,passwor)
        client?.enqueue(object : Callback<LoginSiswaResponse> {
            override fun onResponse(
                call: Call<LoginSiswaResponse>,
                response: Response<LoginSiswaResponse>
            ) {
                Log.d("Check Login :", "dapat")
                Log.d("Check Login :", response.body().toString())
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("responebody :", responseBody.toString())
                    if (responseBody != null) {
                        if(responseBody.success) {
                            prefs.islogin = true;
                            prefs.userID = responseBody.data!!.nis_siswa
                            Log.d("Login Siswa :", responseBody.data!!.nis_siswa)
                            prefs.userToken = responseBody.data!!.token
                            prefs.userNama = responseBody.data!!.nama_siswa
                            prefs.userKela = responseBody.data!!.kelas
                            prefs.userJuru = responseBody.data!!.jurusan

                            moveLogin()
                        } else {
                            showNotif(responseBody.message)
                        }
                    } else {
                        showNotif("terjadi kesalahan");
                    }
                } else {
                    val gson = Gson()
                    val message: CallResponse = gson.fromJson(
                        response.errorBody()!!.charStream(),
                        CallResponse::class.java
                    )
                    showNotif(message.message)
                }
            }

            override fun onFailure(call: Call<LoginSiswaResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                showNotif(t.message.toString())
            }
        })


    }

    private fun moveLogin() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showNotif(text:String) {
        binding.tvLogNotif.visibility = View.VISIBLE
        binding.tvLogNotif.text = text
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}