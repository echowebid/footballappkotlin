package com.okedev.footballschedule.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.okedev.footballschedule.data.db.model.EventTable
import com.okedev.footballschedule.data.db.model.PlayerTable
import com.okedev.footballschedule.data.db.model.TeamTable
import org.jetbrains.anko.db.*

class MyDBHelper  (ctx: Context) : ManagedSQLiteOpenHelper(ctx, "Favorite.db", null, 1) {
    companion object {
        private var instance: MyDBHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDBHelper {
            if (instance == null) {
                instance = MyDBHelper(ctx.applicationContext)
            }
            return instance as MyDBHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables

        db.createTable(EventTable.EVENT_FAVORITE, true,
            EventTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            EventTable.EVENT_ID to TEXT + UNIQUE,
            EventTable.EVENT_NAME to TEXT,
            EventTable.EVENT_DATE to TEXT,
            EventTable.EVENT_TIME to TEXT,
            EventTable.HOME_TEAM_ID to TEXT,
            EventTable.HOME_TEAM_NAME to TEXT,
            EventTable.HOME_TEAM_SCORE to TEXT,
            EventTable.AWAY_TEAM_ID to TEXT,
            EventTable.AWAY_TEAM_NAME to TEXT,
            EventTable.AWAY_TEAM_SCORE to TEXT,
            EventTable.LEAGUE_ID to TEXT,
            EventTable.LEAGUE_NAME to TEXT)

        db.createTable(TeamTable.TEAM_FAVORITE, true,
                TeamTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                TeamTable.TEAM_ID to TEXT + UNIQUE,
                TeamTable.TEAM_NAME to TEXT,
                TeamTable.TEAM_BADGE to TEXT)

        db.createTable(PlayerTable.PLAYER_FAVORITE, true,
                PlayerTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                PlayerTable.PLAYER_ID to TEXT + UNIQUE,
                PlayerTable.PLAYER_NAME to TEXT,
                PlayerTable.PLAYER_POSITION to TEXT,
                PlayerTable.PLAYER_BANNER to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(EventTable.EVENT_FAVORITE, true)

        db.dropTable(TeamTable.TEAM_FAVORITE, true)

        db.dropTable(PlayerTable.PLAYER_FAVORITE, true)
    }
}

// Access property for Context
val Context.database: MyDBHelper
    get() = MyDBHelper.getInstance(applicationContext)