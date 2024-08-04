package com.chinmay.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateTimeUtils {
    private DateTimeUtils() {
    }

    public static String getDateTime() {
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH-mm-ss");
        Date resultdate = new Date(yourmilliseconds);
        return sdf.format(resultdate);
    }

    public static String getSystemDate() {
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMMMMMM dd, yyyy");
        Date resultdate = new Date(yourmilliseconds);
        return String.valueOf(sdf.format(resultdate));
    }

    public static String addDays() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("MMMMMMM dd, yyyy");

        c.setTime(new Date());
        c.add(Calendar.DATE, 10);
        String output = dateOnly.format(c.getTime());
        return output;
    }


    public static String getFirstDayOfWeek() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

        LocalDate monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        return monday.format(formatter).toString();
    }

    public static String getFirstDayOfLastWeek() {
        LocalDate today = LocalDate.now().minusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

        LocalDate monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        return monday.format(formatter).toString();
    }

    public static String getLastDayOfWeek() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        LocalDate sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }

        return sunday.format(formatter).toString();
    }

    public static String getLastDayOfLastWeek() {
        LocalDate today = LocalDate.now().minusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");

        LocalDate sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }

        return sunday.format(formatter).toString();
    }

    public static String getFirstDayOfMonth() {
        LocalDate today = LocalDate.now().withDayOfMonth(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        LocalDate monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        return monday.format(formatter).toString();
    }

    public static String getFirstDayOfYear() {
        LocalDate date = LocalDate.of(2021, Month.JANUARY, 01);
        LocalDate firstDayOfYear = date.with(TemporalAdjusters.firstDayOfYear());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return firstDayOfYear.format(formatter).toString();
    }

    public static Date convertStringToDate(String dates) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/d/yyyy", Locale.ENGLISH);
        Date date = sdf.parse(dates);

        return date;
    }

    public static String getDatesInCalendarFormat(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = null;
        try {
            Date d = inputFormat.parse(date);
            formattedDate = outputFormat.format(d);
        } catch (Exception e) {
            System.out.println("Error parsing date: " + e.getMessage());
        }
        return formattedDate;
    }

    public static String getDay(String dateStr) {
        String[] parts = dateStr.split(" ");
        return parts[0];
    }

    public static String getMonthYear(String dateStr) {
        String[] parts = dateStr.split(" ");
        int monthIndex = 1;
        int yearIndex = 2;

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr);
        }

        String monthName = parts[monthIndex];
        String year = parts[yearIndex];

        return monthName + " " + year;
    }

    public static String getMonth(int month) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return months[month - 1];
    }
}
