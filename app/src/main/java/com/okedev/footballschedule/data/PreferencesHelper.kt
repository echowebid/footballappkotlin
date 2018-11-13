package com.okedev.footballschedule.data

import android.content.Context
import android.preference.PreferenceManager

class PreferencesHelper(context: Context?) {
    companion object {
        private val LEAGUE = "league_id"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)


    var leagueId = preferences.getInt(LEAGUE, 4328)
        set(value) = preferences.edit().putInt(LEAGUE, value).apply()
}