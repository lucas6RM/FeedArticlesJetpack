package com.mercierlucas.feedarticlesjetpack.data.local

import android.content.SharedPreferences
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefsConstants.TOKEN
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefsConstants.USER_ID

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPrefs @Inject constructor(val prefs: SharedPreferences) {

    var userId: Long
        get() = prefs.getLong(USER_ID, 0L)
        set(value) = prefs.edit().putLong(USER_ID, value).apply()

    var token: String?
        get() = prefs.getString(TOKEN, null)
        set(value) = prefs.edit().putString(TOKEN, value).apply()

}