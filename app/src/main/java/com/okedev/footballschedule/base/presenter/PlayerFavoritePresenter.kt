package com.okedev.footballschedule.base.presenter

import android.content.Context
import com.okedev.footballschedule.base.view.PlayerFavoriteView
import com.okedev.footballschedule.data.db.database
import com.okedev.footballschedule.data.db.model.PlayerTable
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class PlayerFavoritePresenter(private val view: PlayerFavoriteView, private val mContext: Context?) {
    fun getData() {
        view.showLoading()
        lateinit var data: List<PlayerTable>
        doAsync {
            mContext?.database?.use {
                val result = select(PlayerTable.PLAYER_FAVORITE)
                data = result.parseList(classParser())
            }
            uiThread {
                view.showData(data)
                view.hideLoading()
            }
        }
    }
}