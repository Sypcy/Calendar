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
import androidx.activity.compose.setContent
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
                Date(month, 40, 20)

            }
            val year = LocalDate.now().year.toString()
            findViewById<TextView>(R.id.monthOfNow).text = year+"年"+month+"月"

            findViewById<Button>(R.id.backOfDate).setOnClickListener { finish() }
        }

    }
}