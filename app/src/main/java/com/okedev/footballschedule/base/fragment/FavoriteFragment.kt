package com.okedev.footballschedule.base.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.okedev.footballschedule.R
import com.okedev.footballschedule.base.activity.MainActivity
import com.okedev.footballschedule.base.adapter.FavoriteViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setSupportActionBar(favorite_toolbar)
        (activity as MainActivity).supportActionBar?.title = resources.getString(R.string.favorite_title)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViewPager(favorite_viewpager)
        favorite_tab?.setupWithViewPager(favorite_viewpager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = FavoriteViewPagerAdapter(fragmentManager)
        adapter.addFragment(EventFavoriteFragment(), "MATCH")
        adapter.addFragment(TeamFavoriteFragment(), "TEAM")
        adapter.addFragment(PlayerFavoriteFragment(), "PLAYER")
        viewPager.adapter = adapter
        viewPager.adapter?.notifyDataSetChanged()
    }


}
