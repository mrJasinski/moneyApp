package com.moneyApp;

import java.time.LocalDate;

public class Utils
{
    public static final int ACTUAL_MONTH = LocalDate.now().getMonthValue();
    public static final int ACTUAL_MONTH_LENGTH = LocalDate.now().lengthOfMonth();
    public static final int ACTUAL_YEAR = LocalDate.now().getYear();
    public static final LocalDate ACTUAL_MONTH_START_DAY = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
    public static final LocalDate ACTUAL_MONTH_END_DAY = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().lengthOfMonth());
    public static final String APP_MAIL = "moneyAppMain@gmail.com";
}
