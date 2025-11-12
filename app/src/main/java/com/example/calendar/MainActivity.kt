package com.example.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    private var fragmentMonth: FragmentMonth ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        if (fragmentMonth == null){
            fragmentMonth = FragmentMonth()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentMonth,fragmentMonth!!)
                .commit()
        }else if (!fragmentMonth!!.isAdded){
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentMonth,fragmentMonth!!)
                .commit()
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            findViewById<TextView>(R.id.yearOfNow).text = LocalDate.now().year.toString()
        }
        findViewById<FloatingActionButton>(R.id.addButton).setOnClickListener {
            startActivity(Intent(this, ActivitySchedule::class.java))
        }
    }
}