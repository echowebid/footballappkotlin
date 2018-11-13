package com.okedev.footballschedule.base.adapter

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.okedev.footballschedule.R
import com.okedev.footballschedule.data.network.response.Player
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class TeamPlayerAdapter(private val players: List<Player>,
                        private val listener: (Player) -> Unit) :
        RecyclerView.Adapter<TeamPlayerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PlayerUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(players[position], listener)
    }

    override fun getItemCount(): Int = players.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val playerBanner: ImageView = view.find(R.id.player_banner)
        private val playerName: TextView = view.find(R.id.player_name)
        private val playerPosition: TextView = view.find(R.id.player_position)

        fun bindItem(player: Player, listener: (Player) -> Unit) {
            if (player.strCutout.isNullOrEmpty()) {
                playerBanner.setImageResource(R.drawable.ic_player)
            } else {
                Glide.with(itemView).load(player.strCutout).into(playerBanner)
            }
            playerName.text = player.strPlayer
            playerPosition.text = player.strPosition
            itemView.setOnClickListener {
                listener(player)
            }
        }
    }

    class PlayerUI : AnkoComponent<ViewGroup> {
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
                            id = R.id.player_banner
                            setImageResource(R.drawable.ic_player)
                        }.lparams {
                            height = dip(50)
                            width = dip(50)
                            rightMargin = dip(8)
                        }

                        linearLayout {
                            lparams(width = matchParent, height = wrapContent)
                            orientation = LinearLayout.VERTICAL

                            themedTextView(R.style.SubList) {
                                id = R.id.player_name
                                textSize = 16f
                                setTypeface(null, Typeface.NORMAL)
                            }.lparams {
                                topMargin = dip(8)
                            }

                            themedTextView(R.style.TextLight) {
                                id = R.id.player_position
                            }
                        }

                    }
                }
            }
        }

    }

}