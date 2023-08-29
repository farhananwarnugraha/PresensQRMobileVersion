package com.endarmartono.storyappdicoding.connection
import com.example.scannerqr.misc.CallResponse
import com.example.scannerqr.misc.LoginGuruResponse
import com.example.scannerqr.misc.LoginSiswaResponse
import com.example.scannerqr.misc.ResponeHadir
import com.example.scannerqr.misc.ResponseMapel
import com.example.scannerqr.misc.ResponsePengumuman
import com.example.scannerqr.misc.ResponseProfilGuru
import com.example.scannerqr.misc.ResponseProfilSiswa
import com.example.scannerqr.misc.ResponseRiwayat
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("loginSiswa.php")
    fun getLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginSiswaResponse>?

    @FormUrlEncoded
    @POST("loginGuru.php")
    fun getLoginGuru(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginGuruResponse>?

    @GET("pengumuman.php")
    fun getPengumuman(
        @Header("Authorization") Authorization: String
    ):Call<ResponsePengumuman>

    @FormUrlEncoded
    @POST("presmasuk.php")
    fun getAbsenMasuk(
        @Header("Authorization") Authorization: String,
        @Field("id") id: String,
        @Field("nis_siswa") nis: String
    ): Call<CallResponse>?

    @Multipart
    @POST("perizinan.php")
    fun getSiswaPerizinan(
        @Header("Authorization") Authorization: String,
        @Part("nis_siswa") nis: RequestBody,
        @Part("nama_gambar") nama_gambar:RequestBody,
        @Part gambar_bukti: MultipartBody.Part,
        @Part("keterangan_izin") keterangan_izin:RequestBody
    ): Call<CallResponse>

    @FormUrlEncoded
    @POST("profilsiswa.php?do=hadir")
    fun getSiswaHadir(
        @Header("Authorization") Authorization: String,
        @Field("id") id: String
    ): Call<ResponeHadir>

    @FormUrlEncoded
    @POST("profilsiswa.php")
    fun getSiswaProfil(
        @Header("Authorization") Authorization: String,
        @Field("id") id: String
    ): Call<ResponseProfilSiswa>

    @FormUrlEncoded
    @POST("profilsiswa.php?do=jadwal")
    fun getJadwal(
        @Header("Authorization") Authorization: String,
        @Field("id") id: String
    ): Call<ResponseMapel>

    @FormUrlEncoded
    @POST("profilsiswa.php?do=morehadir")
    fun getRiwayat(
        @Header("Authorization") Authorization: String,
        @Field("id") id: String
    ): Call<ResponseRiwayat>


    @FormUrlEncoded
    @POST("presmapel.php")
    fun getAbsenMapel(
        @Header("Authorization") Authorization: String,
        @Field("nama_mapel") namamapel: String,
        @Field("nis_siswa") nis: String
    ): Call<CallResponse>?

    @FormUrlEncoded
    @POST("profilguru.php?do=mapel")
    fun getGuruMapel(
        @Header("Authorization") Authorization: String,
        @Field("id") id: String
    ): Call<ResponseMapel>

    @FormUrlEncoded
    @POST("profilguru.php")
    fun getGuruProfil(
        @Header("Authorization") Authorization: String,
        @Field("id") id: String
    ): Call<ResponseProfilGuru>




}