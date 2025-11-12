package com.example.calendar

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.mutableStateListOf
import android.util.Log


class VMSchedule(private val dbHelper: DbHelper): ViewModel(){
    private val _schedule = mutableStateListOf<Triple<String,String,String>>()
    val schedules: List<Triple<String,String, String>> = _schedule

    init {
        scheduleLoad()
    }

    private fun scheduleLoad(){
        viewModelScope.launch(Dispatchers.IO) {
            val db = dbHelper.readableDatabase
            var time: String
            var schedule: String
            var id: Int
            val result = mutableStateListOf<Triple< String,String, String>>()
            val cursor = db.query(
                DbHelper.TABLE_SCHEDULE,
                null,null,null,null,null,null
            )
            with(cursor){
                while (moveToNext()){
                    id = getInt(getColumnIndexOrThrow(DbHelper.SCHEDULE_ID))
                    time = getString(getColumnIndexOrThrow(DbHelper.SCHEDULE_DATE))
                    schedule = getString(getColumnIndexOrThrow(DbHelper.SCHEDULE_CONTENT))
                    result.add(Triple(id.toString(),time,schedule))
                }
            }
            cursor.close()
            db.close()
            withContext(Dispatchers.Main){
                _schedule.clear()
                _schedule.addAll(result)
            }
        }
    }

    fun scheduleSave(context: Context,date: String,schedule: String){
        viewModelScope.launch(Dispatchers.IO){
            if (date.isNotEmpty() && schedule.isNotEmpty()){
                val db = dbHelper.writableDatabase
                val value = android.content.ContentValues().apply {
                    put(DbHelper.SCHEDULE_DATE,date)
                    put(DbHelper.SCHEDULE_CONTENT,schedule)
                }
                db.insert(DbHelper.TABLE_SCHEDULE,null,value)
                db.close()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "successfully save",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "error,please input something",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    fun scheduleDelete(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val db = dbHelper.writableDatabase
            db.delete(
                DbHelper.TABLE_SCHEDULE,
                "${DbHelper.SCHEDULE_ID} = ?",
                arrayOf(id.toString()),
            )
            db.close()
        }
    }
}