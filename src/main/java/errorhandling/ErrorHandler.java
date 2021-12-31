package errorhandling;


public class ErrorHandler {
    private static boolean hasError = false;

    public static void printError(String msg) {
        hasError = true;
        System.out.println(msg);
    }

    public static boolean isHasError() {
        return hasError;
    }

    public static void setHasError(boolean hasError) {
        ErrorHandler.hasError = hasError;
    }
}
