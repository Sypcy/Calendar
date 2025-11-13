package com.example.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.LocalDate
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import android.util.Log
import com.nlf.calendar.Lunar
import com.nlf.calendar.Solar

@Composable
fun Date(month: Int,sizeOfGrid: Int,sizeOfText: Int,judge: Int){
    Column(Modifier.height(400.dp)) {

        val weekTitle = listOf("日", "一", "二", "三", "四", "五", "六")
        LazyVerticalGrid(GridCells.Fixed(7)) {
            items(weekTitle.size) { grid ->
                Box(
                    Modifier
                        .size(sizeOfGrid.dp)
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(
                        weekTitle[grid],
                        fontSize = sizeOfText.sp
                    )
                }
            }
        }
            //每月有多少blanks，数字和blanks放一个数组，再输出
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val firstDay = LocalDate.of(LocalDate.now().year, month, 1)
            val weekOfFDay = firstDay.dayOfWeek.value
            val daysOfMonth = firstDay.lengthOfMonth()

            val blanks = if (weekOfFDay == 7) 0 else weekOfFDay
            val dates = buildList {
                repeat(blanks) { add(" ") }
                repeat(daysOfMonth) { add("${it + 1}") }
            }

            val lunarDate = buildList {
                repeat(blanks) { add(" ") }
                repeat(daysOfMonth) { add(lunarDates(month,it+1)) }
            }

            LazyVerticalGrid(GridCells.Fixed(7)) {
                items(dates.size) { day ->
                    Box(
                        Modifier
                            .size(sizeOfGrid.dp)
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Column {
                            Text(
                                dates[day],
                                fontSize = sizeOfText.sp
                            )
                            if (judge == 1) {
                                Text(
                                    lunarDate[day],
                                    fontSize = (sizeOfText / 1.5).sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleList(viewModel: VMSchedule){
    Column(
        Modifier
            .padding(12.dp)
            .background(
                color = Color.Gray,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        for((id ,time, schedule) in viewModel.schedules) {
            Text(
                "$id、$time：日程：$schedule",
                fontSize = 20.sp,
                )
        }
    }
}

private fun lunarDates(month: Int,day: Int): String{
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val solar = Solar.fromYmd(LocalDate.now().year, month, day)
        val lunarGrid: String =
            if (solar.lunar.dayInChinese != "初一") {
                solar.lunar.dayInChinese
            } else {
                solar.lunar.monthInChinese + "月"
            }
        return lunarGrid
    }else{return "null"}
}
