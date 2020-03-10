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

        Date theDate = null;

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
        if (monthName.equals("January")) {
            return Calendar.JANUARY;
        } else if (monthName.equals("February")) {
            return Calendar.FEBRUARY;
        } else if (monthName.equals("March")) {
            return Calendar.MARCH;
        } else if (monthName.equals("April")) {
            return Calendar.APRIL;
        } else if (monthName.equals("May")) {
            return Calendar.MAY;
        } else if (monthName.equals("June")) {
            return Calendar.JUNE;
        } else if (monthName.equals("July")) {
            return Calendar.JULY;
        } else if (monthName.equals("August")) {
            return Calendar.AUGUST;
        } else if (monthName.equals("September")) {
            return Calendar.SEPTEMBER;
        } else if (monthName.equals("October")) {
            return Calendar.OCTOBER;
        } else if (monthName.equals("November")) {
            return Calendar.NOVEMBER;
        } else if (monthName.equals("December")) {
            return Calendar.DECEMBER;
        }

        return -1;
    }


    //----------------------------------------------------
    protected boolean checkProperLetters(String value) {
        for (int cnt = 0; cnt < value.length(); cnt++) {
            char ch = value.charAt(cnt);

            if ((ch >= 'A') && (ch <= 'Z') || (ch >= 'a') && (ch <= 'z')) {
            } else if ((ch == '-') || (ch == ',') || (ch == '.') || (ch == ' ')) {
            } else {
                return false;
            }
        }
        return true;
    }

    //----------------------------------------------------
    protected boolean checkProperPhoneNumber(String value) {
        if ((value == null) || (value.length() < 7)) {
            return false;
        }

        for (int cnt = 0; cnt < value.length(); cnt++) {
            char ch = value.charAt(cnt);

            if ((ch >= '0') && (ch <= '9')) {
            } else if ((ch == '-') || (ch == '(') || (ch == ')') || (ch == ' ')) {
            } else {
                return false;
            }
        }

        return true;
    }

    public static void logErr(String msg){
    	StackTraceElement[] ex = new Exception().getStackTrace();
    	System.err.printf("%s.%s: %s\n",
				ex[1].getClassName(),
				ex[1].getMethodName(),
				msg);
	}
}

