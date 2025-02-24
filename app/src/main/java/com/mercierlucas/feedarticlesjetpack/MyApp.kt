package com.mercierlucas.feedarticlesjetpack

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

val myPrefs: Prefs by lazy{
    MyApp.prefs
}

class MyApp : Application(){
    companion object {
        lateinit var prefs: Prefs
        lateinit var appContext : Context
    }
    override fun onCreate() {
        prefs = Prefs(applicationContext)
        appContext = applicationContext

        super.onCreate()
    }

}

class Prefs (context: Context){
    private val PREFS_FILENAME = "com.mercierlucas.psybudgetv1.prefs"
    private val TOKEN = "token"
    private val USER_ID = "userId"
    private val USER_FIRST_NAME = "userFirstName"
    private val USER_LAST_NAME = "userLastName"
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var userId : Long
        get() = prefs.getLong(USER_ID, 0L)
        set(value) = prefs.edit().putLong(USER_ID,value).apply()
    var userFirstName : String?
        get() = prefs.getString(USER_FIRST_NAME, null)
        set(value) = prefs.edit().putString(USER_FIRST_NAME,value).apply()

    var userLastName : String?
        get() = prefs.getString(USER_LAST_NAME, null)
        set(value) = prefs.edit().putString(USER_LAST_NAME,value).apply()

    var token: String?
        get() = prefs.getString(TOKEN,null)
        set(value) = prefs.edit().putString(TOKEN, value).apply()
}