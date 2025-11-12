package com.example.calendar

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DbHelper(context: Context,factory: SQLiteDatabase.CursorFactory?,version:Int):
    SQLiteOpenHelper(context,DATABASE_NAME,factory,version) {
        companion object{
            const val DATABASE_NAME = "Schedule.db"
            const val TABLE_SCHEDULE = "schedule"
            const val SCHEDULE_ID = "id"
            const val SCHEDULE_DATE = "date"
            const val SCHEDULE_CONTENT = "content"

            const val TAG = "DbHelper"
        }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSchedule = """
            CREATE TABLE $TABLE_SCHEDULE(
            $SCHEDULE_ID INTEGER PRIMARY KEY,
            $SCHEDULE_DATE TEXT NOT NULL,
            $SCHEDULE_CONTENT TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTableSchedule)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(
            TAG,
            "oldVersion=$oldVersion,newVersion=$newVersion,dbVersion=${db?.version}"
        )
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SCHEDULE")
    }
}