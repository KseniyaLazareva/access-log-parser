public class LogParseException extends Exception {
    public LogParseException(String message) {
        super(message);
    }

    public LogParseException(NumberFormatException e) {
        super(e);
    }
}