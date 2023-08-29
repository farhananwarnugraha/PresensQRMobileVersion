package com.example.scannerqr

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.scannerqr.databinding.ActivitySiswapresensiQrBinding
import com.example.scannerqr.misc.PreferenceHelper
import com.example.scannerqr.misc.PreferenceHelper.userID
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder


class SiswaPresensiQR:AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySiswapresensiQrBinding
    private val CUSTOM_PREF_NAME = "Data Login"

    lateinit var bitmap: Bitmap



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiswapresensiQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadQR()
    }

    override fun onClick(v: View?) {

    }

    private fun loadQR() {
        val prefs = PreferenceHelper.customPreference(this, CUSTOM_PREF_NAME)
        val text: String = prefs.userID.toString()
        Log.d("Buat QR : ",text)
        val writer = MultiFormatWriter()
        try {
            val matrix = writer.encode(text, BarcodeFormat.QR_CODE, 600, 600)
            val encoder = BarcodeEncoder()
            val bitmap: Bitmap = encoder.createBitmap(matrix)
            //set data image to imageview
            binding.QRcode.setImageBitmap(bitmap)//.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

}