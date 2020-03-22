package utilities;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Useful Utilities
 */
public class Utilities {

    public static String convertToDefaultDateFormat(Date theDate) {
        return new SimpleDateFormat("yyyy/MM/dd").format(theDate);
    }

    public static String convertDateStringToDefaultDateFormat(String dateStr) {
        Date theDate = validateDateString(dateStr);
        return theDate == null ? null : new SimpleDateFormat("yyyy/MM/dd").format(theDate);
    }

    protected static Date validateDateString(String str) {
        String[] formatterStrings = {"yyyy/MM/dd", "yyyy-MM-dd", "yyyy/MMdd", "yyyyMM/dd"};

        for (String formatterString : formatterStrings) {
            SimpleDateFormat formatter = new SimpleDateFormat(formatterString);

            try {
                return formatter.parse(str);
            } catch (ParseException ignored) {

            }
        }

        return null;
    }

    protected String mapMonthToString(int month) {
        try {
            return Calendar.getInstance().getDisplayName(month, Calendar.LONG, Locale.US);
        } catch (Exception ex) {
            return null;
        }
    }

    protected int mapMonthNameToIndex(String monthName) throws ParseException {
        // TOOO: Test this.
        return Arrays.asList(DateFormatSymbols.getInstance(Locale.US).getMonths()).indexOf(monthName);
//        switch (monthName) {
//            case "January":
//                return Calendar.JANUARY;
//            case "February":
//                return Calendar.FEBRUARY;
//            case "March":
//                return Calendar.MARCH;
//            case "April":
//                return Calendar.APRIL;
//            case "May":
//                return Calendar.MAY;
//            case "June":
//                return Calendar.JUNE;
//            case "July":
//                return Calendar.JULY;
//            case "August":
//                return Calendar.AUGUST;
//            case "September":
//                return Calendar.SEPTEMBER;
//            case "October":
//                return Calendar.OCTOBER;
//            case "November":
//                return Calendar.NOVEMBER;
//            case "December":
//                return Calendar.DECEMBER;
//        }
//
//        return -1;
    }

    protected boolean checkProperLetters(String value) {
        boolean[] valid = {true};

        value.chars().forEachOrdered(ch -> {
            if (((ch < 'A') || (ch > 'Z')) && ((ch < 'a') || (ch > 'z')) && (ch != '-') && (ch != ',') && (ch != '.') && (ch != ' ')) {
                valid[0] = false;
            }
        });

        return valid[0];
    }

    protected boolean checkProperPhoneNumber(String value) {
        if ((value == null) || (value.length() < 7)) {
            return false;
        }

        boolean[] valid = {true};

        value.chars().forEachOrdered(ch -> {
            if (((ch < '0') || (ch > '9')) && ((ch != '-') && (ch != '(') && (ch != ')') && (ch != ' '))) {
                valid[0] = false;
            }
        });

        return valid[0];
    }
}
