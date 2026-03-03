import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgent {
    private final String os;
    private final String browser;

    public UserAgent(String userAgentString) {
        this.os = extractOS(userAgentString);
        this.browser = extractBrowser(userAgentString);
    }

    private static String extractOS(String userAgent) {
        String uaLower = userAgent.toLowerCase();

        if (uaLower.contains("windows")) {
            return "Windows";
        } else if (uaLower.contains("macintosh") || uaLower.contains("mac os")) {
            return "Mac OS";
        } else if (uaLower.contains("linux")) {
            return "Linux";
        } else if (uaLower.contains("android")) {
            return "Android";
        } else if (uaLower.contains("iphone") || uaLower.contains("ipad")) {
            return "iOS";
        } else if (uaLower.contains("yandexbot")) {
            return "YandexBot";
        } else {
            int startIdx = userAgent.indexOf('(');
            int endIdx = userAgent.indexOf(')', startIdx);
            if (startIdx != -1 && endIdx != -1) {
                return userAgent.substring(startIdx + 1, endIdx).trim();
            }
            return "Неизвестно";
        }
    }

    private static String extractBrowser(String userAgent) {
        String uaLower = userAgent.toLowerCase();

        if (uaLower.contains("yandexbot")) {
            return "YandexBot";
        } else if (uaLower.contains("googlebot")) {
            return "Googlebot";
        } else if (uaLower.contains("chrome") && !uaLower.contains("edg")) {
            return "Chrome";
        } else if (uaLower.contains("firefox")) {
            return "Firefox";
        } else if (uaLower.contains("safari") && !uaLower.contains("chrome") && !uaLower.contains("edg") && !uaLower.contains("opr")) {
            return "Safari";
        } else if (uaLower.contains("edg")) {
            return "Edge";
        } else if (uaLower.contains("msie") || uaLower.contains("trident")) {
            return "Internet Explorer";
        } else if (uaLower.contains("opera") || uaLower.contains("opr")) {
            return "Opera";
        } else {
            return "Неизвестно";
        }
    }

    public String getOs() {
        return os != null ? os : "";
    }

    public String getBrowser() {
        return browser != null ? browser : "";
    }

    @Override
    public String toString() {
        return "UserAgent {\n" +
                "  OS: " + getOs() + "\n" +
                "  Browser: " + getBrowser() + "\n" +
                "}";
    }
}

