package com.okedev.footballschedule.base.fragment

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import com.okedev.footballschedule.BuildConfig
import com.okedev.footballschedule.R
import com.okedev.footballschedule.base.activity.DetailTeamActivity
import com.okedev.footballschedule.base.activity.MainActivity
import com.okedev.footballschedule.base.adapter.TeamAdapter
import com.okedev.footballschedule.base.presenter.TeamPresenter
import com.okedev.footballschedule.base.view.TeamView
import com.okedev.footballschedule.data.PreferencesHelper
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.Team
import com.okedev.footballschedule.data.network.response.TeamResponse
import com.okedev.footballschedule.utils.gone
import com.okedev.footballschedule.utils.invisible
import com.okedev.footballschedule.utils.visible
import kotlinx.android.synthetic.main.fragment_team.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh
import java.util.*

class TeamFragment : Fragment(), TeamView {
    private var defLeagueId = BuildConfig.LEAGUE_ID
    private var isSearch = false
    private var isSpinnerTouched = false
    private var mapLeague = HashMap<String, Int>()
    @SuppressLint("UseSparseArrays")
    private var mapLeagueKey = HashMap<Int, Int>()
    private var preferencesHelper: PreferencesHelper? = null
    private var s: String? = ""
    private var selectedLeague: String = ""
    private var teams: MutableList<Team> = mutableListOf()

    private lateinit var teamPresenter: TeamPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setSupportActionBar(team_toolbar)
        (activity as MainActivity).supportActionBar?.title = resources.getString(R.string.team_title)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferencesHelper = PreferencesHelper(context)
        preferencesHelper?.leagueId.let {
            defLeagueId = it
        }

        loadSpinner(defLeagueId)

        teamPresenter = TeamPresenter(this, EventRepository())

        team_recycler.layoutManager = LinearLayoutManager(context)
        team_recycler.adapter = TeamAdapter(teams) {
            context?.startActivity<DetailTeamActivity>("teamId" to it.idTeam?.toInt())
        }

        getData(defLeagueId)

        team_swipe_refresh?.onRefresh {
            if (isSearch) {
                loadQuery(s)
            } else {
                getData(defLeagueId)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater)  {
        inflater.inflate(R.menu.search, menu)

        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.setOnSearchClickListener {
            loadQuery("null")
        }
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (query.isNotEmpty()) loadQuery(query)
                    if (query.isEmpty()) loadQuery("null")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.length > 1) loadQuery(newText)
                    if (newText.isEmpty()) loadQuery("null")
                }
                return false
            }
        })
        searchView.setOnCloseListener {
            loadQuery("")
            false
        }
        super.onCreateOptionsMenu(menu, inflater)

    }

    private fun getData(l: Int) {
        team_spinner.visible()

        val param = team_recycler.layoutParams as RelativeLayout.LayoutParams
        param.setMargins(0,0,0,0)
        team_layout.layoutParams = param

        isSearch = false
        teamPresenter.getData(l)
    }

    private fun getSearch(str: String?) {
        team_spinner.gone()

        val param = team_recycler.layoutParams as RelativeLayout.LayoutParams
        param.setMargins(0,16,0,0)
        team_layout.layoutParams = param

        isSearch = true
        s = str
        teamPresenter.searchData(str)
    }

    override fun showLoading() {
        team_swipe_refresh?.let {
            if (!it.isRefreshing) {
                team_progressbar?.visible()
            }
        }
        team_message?.gone()
    }

    override fun hideLoading() {
        team_progressbar?.invisible()
        team_swipe_refresh?.let {
            if (it.isRefreshing) {
                it.isRefreshing = false
            }
        }
    }

    override fun dataNotFound(str: String?) {
        hideLoading()
        team_message?.let {
            it.text = if (str.isNullOrEmpty()) resources.getString(R.string.data_error) else str
            it.visible()
        }
    }

    override fun showData(data: TeamResponse?) {
        val activity = activity
        if (activity != null && isAdded) {
            teams.clear()
            data?.teams?.let {
                for (team: Team in it) {
                    teams.add(team)
                }
                hideLoading()
                team_recycler?.adapter?.notifyDataSetChanged()
            }?: run {
                team_recycler?.adapter?.notifyDataSetChanged()
                dataNotFound(resources.getString(R.string.data_empty))
            }
        }
    }

    private fun loadSpinner(leagueSelected: Int) {
        val arrayLeague = resources.getStringArray(R.array.leagues)
        val arrayLeagueId = resources.getIntArray(R.array.league_id)
        var ii = 0
        arrayLeague.indices.forEach { i ->
            mapLeague[arrayLeague[i]] = arrayLeagueId[i]
            mapLeagueKey[arrayLeagueId[i]] = ii
            ii++
        }
        val spinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, arrayLeague)

        team_spinner?.let { this_spinner ->
            this_spinner.adapter = spinnerAdapter
            this_spinner.setOnTouchListener { _ , _ ->
                isSpinnerTouched = true
                false
            }
            this_spinner.setSelection(mapLeagueKey.getValue(leagueSelected))
            this_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    if (isSpinnerTouched) {
                        selectedLeague = team_spinner?.selectedItem.toString()
                        mapLeague.getValue(selectedLeague).let {
                            defLeagueId = it
                            isSpinnerTouched = false
                            preferencesHelper?.leagueId = it
                            getData(it)
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        teamPresenter.detachProcess()
    }

    override fun onPause() {
        super.onPause()
        teamPresenter.detachProcess()
    }

    override fun onStop() {
        super.onStop()
        teamPresenter.detachProcess()
    }

    private fun loadQuery(str: String?) {
        if (!str.isNullOrEmpty() && !str.equals("null")) {
            getSearch(str)
        } else {
            getData(defLeagueId)
        }
    }
}
