package com.okedev.footballschedule.base.presenter

import android.content.Context
import com.okedev.footballschedule.base.view.TeamFavoriteView
import com.okedev.footballschedule.data.db.database
import com.okedev.footballschedule.data.db.model.TeamTable
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class TeamFavoritePresenter(private val view: TeamFavoriteView, private val mContext: Context?) {
    fun getData() {
        view.showLoading()
        lateinit var data: List<TeamTable>
        doAsync {
            mContext?.database?.use {
                val result = select(TeamTable.TEAM_FAVORITE)
                data = result.parseList(classParser())
            }
            uiThread {
                view.showData(data)
                view.hideLoading()
            }
        }
    }
}