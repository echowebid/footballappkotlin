package com.okedev.footballschedule.base.presenter

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.okedev.footballschedule.base.callback.EventCallback
import com.okedev.footballschedule.base.view.EventView
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.EventResponse

import org.junit.Test
import org.junit.Before
import org.mockito.*

class EventPresenterTest {

    @Mock
    private lateinit var view: EventView

    @Mock
    private lateinit var eventRepository: EventRepository

    @Mock
    private lateinit var eventResponse: EventResponse

    private lateinit var eventPresenter: EventPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        eventPresenter = EventPresenter(view, eventRepository)
    }

    @Test
    fun testGetData() {

        val leagueId =  4328
        eventPresenter.getData(leagueId, 1)

        argumentCaptor<EventCallback<EventResponse>>().apply {
            verify(eventRepository).getPastMatch(eq(leagueId), capture())
            firstValue.showData(eventResponse)
        }
    }

    @Test
    fun testGetDataError() {

        eventPresenter.getData(0, 1)

        argumentCaptor<EventCallback<EventResponse>>().apply {
            verify(eventRepository).getPastMatch(eq(0), capture())
            firstValue.dataNotFound("")
        }
    }
}