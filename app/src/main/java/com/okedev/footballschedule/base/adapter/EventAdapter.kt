package com.okedev.footballschedule.base.adapter

import android.graphics.Typeface.NORMAL
import android.support.v7.widget.RecyclerView
import android.view.Gravity.*
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout.HORIZONTAL
import android.widget.TextView
import com.okedev.footballschedule.R
import com.okedev.footballschedule.data.network.response.Event
import com.okedev.footballschedule.utils.*
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import java.util.*

class EventAdapter (private val events: List<Event>,
                    private val listener: (Event, setAlarm: Boolean) -> Unit):
        RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(EventUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(events[position], listener)
    }

    override fun getItemCount(): Int = events.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val awayScore: TextView = itemView.find(R.id.away_score)
        private val awayTeam: TextView = itemView.find(R.id.away_team)
        private val eventLabel: TextView = itemView.find(R.id.event_label)
        private val eventSchedule: TextView = itemView.find(R.id.event_schedule)
        private val eventTime: TextView = itemView.find(R.id.event_time)
        private val homeScore: TextView = itemView.find(R.id.home_score)
        private val homeTeam: TextView = itemView.find(R.id.home_team)
        private val eventAlarm: ImageView = itemView.find(R.id.event_alarm)

        fun bindItem(event: Event, listener: (Event, setAlarm: Boolean) -> Unit) {
            val label = if (event.intHomeScore.isNullOrEmpty() && event.intAwayScore.isNullOrEmpty()) "VS" else "-"
            val schedule = formatDate(strToDate(event.dateEvent))
            val time = if (!event.strTime.isNullOrEmpty()) formatTime(strToTime(event.strTime?.substring(0, 8))) else ""

            awayScore.text = event.intAwayScore
            awayTeam.text = event.strAwayTeam
            homeScore.text = event.intHomeScore
            homeTeam.text = event.strHomeTeam
            eventSchedule.text = schedule
            eventTime.text = time
            eventLabel.text = label

            eventAlarm.invisible()
            if (!event.dateEvent.isNullOrEmpty() && !event.strTime.isNullOrEmpty()) {
                val eventTime = strToMilliSecond(event.dateEvent + " " + event.strTime)
                val eventTimeNow = strToMilliSecond(dateToStr(Date()))
                if (eventTime > eventTimeNow) {
                    eventAlarm.visible()
                    eventAlarm.setOnClickListener {
                        listener(event, true)
                    }
                }
            }

            itemView.setOnClickListener {
                listener(event, false)
            }
        }
    }

    class EventUI : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
            cardView {

                cardElevation = 0f

                lparams(width = matchParent, height = wrapContent){
                    bottomMargin = dip(16)
                    radius = 4f
                }

                verticalLayout {

                    lparams(width = matchParent, height = wrapContent) {
                        padding = dip(16)
                    }

                    relativeLayout {

                        imageView {
                            id = R.id.event_alarm
                            setImageResource(R.drawable.ic_alarm)
                        }.lparams {
                            height = dip(28)
                            width = dip(28)
                            alignParentRight()
                        }

                        themedTextView(R.style.TextLight) {
                            id = R.id.event_schedule
                        }.lparams(width = wrapContent, height = wrapContent){
                            bottomMargin = dip(8)
                            centerHorizontally()
                        }

                        themedTextView(R.style.TextLight) {
                            id = R.id.event_time
                        }.lparams(width = wrapContent, height = wrapContent){
                            bottomMargin = dip(8)
                            below(R.id.event_schedule)
                            centerHorizontally()
                        }
                    }

                    linearLayout {
                        lparams(width = matchParent, height = wrapContent){
                            orientation = HORIZONTAL
                            weightSum = 5f
                        }

                        themedTextView(R.style.SubListTeam){
                            id = R.id.home_team
                            gravity = END
                        }.lparams(width = 0, height = wrapContent){
                            weight = 2f
                        }

                        linearLayout {
                            lparams(width = 0, height = wrapContent){
                                orientation = HORIZONTAL
                                weight = 1f
                                weightSum = 3f
                            }

                            themedTextView(R.style.SubList){
                                id = R.id.home_score
                                gravity = END
                            }.lparams(height = wrapContent){
                                weight = 1f
                            }

                            themedTextView(R.style.SubList){
                                id = R.id.event_label
                                gravity = CENTER
                                setTypeface(null, NORMAL)
                                text = "-"
                            }.lparams(height = wrapContent){
                                weight = 1f
                            }

                            themedTextView(R.style.SubList){
                                id = R.id.away_score
                                gravity = START
                            }.lparams(height = wrapContent){
                                weight = 1f
                            }
                        }

                        themedTextView(R.style.SubListTeam){
                            id = R.id.away_team
                            gravity = START
                        }.lparams(width = 0, height = wrapContent){
                            weight = 2f
                        }
                    }
                }
            }
        }
    }
}