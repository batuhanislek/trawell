package com.trawell.batu.trawell.TaskManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Batuhan Islek on 22.04.2018.
 */
public class DateCalculation {

    public long DaysBetweenDates(String date1, String date2) {
        long daysDiff=0;
        try {
            String format = "dd/M/yyyy";
            SimpleDateFormat formatter = new SimpleDateFormat(format);

            Date d1 = formatter.parse(date1);
            Date d2 = formatter.parse(date2);

            long diff = d2.getTime() - d1.getTime();
            daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return daysDiff;
    }
}
