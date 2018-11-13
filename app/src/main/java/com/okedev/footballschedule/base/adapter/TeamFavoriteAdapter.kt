package com.okedev.footballschedule.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.okedev.footballschedule.R
import com.okedev.footballschedule.data.db.model.TeamTable
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.sdk27.coroutines.onClick

class TeamFavoriteAdapter (
        private val teams: List<TeamTable>,
        private val listener: (TeamTable) -> Unit):
        RecyclerView.Adapter<TeamFavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TeamUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(teams[position], listener)
    }

    override fun getItemCount(): Int = teams.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val teamBadge: ImageView = view.find(R.id.team_badge)
        private val teamName: TextView = view.find(R.id.team_name)

        fun bindItem(team: TeamTable, listener: (TeamTable) -> Unit) {
            if (team.teamBadge.isNullOrEmpty()) {
                teamBadge.setImageResource(R.drawable.ic_team)
            } else {
                Glide.with(itemView).load(team.teamBadge).into(teamBadge)
            }
            teamName.text = team.teamName
            itemView.onClick { listener(team) }
        }
    }

    class TeamUI : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View {
            return with(ui) {
                cardView {
                    cardElevation = 0f
                    lparams(width = matchParent, height = wrapContent) {
                        bottomMargin = dip(16)
                        radius = 4f
                    }
                    linearLayout {
                        lparams(width = matchParent, height = wrapContent)
                        padding = dip(16)
                        orientation = LinearLayout.HORIZONTAL

                        imageView {
                            id = R.id.team_badge
                            setImageResource(R.drawable.ic_team)
                        }.lparams {
                            height = dip(50)
                            width = dip(50)
                        }

                        textView {
                            id = R.id.team_name
                            textSize = 16f
                        }.lparams {
                            margin = dip(15)
                        }

                    }
                }
            }
        }
    }
}