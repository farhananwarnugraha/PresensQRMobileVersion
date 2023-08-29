package com.example.scannerqr

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.endarmartono.storyappdicoding.connection.ApiConfig
import com.example.scannerqr.databinding.ActivityGuruprofileBinding
import com.example.scannerqr.databinding.ActivitySiswaizinBinding
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.example.scannerqr.misc.PreferenceHelper.userToken
import com.example.scannerqr.misc.reduceFileImage
import com.example.scannerqr.misc.uriToFile
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Objects

class SiswaIzin:AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySiswaizinBinding
    val CUSTOM_PREF_NAME = "Data Login"
    private lateinit var curetPahtPoto: String
    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(this, "tidak ada permission", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiswaizinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCam.setOnClickListener { startTakePhoto() }
        binding.button.setOnClickListener { startGalary() }
        binding.btnizin.setOnClickListener { upload() }

    }

    override fun onClick(v: View?) {
        when(v?.id) {

        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        com.example.scannerqr.misc.createTempFile(application).also {
            /*
            val photoUri: Uri = FileProvider.getUriForFile(
                this@SiswaIzin,
                "com.example.scannerqr",
                it
            )

             */
            val photoUri: Uri = FileProvider.getUriForFile(
                Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider", it);
            curetPahtPoto = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGalary() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri

            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            binding.imageUp.setImageURI(selectedImg)
        }

    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(curetPahtPoto)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.imageUp.setImageBitmap(result)
        }
    }

    private fun upload() {
        setProgress(true)
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        if (getFile != null) {
            val exKet = binding.etnisIzinketerangan.text.toString()
            if (exKet.isEmpty()) {
                Toast.makeText(
                    this@SiswaIzin,
                    resources.getString(R.string.erKeterangan),
                    Toast.LENGTH_LONG
                ).show()

            } else {
                val ket = exKet.toRequestBody("text/plain".toMediaType())
                val userid = prefs.userID.toString().toRequestBody("text/plain".toMediaType());
                val nama_gambar = "gambar bukti".toRequestBody("text/plain".toMediaType());
                val file = reduceFileImage(getFile as File)
                val requstImage = file.asRequestBody("image/jpg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "gambar_bukti",
                    file.name,
                    requstImage
                )
                Log.d("Izin", prefs.userID.toString())
                val client = ApiConfig.getApiService().getSiswaPerizinan(prefs.userToken.toString(),userid, nama_gambar,imageMultipart, ket)
                client.enqueue(object : Callback<CallResponse> {
                    override fun onResponse(
                        call: Call<CallResponse>,
                        response: Response<CallResponse>
                    ) {
                        Log.d("Izin :", "dapat")
                        Log.d("Izin -body :", response.body().toString())
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            Log.d("Izin -responebody :", responseBody.toString())
                            if (responseBody != null) {
                                if(responseBody.success) {
                                    Toast.makeText(
                                        this@SiswaIzin,
                                        responseBody.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    //binding.etnisIzinketerangan.text = "Kosong"
                                } else {
                                    Toast.makeText(
                                        this@SiswaIzin,
                                        responseBody.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    this@SiswaIzin,
                                    "Gagal menyimpan",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            val gson = Gson()
                            val message: CallResponse = gson.fromJson(
                                response.errorBody()!!.charStream(),
                                CallResponse::class.java
                            )
                            Toast.makeText(
                                this@SiswaIzin,
                                "Gagal melakukan koneksi",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }

                    override fun onFailure(call: Call<CallResponse>, t: Throwable) {
                        Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                        Toast.makeText(
                            this@SiswaIzin,
                            t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        } else {
            Toast.makeText(
                this@SiswaIzin,
                resources.getString(R.string.no_image),
                Toast.LENGTH_LONG
            ).show()
        }
        setProgress(false)
    }

    private fun setProgress(cond:Boolean) {
        if(cond==true) {
            binding.progressBar4.visibility = View.VISIBLE
        } else {
            binding.progressBar4.visibility = View.GONE
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val EXTRA_TOKEN = "extra_token"

    }
}