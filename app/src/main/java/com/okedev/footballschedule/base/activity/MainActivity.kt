package com.okedev.footballschedule.base.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.okedev.footballschedule.BuildConfig
import com.okedev.footballschedule.R
import com.okedev.footballschedule.R.id.*
import com.okedev.footballschedule.base.fragment.EventFavoriteFragment
import com.okedev.footballschedule.base.fragment.FavoriteFragment
import com.okedev.footballschedule.base.fragment.MainEventFragment
import com.okedev.footballschedule.base.fragment.TeamFragment
import com.okedev.footballschedule.data.PreferencesHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var defLeagueId = BuildConfig.LEAGUE_ID
    private var preferencesHelper: PreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferencesHelper = PreferencesHelper(this)
        preferencesHelper?.leagueId.let {
            defLeagueId = it
        }

        main_navigation.setOnNavigationItemSelectedListener {
            item -> when(item.itemId){
                menu_match -> {
                    mainEventFragment(savedInstanceState)
                }
                menu_team -> {
                    teamFragment(savedInstanceState)
                }
                menu_favorite -> {
                    favoriteFragment(savedInstanceState)
                }
            }
            true
        }
        main_navigation.selectedItemId = menu_match
    }

    private fun mainEventFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, MainEventFragment(), MainEventFragment::class.java.simpleName)
                .commit()
        }
    }

    private fun favoriteFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, FavoriteFragment(), EventFavoriteFragment::class.java.simpleName)
                .commit()
        }
    }

    private fun teamFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, TeamFragment(), TeamFragment::class.java.simpleName)
                    .commit()
        }
    }
}