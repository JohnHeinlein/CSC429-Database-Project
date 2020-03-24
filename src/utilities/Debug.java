package utilities;

/**
 * Class containing static methods to aid in debug logs
 * call setDebug(true) in the main method to enable logErr calls
 */
public class Debug {

    public static boolean debug;

    public static Boolean setDebug(Boolean val) {
        debug = val;
        return debug;
    }

    /**
     * Prints the class and method name this function is called from, plus a message, as red text
     *
     * @param msg Message to append to stack trace
     */
    public static void logErr(String msg) { log(msg,true); }

    /**
     * Prints the class and method name this function is called from, plus a message
     *
     * @param msg Message to append to stack trace
     */
    public static void logMsg(String msg) { log(msg,false); }

    private static void log(String msg, Boolean isError){
        if(!debug) return;

        StackTraceElement[] ex = new Exception().getStackTrace();
        if(isError) {
            System.err.printf("[%s.%s] %s\n", //Prints as red error text to console
                    ex[2].getClassName(),   // 0 is this method and class
                    ex[2].getMethodName(),  // 1 is the above methods
                    msg);
        }else{
            System.out.printf("[%s.%s] %s\n", //Prints as normal text
                    ex[2].getClassName(),
                    ex[2].getMethodName(),
                    msg);
        }
    }
}