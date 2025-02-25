package com.mercierlucas.feedarticlesjetpack.data.local

import android.content.SharedPreferences
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefsConstants.TOKEN
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefsConstants.USER_FIRST_NAME
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefsConstants.USER_ID
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefsConstants.USER_LAST_NAME
import javax.inject.Inject


class MyPrefs @Inject constructor(val prefs: SharedPreferences){



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