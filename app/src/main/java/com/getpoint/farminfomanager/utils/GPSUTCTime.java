package com.getpoint.farminfomanager.utils;

/**
 * Created by Gui Zhou on 2016-07-08.
 */
public class GPSUTCTime {

    public int year;
    public short month;
    public short day;
    public short hour;
    public short minute;
    public short second;

    public GPSUTCTime(int gpsWeek, long gpsMs) {

        long allSecond = gpsMs / 1000L;

        gpsCalendar(gpsWeekToday(gpsWeek, allSecond / (3600 * 24)));

        long lessDay = allSecond % (3600 * 24);
        long lessHour = lessDay % 3600;

        this.hour = (short) ((lessDay / 3600 + 8) % 24);
        this.minute = (short) (lessHour / 60);
        this.second = (short) (lessHour % 60);

    }

    public double gpsWeekToday(int gpsweek, long day) {

        double calDate1 = 1980;
        double calDate2 = 1;
        double calDate3 = 6;

        long upJulCal;
        long frGreCal;
        long curDate;

        double B = 0;
        double today;

        if (calDate2 <= 2) {
            calDate1 -= 1;
            calDate2 += 12;
        }

        upJulCal = 4 + 31 * (10 + 12 * 1582);
        frGreCal = 15 + 31 * (10 + 12 * 1582);
        curDate = (long) (calDate3 + 31 * (calDate2 + 12 * calDate1));

        if (curDate <= upJulCal) {
            B = -2;
        } else if (curDate >= frGreCal) {
            B = (long) (calDate1 / 400) - (long) (calDate1 / 100);
        }

        if (calDate1 > 0) {
            today = (long) (365.25 * calDate1) +
                    (long) (30.6001 * (calDate2 + 1))
                    + B + 1720996.5 + calDate3;
        } else {
            today = (long) (365.25 * calDate1 - 0.75)
                    + (long) (30.6001 * (calDate2 + 1))
                    + B + 1720996.5 + calDate3;
        }

        today = today + gpsweek * 7 + day;

        return today;
    }

    private void gpsCalendar(double today) {

        double a, fract, b, c, d, e, f;

        a = (long) (today + 0.5);
        fract = today + 0.5 - a;

        if (a < 2299161) c = a + 1524;
        else {
            b = (long) ((a - 1867216.25) / 36524.25);
            c = a + b - (long) (b / 4) + 1525;
        }

        d = (long) ((c - 122.1) / 365.25);
        e = (long) (365.25 * d);
        f = (long) ((c - e) / 30.6001);

        this.day = (short) (c - e - (long) (30.6001 * f) + fract);
        this.month = (short) (f - 1 - 12 * (long) (f / 14));
        this.year = (int)(d - 4715 - (long) ((7 + this.month) / 10));
    }
}
