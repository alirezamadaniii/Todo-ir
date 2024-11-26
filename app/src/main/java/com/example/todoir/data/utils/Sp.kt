package com.example.todoir.data.utils

import android.content.SharedPreferences
import javax.inject.Inject


class Sp @Inject constructor(private val sharedPreferences: SharedPreferences) {


    fun data(key: String?, value: String?) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun fetch(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun clear(){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear().apply()
    }
}