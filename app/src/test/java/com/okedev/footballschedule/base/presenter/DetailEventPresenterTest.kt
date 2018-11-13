package com.okedev.footballschedule.base.presenter

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.okedev.footballschedule.base.callback.AwayTeamCallback
import com.okedev.footballschedule.base.callback.HomeTeamCallback
import com.okedev.footballschedule.base.view.DetailEventView
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.Team
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DetailEventPresenterTest {

    @Mock
    private lateinit var view: DetailEventView

    @Mock
    private lateinit var eventRepository: EventRepository

    @Mock
    private lateinit var team: Team

    private lateinit var detailEventPresenter: DetailEventPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        detailEventPresenter = DetailEventPresenter(view, eventRepository)
    }

    @Test
    fun testGetTeam() {
        val teamId =  133604
        detailEventPresenter.getHomeTeam(teamId)

        argumentCaptor<HomeTeamCallback<Team>>().apply {
            verify(eventRepository).getHomeTeam(eq(teamId), capture())
            firstValue.showHomeTeam(team)
        }
    }
}