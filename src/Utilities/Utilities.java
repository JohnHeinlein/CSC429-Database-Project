// specify the package
package Utilities;

// system imports

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// project imports

/**
 * Useful Utilities
 */
//==============================================================
public class Utilities {
    //----------------------------------------------------------
    public static String convertToDefaultDateFormat(Date theDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        return formatter.format(theDate);

    }

    //----------------------------------------------------------
    public static String convertDateStringToDefaultDateFormat(String dateStr) {

        Date theDate = validateDateString(dateStr);

        if (theDate == null) {
            return null;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

            return formatter.format(theDate);
        }
    }

    //----------------------------------------------------------
    protected static Date validateDateString(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        Date theDate;

        try {
            theDate = formatter.parse(str);
            return theDate;
        } catch (ParseException ex) {
            SimpleDateFormat formatter2 =
                    new SimpleDateFormat("yyyy-MM-dd");

            try {
                theDate = formatter2.parse(str);
                return theDate;
            } catch (ParseException ex2) {
                SimpleDateFormat formatter3 =
                        new SimpleDateFormat("yyyy/MMdd");

                try {
                    theDate = formatter3.parse(str);
                    return theDate;
                } catch (ParseException ex3) {
                    SimpleDateFormat formatter4 =
                            new SimpleDateFormat("yyyyMM/dd");

                    try {
                        theDate = formatter4.parse(str);
                        return theDate;
                    } catch (ParseException ex4) {
                        return null;
                    }
                }
            }
        }
    }

    //----------------------------------------------------------
    protected String mapMonthToString(int month) {
        try {
            return Calendar.getInstance().getDisplayName(month, Calendar.LONG, Locale.US);
        } catch (Exception ex) {
            return null;
        }
    }

    //----------------------------------------------------------
    protected int mapMonthNameToIndex(String monthName) {
        switch (monthName) {
            case "January":
                return Calendar.JANUARY;
            case "February":
                return Calendar.FEBRUARY;
            case "March":
                return Calendar.MARCH;
            case "April":
                return Calendar.APRIL;
            case "May":
                return Calendar.MAY;
            case "June":
                return Calendar.JUNE;
            case "July":
                return Calendar.JULY;
            case "August":
                return Calendar.AUGUST;
            case "September":
                return Calendar.SEPTEMBER;
            case "October":
                return Calendar.OCTOBER;
            case "November":
                return Calendar.NOVEMBER;
            case "December":
                return Calendar.DECEMBER;
        }

        return -1;
    }


    //----------------------------------------------------
    protected boolean checkProperLetters(String value) {
        boolean[] valid = {true};

        value.chars().forEachOrdered(ch -> {
            if (((ch < 'A') || (ch > 'Z')) && ((ch < 'a') || (ch > 'z')) && (ch != '-') && (ch != ',') && (ch != '.') && (ch != ' ')) {
                valid[0] = false;
            }
        });

        return valid[0];
    }

    //----------------------------------------------------
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

    public static void logErr(String msg){
    	StackTraceElement[] ex = new Exception().getStackTrace();
    	System.err.printf("%s.%s: %s\n",
				ex[1].getClassName(),
				ex[1].getMethodName(),
				msg);
	}
}

