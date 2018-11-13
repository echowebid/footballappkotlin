package com.okedev.footballschedule.base.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.okedev.footballschedule.R
import com.okedev.footballschedule.base.adapter.EventAdapter
import com.okedev.footballschedule.base.presenter.EventPresenter
import com.okedev.footballschedule.base.view.EventView
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.Event
import com.okedev.footballschedule.data.network.response.EventResponse
import com.okedev.footballschedule.utils.*
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh
import java.util.*

class SearchActivity : AppCompatActivity(), EventView {

    private var events: MutableList<Event> = mutableListOf()
    private var s: String? = ""
    private var isMenu: Menu? = null

    private lateinit var eventPresenter: EventPresenter
    private lateinit var searchView: SearchView
    private lateinit var searchManager: SearchManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(search_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        eventPresenter = EventPresenter(this, EventRepository())

        search_recycler.layoutManager = LinearLayoutManager(this)
        search_recycler.adapter = EventAdapter(events) { event, alarm ->
            if (alarm == true) {
                openCalendar(event)
            } else {
                startActivity<DetailEventActivity>("eventId" to event.idEvent?.toInt())
            }
        }

        hideLoading()
        dataNotFound(resources.getString(R.string.data_empty))

        search_swipe_refresh?.onRefresh {
            getSearch(s)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean  {
        menuInflater.inflate(R.menu.search, menu)
        isMenu = menu

        searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.onActionViewExpanded()
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnSearchClickListener {
            loadQuery("null")
        }
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (query.isNotEmpty()) loadQuery(query)
                    if (query.isEmpty()) loadQuery("")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.length > 1) loadQuery(newText)
                    if (newText.isEmpty()) loadQuery("")
                }
                return false
            }
        })
        searchView.setOnCloseListener {
            loadQuery("")
            false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showLoading() {
        search_swipe_refresh?.let {
            if (!it.isRefreshing) {
                search_progressbar?.visible()
            }
        }
        search_message?.gone()
    }

    override fun hideLoading() {
        search_progressbar?.invisible()
        search_swipe_refresh?.let {
            if (it.isRefreshing) {
                it.isRefreshing = false
            }
        }
    }

    override fun dataNotFound(str: String?) {
        hideLoading()
        search_message?.let {
            it.text = if (str.isNullOrEmpty()) resources.getString(R.string.data_error) else str
            it.visible()
        }
    }

    override fun showData(data: EventResponse?) {
        events.clear()
        data?.event?.let {
            for (event: Event in it) {
                events.add(event)
            }
            hideLoading()
            search_recycler?.adapter?.notifyDataSetChanged()
        }?: run {
            search_recycler?.adapter?.notifyDataSetChanged()
            dataNotFound(resources.getString(R.string.data_empty))
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

    private fun loadQuery(str: String?) {
        getSearch(str)
    }

    private fun getSearch(str: String?) {
        s = str
        eventPresenter.searchData(str)
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
                startActivity(intent)
            }
        }
    }

}
