package com.okedev.footballschedule.base.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.okedev.footballschedule.BuildConfig
import com.okedev.footballschedule.R
import com.okedev.footballschedule.base.activity.DetailEventActivity
import com.okedev.footballschedule.base.adapter.EventAdapter
import com.okedev.footballschedule.base.presenter.EventPresenter
import com.okedev.footballschedule.base.view.EventView
import com.okedev.footballschedule.data.PreferencesHelper
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.Event
import com.okedev.footballschedule.data.network.response.EventResponse
import com.okedev.footballschedule.utils.*
import kotlinx.android.synthetic.main.fragment_event.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh
import java.util.*


class EventFragment : Fragment(), EventView {

    private var events: MutableList<Event> = mutableListOf()
    private var defLeagueId = BuildConfig.LEAGUE_ID
    private var target = 1
    private var preferencesHelper: PreferencesHelper? = null

    private lateinit var eventPresenter: EventPresenter

    companion object {
        fun newInstance(target: Int): EventFragment {
            val fragment = EventFragment()
            fragment.target = target
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        eventPresenter = EventPresenter(this, EventRepository())

        event_recycler.layoutManager = LinearLayoutManager(context)
        event_recycler.adapter = EventAdapter(events) { event, alarm ->
            if (alarm == true) {
                openCalendar(event)
            } else {
                context?.startActivity<DetailEventActivity>("eventId" to event.idEvent?.toInt())
            }
        }

        preferencesHelper = PreferencesHelper(context)
        preferencesHelper?.leagueId.let {
            defLeagueId = it
        }

        getData(defLeagueId, target)

        event_swipe_refresh?.onRefresh {
            getData(defLeagueId, target)
        }
    }

    private fun getData(l: Int, t: Int) {
        eventPresenter.getData(l, t)
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

    override fun showData(data: EventResponse?) {
        val activity = activity
        if (activity != null && isAdded) {
            events.clear()
            data?.events?.let {
                for (event: Event in it) {
                    events.add(event)
                }
                hideLoading()
                event_recycler?.adapter?.notifyDataSetChanged()
            }?: run {
                event_recycler?.adapter?.notifyDataSetChanged()
                dataNotFound(resources.getString(R.string.data_empty))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventPresenter.detachProcess()
    }

    override fun onPause() {
        super.onPause()
        eventPresenter.detachProcess()
    }

    override fun onStop() {
        super.onStop()
        eventPresenter.detachProcess()
    }

    private fun openCalendar(event: Event) {
        if (!event.dateEvent.isNullOrEmpty() && !event.strTime.isNullOrEmpty()) {
            val eventTime = strToMilliSecond(event.dateEvent + " " + event.strTime)
            val eventTimeNow = strToMilliSecond(dateToStr( Date()))
            if (eventTime > eventTimeNow) {
                val eventTimeEnd = eventTime + minuteToMilliSecond(90)
                val eventTitle = event.strHomeTeam + " vs " + event.strAwayTeam
                val intent = Intent(Intent.ACTION_EDIT)
                intent.type = "vnd.android.cursor.item/event"
                intent.putExtra(CalendarContract.Events.TITLE, eventTitle)
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventTime)
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventTimeEnd)
                intent.putExtra(CalendarContract.Events.ALL_DAY, false)
                intent.putExtra(CalendarContract.Events.DESCRIPTION, event.strDescriptionEN)
                context?.startActivity(intent)
            }
        }
    }
}