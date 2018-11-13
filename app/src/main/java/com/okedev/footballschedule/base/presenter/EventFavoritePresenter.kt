package com.okedev.footballschedule.base.presenter

import android.content.Context
import com.okedev.footballschedule.base.view.EventFavoriteView
import com.okedev.footballschedule.data.db.database
import com.okedev.footballschedule.data.db.model.EventTable
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EventFavoritePresenter(private val view: EventFavoriteView, private val mContext: Context?) {
    fun getData() {
        view.showLoading()
        lateinit var data: List<EventTable>
        doAsync {
            mContext?.database?.use {
                val result = select(EventTable.EVENT_FAVORITE)
                data = result.parseList(classParser())
            }
            uiThread {
                view.showData(data)
                view.hideLoading()
            }
        }
    }
}