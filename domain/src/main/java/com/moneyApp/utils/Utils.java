package com.moneyApp.utils;

import java.time.LocalDate;

public class Utils
{
    public static final String APP_MAIL = "moneyAppMain@gmail.com";

    public static double roundToTwoDecimals(double toRound)
    {
        return Math.round(toRound * 100d) / 100d;
    }

    public static LocalDate getMonthYearStartDate(LocalDate monthYear)
    {
        return LocalDate.of(monthYear.getYear(), monthYear.getMonthValue(), 1);
    }

    public static LocalDate getMonthYearEndDate(LocalDate monthYear)
    {
        return LocalDate.of(monthYear.getYear(), monthYear.getMonthValue(), monthYear.lengthOfMonth());
    }

    public static String createBillNumber(LocalDate date, int count)
    {
        return String.format("%s%s_%s", date.getYear(), date.getMonthValue(), count);
    }
}
