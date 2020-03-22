package utilities;

/**
 * Class containing static methods to aid in debug logs
 * call setDebug(true) in the main method to enable logErr calls
 */
public class Debug {

    public static boolean debug;

    public static Boolean setDebug(Boolean val){
        debug = val;
        return debug;
    }

    public static void logMsg(String messasge) {
        System.out.println(messasge);
    }

    /**
     * Prints the class and method name this function is called from, plus a message
     * @param msg Message to append to stack trace
     */
    public static void logErr(String msg){
        if(!debug) return; //Return if we're not debugging

        StackTraceElement[] ex = new Exception().getStackTrace();
        System.err.printf("%s.%s: %s\n",
                ex[1].getClassName(),   // 0 is this method and class
                ex[1].getMethodName(),
                msg);
    }
}
