package com.example.scannerqr

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivityLoginGuruBinding
import com.example.scannerqr.databinding.ActivityLoginSiswaBinding
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.LoginGuruResponse
import com.example.scannerqr.misc.LoginSiswaResponse
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.customPreference
import com.example.scannerqr.misc.PreferenceHelper.isType
import com.example.scannerqr.misc.PreferenceHelper.islogin
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.example.scannerqr.misc.PreferenceHelper.userJuru
import com.example.scannerqr.misc.PreferenceHelper.userKela
import com.example.scannerqr.misc.PreferenceHelper.userMapel
import com.example.scannerqr.misc.PreferenceHelper.userNama
import com.example.scannerqr.misc.PreferenceHelper.userToken
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginGuru : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginGuruBinding
    val CUSTOM_PREF_NAME = "Data Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginGuruBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSiswa.setOnClickListener(this)
        binding.buttonLoginGuru.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.loginSiswa -> {
                val i = Intent(this, LoginSiswa::class.java)
                startActivity(i)
                finish()
            }
            R.id.buttonLoginGuru -> {
                var usernam = binding.etUsernameGuru.text.toString()
                var passwor = binding.etpasswordGuru.text.toString()

                if(passwor!="" && usernam!= "") {
                    checkLogin()
                } else {
                    showNotif("Periksa kembali email dan password yang anda masukan")
                }


                //prefs.userID = usernam.toString()
                //prefs.islogin = true
            }
        }
    }

    private fun checkLogin() {
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        showLoading(true)

        val usernam : String = binding.etUsernameGuru.text.toString()
        val passwor : String = binding.etpasswordGuru.text.toString()
        //Log.e(TAG, "onCekForm: "+mail+" - "+pass)
        Log.d("Check Login :", "Mulai")
        val client = ApiConfig.getApiService().getLoginGuru(usernam,passwor)
        client?.enqueue(object : Callback<LoginGuruResponse> {
            override fun onResponse(
                call: Call<LoginGuruResponse>,
                response: Response<LoginGuruResponse>
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
                            prefs.isType = "Guru"
                            prefs.userID = responseBody.data!!.nip
                            prefs.userToken = responseBody.data!!.token
                            prefs.userNama = responseBody.data!!.nama
                            prefs.userMapel = responseBody.data!!.jml_matapelajaran

                            moveLogin()
                        } else {
                            Log.d("Response Error",responseBody.toString())
                            showNotif(responseBody.message.toString())
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

            override fun onFailure(call: Call<LoginGuruResponse>, t: Throwable) {
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