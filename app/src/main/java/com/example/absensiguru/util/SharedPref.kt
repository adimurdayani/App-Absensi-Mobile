package com.example.absensiguru.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.absensiguru.core.data.model.Guru
import com.google.gson.Gson

class SharedPref(activity: Activity) {
    var login = "Login"
    var input = "input"
    var nama = "nama"
    var email = "email"
    val guru = "guru"
    val mypref = "MY_PREF"
    val sp: SharedPreferences

    init {
        sp = activity.getSharedPreferences(mypref, Context.MODE_PRIVATE)
    }

    fun setStatusLogin(status: Boolean) {
        sp.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin(): Boolean {
        return sp.getBoolean(login, false)
    }

    fun setInputGuru(status: Boolean) {
        sp.edit().putBoolean(input, status).apply()
    }

    fun getInputGuru(): Boolean {
        return sp.getBoolean(input, false)
    }

    fun setUser(value: Guru) {
        val data: String = Gson().toJson(value, Guru::class.java)
        sp.edit().putString(guru, data).apply()
    }

    fun getUser(): Guru? {
        val data: String = sp.getString(guru, null) ?: return null
        return Gson().fromJson<Guru>(data, Guru::class.java)
    }

    fun setString(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    fun getStrin(key: String): String {
        return sp.getString(key, "")!!
    }
}