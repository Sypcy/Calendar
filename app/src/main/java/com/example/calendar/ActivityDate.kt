package com.example.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calendar.databinding.ActivityDateBinding
import java.time.LocalDate
import android.widget.Button
import android.content.Intent
import android.util.Log
import androidx.activity.compose.setContent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nlf.calendar.Holiday
import com.nlf.calendar.Solar

class ActivityDate : AppCompatActivity() {
    private lateinit var binding: ActivityDateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //得到composeView实例
            val date = binding.composeDate
            val month = intent.getIntExtra("month", 1)
            //date.表示把控件放入composeView里
            date.setContent {
                Date(month, 40, 20,1)
            }
            val year = LocalDate.now().year.toString()
            binding.monthOfNow.text = year+"年"+month+"月"

            holidays(month)
            binding.backOfDate.setOnClickListener { finish() }
        }
    }
    private fun holidays(month: Int){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val days = LocalDate.of(LocalDate.now().year, month, 1).lengthOfMonth()
            for (i in 1..days){
                val solar = Solar.fromYmd(LocalDate.now().year,month,i)
                val lunar = solar.lunar

                val solarHoliday = solar.festivals
                val lunarHoliday = lunar.festivals


                if (solarHoliday.isNotEmpty()){
                    binding.holiday.append( "${solar.month}月${solar.day}日: ${solarHoliday.joinToString() }\n")
                }else if (lunarHoliday.isNotEmpty()){
                    binding.holiday.append( "${solar.month}月${solar.day}日: ${lunarHoliday.joinToString()}\n")
                }
            }

        }
    }
}