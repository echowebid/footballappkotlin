package com.okedev.footballschedule.base.presenter

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.okedev.footballschedule.base.callback.TeamCallback
import com.okedev.footballschedule.base.view.TeamView
import com.okedev.footballschedule.data.network.repository.EventRepository
import com.okedev.footballschedule.data.network.response.TeamResponse
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TeamPresenterTest {

    @Mock
    private lateinit var view: TeamView

    @Mock
    private lateinit var repository: EventRepository

    @Mock
    private lateinit var response: TeamResponse

    private lateinit var presenter: TeamPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TeamPresenter(view, repository)
    }

    @Test
    fun searchData() {
        val s =  "Barcelona"
        presenter.searchData(s)

        argumentCaptor<TeamCallback<TeamResponse>>().apply {
            verify(repository).getSearchTeam(eq(s), capture())
            firstValue.showData(response)
        }
    }
}