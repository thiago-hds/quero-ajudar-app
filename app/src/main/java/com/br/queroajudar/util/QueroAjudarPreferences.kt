package com.br.queroajudar.util

import android.content.Context
import android.content.SharedPreferences

object QueroAjudarPreferences{
    private const val NAME = "QueroAjudar"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences : SharedPreferences

    // list of app specific preferences
    private val API_TOKEN_PREF = Pair("api_token", null)

    fun init(context : Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var apiToken: String?
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString(API_TOKEN_PREF.first, API_TOKEN_PREF.second)

        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString(API_TOKEN_PREF.first, value)
        }
}