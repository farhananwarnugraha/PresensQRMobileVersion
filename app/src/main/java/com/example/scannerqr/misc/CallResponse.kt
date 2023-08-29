package com.example.scannerqr.misc

import com.google.gson.annotations.SerializedName

data class CallResponse (
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,
)

data class CallResponseData (
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: Array<String>,
)

data class LoginSiswaResponse(
    @field:SerializedName("data")
    val data: SiswaResponse?,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class SiswaResponse(
    @field:SerializedName("nis_siswa")
    val nis_siswa: String,

    @field:SerializedName("nama_siswa")
    val nama_siswa: String,

    @field:SerializedName("kelas")
    val kelas: String,

    @field:SerializedName("jurusan")
    val jurusan: String,

    @field:SerializedName("token")
val token: String,

)

data class LoginGuruResponse(
    @field:SerializedName("data")
    val data: GuruResponse?,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class GuruResponse(
    @field:SerializedName("nip")
    val nip: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("jml_matapelajaran")
    val jml_matapelajaran: String,

    @field:SerializedName("token")
    val token: String,

)

data class ResponsePengumuman(
    @field:SerializedName("data")
    val data: List<Pengumuman>,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class Pengumuman (
    @field:SerializedName("id_pengumuman")
    val id_pengumuman:String,

    @field:SerializedName("tgl_pengumuman")
    val tgl_pengumuman:String,

    @field:SerializedName("isi_pengumuman")
    val isi_pengumuman:String,
)

data class ResponeHadir(
    @field : SerializedName("data")
    val data: Kehadiran,

    @field : SerializedName("success")
    val success:Boolean,

    @field:SerializedName("message")
    val message: String
)

data class Kehadiran(
    @field:SerializedName("Hadir")
    val Hadir: String,

    @field:SerializedName("Izin")
    val Izin: String,

    @field:SerializedName("Alpa")
    val Alpa: String,

    @field:SerializedName("Sakit")
    val Sakit: String,
)

data class ResponseProfilGuru (
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: ProfilGuru
)

data class ProfilGuru (
    @field:SerializedName("nama_guru")
    val namaGuru: String,

    @field:SerializedName("nip_guru")
    val nipGuru: String,

    @field:SerializedName("alamat_guru")
    val alamatGuru: String,

    @field:SerializedName("kota_guru")
    val kotaGuru: String,

    @field:SerializedName("jenis_kelamin")
    val jenisKelamin: String,

    @field:SerializedName("no_tlp")
    val noTlp: String,

    @field:SerializedName("pass_guru")
    val passGuru: String
)

data class ResponseProfilSiswa (
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: ProfilSiswa
)

data class ProfilSiswa (
    @field:SerializedName("nis_siswa")
    val nisSiswa: String,

    @field:SerializedName("nama_siswa")
    val namaSiswa: String,

    @field:SerializedName("alamat_siswa")
    val alamatSiswa: String,

    @field:SerializedName("kota_siswa")
    val kotaSiswa: String,

    @field:SerializedName("tgl_lahir")
    val tglLahir: String,

    @field:SerializedName("no_tlp")
    val noTlp: String,

    @field:SerializedName("pass_siswa")
    val passSiswa: String
)

data class ResponseMapel (
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<Mapel>
)

data class Mapel (
    @field:SerializedName("id_mapel")
    val idMapel: String,

    @field:SerializedName("nama_mapel")
    val namaMapel: String
)

data class ResponseRiwayat(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data : List<Riwayat>
)

data class Riwayat(
    @field:SerializedName("tanggal_presensi")
    val tanggal_presensi: String,

    @field:SerializedName("waktu_presensi")
    val waktu_presensi: String,

    @field:SerializedName("keterangan")
    val keterangan: String,
)


