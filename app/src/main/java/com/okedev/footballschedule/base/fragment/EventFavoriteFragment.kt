package com.okedev.footballschedule.base.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.okedev.footballschedule.R
import com.okedev.footballschedule.base.activity.DetailEventActivity
import com.okedev.footballschedule.base.adapter.EventFavoriteAdapter
import com.okedev.footballschedule.base.presenter.EventFavoritePresenter
import com.okedev.footballschedule.base.view.EventFavoriteView
import com.okedev.footballschedule.data.db.model.EventTable
import com.okedev.footballschedule.utils.gone
import com.okedev.footballschedule.utils.invisible
import com.okedev.footballschedule.utils.visible
import kotlinx.android.synthetic.main.fragment_event.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh

class EventFavoriteFragment : Fragment(), EventFavoriteView {
    private var events: MutableList<EventTable> = mutableListOf()

    private lateinit var presenter: EventFavoritePresenter
    private lateinit var adapter: EventFavoriteAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter = EventFavoritePresenter(this, context)

        event_recycler.layoutManager = LinearLayoutManager(context)
        adapter = EventFavoriteAdapter(events) {
            context?.startActivity<DetailEventActivity>("eventId" to it.eventId?.toInt())
        }

        event_recycler.adapter = adapter

        presenter.getData()

        event_swipe_refresh?.onRefresh {
            presenter.getData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.getData()
    }

    override fun showLoading() {
        event_swipe_refresh?.let {
            if (!it.isRefreshing) {
                event_progressbar?.visible()
            }
        }
        event_message?.gone()
    }

    override fun hideLoading() {
        event_progressbar?.invisible()
        event_swipe_refresh?.let {
            if (it.isRefreshing) {
                it.isRefreshing = false
            }
        }
    }

    override fun dataNotFound(str: String?) {
        hideLoading()
        event_message?.let {
            it.text = if (str.isNullOrEmpty()) resources.getString(R.string.data_error) else str
            it.visible()
        }
    }

    override fun showData(result: List<EventTable>) {
        val activity = activity
        if (activity != null && isAdded) {
            events.clear()
            events.addAll(result)
            adapter.notifyDataSetChanged()
            if (events.size == 0) {
                dataNotFound(resources.getString(R.string.data_empty))
            }
        }
    }
}
