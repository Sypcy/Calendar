package com.example.calendar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calendar.databinding.ActivityScheduleBinding
import android.util.Log
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.icu.util.Calendar
import android.provider.Settings
import java.time.LocalDate

class ActivitySchedule : AppCompatActivity() {
    companion object{
        const val CHANNEL_ID = "AlarmReceiver"
    }
    private lateinit var binding: ActivityScheduleBinding
    private lateinit var dbHelper: DbHelper
    private lateinit var vmSchedule: VMSchedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dbHelper = DbHelper(this,null,1)

        val scheduleList = binding.composeList
        vmSchedule = VMSchedule(dbHelper)

        binding.save.setOnClickListener {
            //需要查日期是否乱写
            val parts = binding.timeEdit.text.toString().split(".")
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val firstDay = LocalDate.of(LocalDate.now().year, parts[1].toInt(), 1)
                val dayOfMonth = firstDay.lengthOfMonth()
                if (parts[1].toInt() <= 12 && parts[1].toInt() >= 1
                    && parts[2].toInt() <= dayOfMonth && parts[2].toInt() >= 1
                ) {
                    vmSchedule.scheduleSave(
                        this,
                        binding.timeEdit.text.toString(),
                        binding.scheduleEdit.text.toString()
                    )
                }else{
                    Toast.makeText(
                        this,
                        "error,please check your date",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.query.setOnClickListener {
            scheduleList.setContent {
                ScheduleList(VMSchedule(dbHelper))
            }
        }
        binding.delete.setOnClickListener {
            val id = binding.idEdit.text.toString().toIntOrNull()
            if (id != null) {
                //删除日程
                vmSchedule.scheduleDelete(id)
                //删除闹钟通知
                deleteAlarm(id)
            }else{
                Toast.makeText(
                    this,
                    "error,please check your id",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.alarm.setOnClickListener {
            val id = binding.idEdit.text.toString().toIntOrNull()
            if (id != null) {
                createChannel()
                setAlarm(id)
            }else{
                Toast.makeText(
                    this,
                    "error,please check your id",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.backOfSchedule.setOnClickListener { finish() }
    }

    private fun setAlarm(id: Int) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val item = vmSchedule.schedules.find { it.first == id.toString() }
        val parts = item!!.second.split(".")
        val year = parts[0].toInt()
        val month = parts[1].toInt()
        val day = parts[2].toInt()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            if (year >= LocalDate.now().year
                && month >= LocalDate.now().monthValue
                && day >= LocalDate.now().dayOfMonth
            ) {
                val intent = Intent(this, AlarmReceiver::class.java)
                intent.putExtra("content", item.third)

                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val calendar: Calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month-1)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    calendar.set(Calendar.HOUR_OF_DAY, 8)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)

//                calendar.add(Calendar.SECOND, 5)
                val alarmTime: Long = calendar.getTimeInMillis()

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    if (!alarmManager.canScheduleExactAlarms()) {
                        startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                    } else {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            alarmTime,
                            pendingIntent
                        )
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "your date is before now",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i(
                    "test",
                    "error"
                )
            }
        }
    }

    private fun createChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarm",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun deleteAlarm(id: Int){
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }
}