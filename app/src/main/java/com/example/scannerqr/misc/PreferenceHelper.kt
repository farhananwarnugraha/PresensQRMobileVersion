package com.example.scannerqr.misc

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferenceHelper {
    val IS_LOGIN = "IS_LOGIN"
    val IS_TYPE = "IS_TYPE"
    val USER_NAMA = "USER_NAMA"
    val USER_KELA = "USER_KELAS"
    val USER_JURU = "USER_JURUSAN"
    val USER_ID = "USER_ID"
    val USER_MAPEL = "USER_MAPEL"
    val USER_TOKEN = "USER_TOKEN"

    fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.islogin
        get() = getBoolean(IS_LOGIN, false)
        set(value) {
            editMe {
                it.putBoolean(IS_LOGIN, value)
            }
        }

    var SharedPreferences.isType: String?
        get() = getString(IS_TYPE,"Siswa")
        set(value) {
            editMe {
                it.putString(IS_TYPE, value)
            }
        }

    var SharedPreferences.userToken: String?
        get() = getString(USER_TOKEN,"")
        set(value) {
            editMe {
                it.putString(USER_TOKEN, value)
            }
        }

    var SharedPreferences.userNama: String?
        get() = getString(USER_NAMA,"")
        set(value) {
            editMe {
                it.putString(USER_NAMA, value)
            }
        }

    var SharedPreferences.userKela: String?
        get() = getString(USER_KELA,"")
        set(value) {
            editMe {
                it.putString(USER_KELA, value)
            }
        }
    var SharedPreferences.userJuru: String?
        get() = getString(USER_JURU,"")
        set(value) {
            editMe {
                it.putString(USER_JURU, value)
            }
        }
    var SharedPreferences.userMapel: String?
        get() = getString(USER_MAPEL,"")
        set(value) {
            editMe {
                it.putString(USER_MAPEL, value)
            }
        }
    var SharedPreferences.userID: String?
        get() = getString(USER_ID,"")
        set(value) {
            editMe {
                it.putString(USER_ID, value)
            }
        }


    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.clear()
                it.commit()
            }
        }
}