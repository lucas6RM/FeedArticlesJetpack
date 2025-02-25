package com.mercierlucas.feedarticlesjetpack.data.local

import android.content.Context
import android.content.SharedPreferences
import com.mercierlucas.feedarticlesjetpack.data.local.MyPrefsConstants.PREF_FILENAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context) : SharedPreferences {
        return appContext.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)
    }

}