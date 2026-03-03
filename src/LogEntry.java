import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private final String ipAddr;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int status;
    private final int responseSize;
    private final String referrer;
    private final UserAgent userAgent;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    public LogEntry(String logLine) throws LogParseException {
        String[] parts = parseLogLine(logLine);
        this.ipAddr = parts[0];
        this.time = LocalDateTime.parse(parts[3], formatter);
        String requestLine = parts[4];
        String[] methodAndPath = getHttpMethodAndPath(requestLine);
        this.method = HttpMethod.fromString(methodAndPath[0]);
        this.path = methodAndPath[1];
        try {
            this.status = Integer.parseInt(parts[5]);
            this.responseSize = Integer.parseInt(parts[6]);
        } catch (NumberFormatException e) {
            throw new LogParseException(e);
        }
        this.referrer = parts[7];
        this.userAgent = new UserAgent(parts[8]);
    }

    private String[] parseLogLine(String logLine) throws LogParseException {
        String regex = "^(\\S+) (\\S+) (\\S+) \\[(.+?)\\] \"(.+?)\" (\\d+) (\\d+) \"(.*?)\" \"(.*?)\"$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(logLine);
        if (matcher.find()) {
            return new String[]{
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    matcher.group(4),
                    matcher.group(5),
                    matcher.group(6),
                    matcher.group(7),
                    matcher.group(8),
                    matcher.group(9)
            };
        } else {
            throw new LogParseException("Невозможно распарсить лог: " + logLine);
        }
    }

    public int getStatus() {
        return status;
    }

    public long getSize() {
        return responseSize;
    }

    public String getReferrer() {
        return referrer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String[] getHttpMethodAndPath(String requestLine) {
        String[] requestParts = requestLine.split("\\s+");
        if (requestParts.length >= 2) {
            return new String[]{requestParts[0], requestParts[1]};
        } else {
            return new String[]{"", ""};
        }
    }

    @Override
    public String toString() {
        return "LogEntry {\n" +
                "  IP Address: " + ipAddr + "\n" +
                "  Time: " + time + "\n" +
                "  Method: " + method + "\n" +
                "  Path: " + path + "\n" +
                "  Status: " + status + "\n" +
                "  Size: " + responseSize + "\n" +
                "  Referrer: " + referrer + "\n" +
                "  User Agent: " + userAgent + "\n" +
                "}";
    }
}



