package com.sh.barcodemanagement.utils;

import android.annotation.SuppressLint;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class StringFormatUtils {

    public static String convertToStringMoneyVND(long money) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(money) + " đ";
    }

    public static String convertToStringMoney(long money) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(money);
    }

    public static String getCurrentDateStr() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date convertStringToDate(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateStrByCalendar(Calendar calendar) {
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        return date;
    }

    public static String getCurrentDateFormatStr(String formatStr) {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat(formatStr).format(calendar.getTime());
        return date;
    }

    public static Date convertToDateTime(String dateStr) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        return dateFormat.parse(dateStr);
    }

    public static String convertDateTimeToStr(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public static String getTextCustomerNameForTextView(String input) {
        String result;
        input = input.trim();
        String[] arr = input.split(" ");
        if (arr.length > 1) {
            result = (arr[0].trim().charAt(0) + "") + (arr[arr.length - 1].trim().charAt(0) + "");
        } else {
            result = (input.charAt(0) + "") + (input.charAt(input.length() - 1) + "");
        }
        return result.toUpperCase();
    }

    public static String convertUTF8ToString(String value) {
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").replace("Đ", "D")
                    .replace("đ", "d").toLowerCase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    public static String convertObjectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

}
