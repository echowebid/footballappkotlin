package com.okedev.footballschedule.base.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.okedev.footballschedule.BuildConfig
import com.okedev.footballschedule.R
import com.okedev.footballschedule.base.activity.MainActivity
import com.okedev.footballschedule.base.activity.SearchActivity
import com.okedev.footballschedule.base.adapter.MainEventViewPagerAdapter
import com.okedev.footballschedule.data.PreferencesHelper
import kotlinx.android.synthetic.main.fragment_main_event.*
import org.jetbrains.anko.startActivity
import java.util.*

class MainEventFragment : Fragment() {

    private var defLeagueId = BuildConfig.LEAGUE_ID
    private var isSpinnerTouched = false
    private var mapLeague = HashMap<String, Int>()
    @SuppressLint("UseSparseArrays")
    private var mapLeagueKey = HashMap<Int, Int>()
    private var menuItem: Menu? = null
    private var preferencesHelper: PreferencesHelper? = null
    private var selectedLeague: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setSupportActionBar(main_event_toolbar)
        (activity as MainActivity).supportActionBar?.title = resources.getString(R.string.event_title)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferencesHelper = PreferencesHelper(context)
        preferencesHelper?.leagueId.let {
            defLeagueId = it
        }

        loadSpinner(defLeagueId)
        setupViewPager(main_event_viewpager)
        main_event_tab?.setupWithViewPager(main_event_viewpager)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater)  {
        inflater.inflate(R.menu.search_static, menu)
        menuItem = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search_static -> {
                context?.startActivity<SearchActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        preferencesHelper?.leagueId.let {
            defLeagueId = it
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = MainEventViewPagerAdapter(fragmentManager)
        adapter.addFragment(EventFragment.newInstance(1), "LAST")
        adapter.addFragment(EventFragment.newInstance(2), "NEXT")
        viewPager.adapter = adapter
        viewPager.adapter?.notifyDataSetChanged()
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

        main_event_spinner?.let { this_spinner ->
            this_spinner.adapter = spinnerAdapter
            this_spinner.setOnTouchListener { _ , _ ->
                isSpinnerTouched = true
                false
            }
            this_spinner.setSelection(mapLeagueKey.getValue(leagueSelected))
            this_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    if (isSpinnerTouched) {
                        selectedLeague = main_event_spinner?.selectedItem.toString()
                        mapLeague.getValue(selectedLeague).let {
                            preferencesHelper?.leagueId = it
                            isSpinnerTouched = false
                            setupViewPager(main_event_viewpager)
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }
}
