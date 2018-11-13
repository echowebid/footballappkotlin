package com.okedev.footballschedule.base.adapter

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.okedev.footballschedule.R
import com.okedev.footballschedule.data.db.model.EventTable
import com.okedev.footballschedule.utils.formatDate
import com.okedev.footballschedule.utils.formatTime
import com.okedev.footballschedule.utils.strToDate
import com.okedev.footballschedule.utils.strToTime
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class EventFavoriteAdapter (private val events: List<EventTable>,
                            private val listener: (EventTable) -> Unit):
        RecyclerView.Adapter<EventFavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(EventFavoriteUI().createView(AnkoContext.create(parent.context, parent)))
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

        fun bindItem(event: EventTable, listener: (EventTable) -> Unit) {

            val label = if (event.homeTeamScore.isNullOrEmpty() && event.awayTeamScore.isNullOrEmpty()) "VS" else "-"
            val schedule = formatDate(strToDate(event.eventDate))
            val time = if (!event.eventTime.isNullOrEmpty()) formatTime(strToTime(event.eventTime?.substring(0, 8))) else ""

            awayScore.text = event.awayTeamScore
            awayTeam.text = event.awayTeamName
            homeScore.text = event.homeTeamScore
            homeTeam.text = event.homeTeamName
            eventSchedule.text = schedule
            eventTime.text = time
            eventLabel.text = label

            itemView.setOnClickListener {
                listener(event)
            }
        }
    }

    class EventFavoriteUI : AnkoComponent<ViewGroup> {
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

                    themedTextView(R.style.TextLight) {
                        id = R.id.event_schedule
                    }.lparams(width = wrapContent, height = wrapContent){
                        gravity = Gravity.CENTER
                        bottomMargin = dip(8)
                    }

                    themedTextView(R.style.TextLight) {
                        id = R.id.event_time
                    }.lparams(width = wrapContent, height = wrapContent){
                        gravity = Gravity.CENTER
                        bottomMargin = dip(8)
                    }

                    linearLayout {
                        lparams(width = matchParent, height = wrapContent){
                            orientation = LinearLayout.HORIZONTAL
                            weightSum = 5f
                        }

                        themedTextView(R.style.SubListTeam){
                            id = R.id.home_team
                            gravity = Gravity.END
                        }.lparams(width = 0, height = wrapContent){
                            weight = 2f
                        }

                        linearLayout {
                            lparams(width = 0, height = wrapContent){
                                orientation = LinearLayout.HORIZONTAL
                                weight = 1f
                                weightSum = 3f
                            }

                            themedTextView(R.style.SubList){
                                id = R.id.home_score
                                gravity = Gravity.END
                            }.lparams(height = wrapContent){
                                weight = 1f
                            }

                            themedTextView(R.style.SubList){
                                id = R.id.event_label
                                gravity = Gravity.CENTER
                                setTypeface(null, Typeface.NORMAL)
                                text = "-"
                            }.lparams(height = wrapContent){
                                weight = 1f
                            }

                            themedTextView(R.style.SubList){
                                id = R.id.away_score
                                gravity = Gravity.START
                            }.lparams(height = wrapContent){
                                weight = 1f
                            }
                        }

                        themedTextView(R.style.SubListTeam){
                            id = R.id.away_team
                            gravity = Gravity.START
                        }.lparams(width = 0, height = wrapContent){
                            weight = 2f
                        }
                    }
                }
            }
        }
    }
}