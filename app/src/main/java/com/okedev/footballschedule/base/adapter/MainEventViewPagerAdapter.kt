package com.okedev.footballschedule.base.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class MainEventViewPagerAdapter(manager: FragmentManager?) : FragmentStatePagerAdapter(manager) {

    private var mFragmentList: MutableList<Fragment> =  mutableListOf()
    private var mFragmentTitleList: MutableList<String> = mutableListOf()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }
}