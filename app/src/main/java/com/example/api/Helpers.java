package com.example.api;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Helpers {

    @SuppressLint("NewApi")
    public static String getTime(String interval) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        switch (interval){

            case "Weekly": date = Date.from((LocalDate.now().plusWeeks(1)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()); break;
            case "Monthly": date = Date.from((LocalDate.now().plusMonths(1)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()); break;
            case "Quarter yearly": date = Date.from((LocalDate.now().plusMonths(3)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()); break;
            case "Half yearly": date = Date.from((LocalDate.now().plusMonths(6)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()); break;
            case "Yearly": date = Date.from((LocalDate.now().plusYears(1)).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()); break;

        }
        return formatter.format(date).toString();
    }
}
