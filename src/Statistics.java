import java.time.LocalDateTime;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private long humanVisitCount;
    private long errorRequestCount;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
        this.uniquePath = new HashSet<>();
        this.occurrenceOS = new HashMap<>();
        this.osShare = new HashMap<>();
        this.notFound = new HashSet<>();
        this.occurrenceBrowser = new HashMap<String, Integer>();
        this.browserShare = new HashMap<String, Double>();
        this.humanVisitCount = 0;
        this.errorRequestCount = 0;
        this.uniqueRealUserIPs=new HashSet<>();

    }

    private final HashSet<String> uniquePath;
    private final HashMap<String, Integer> occurrenceOS;
    private final HashMap<String, Double> osShare;
    private final HashSet<String> notFound;
    private final HashMap<String, Integer> occurrenceBrowser;
    private final HashMap<String, Double> browserShare;
    private final HashSet<String> uniqueRealUserIPs;


    public void addEntry(LogEntry logEntry) {
        this.totalTraffic += logEntry.getSize();
        updateTimeBounds(logEntry.getTime());
        String userAgent = logEntry.getUserAgent().toString();
        if (userAgent != null && !UserAgent.isBot(userAgent)) {
            humanVisitCount++;
            uniqueRealUserIPs.add(logEntry.getIpAddr());
        }
        if (logEntry.getStatus() >= 400 && logEntry.getStatus() < 600) {
            errorRequestCount++;
        }
    }

    private void updateTimeBounds(LocalDateTime time) {
        if (minTime == null || time.isBefore(minTime)) {
            minTime = time;
        }

        if (maxTime == null || time.isAfter(maxTime)) {
            maxTime = time;
        }
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0;
        }

        Duration duration = Duration.between(minTime, maxTime);
        double hours = duration.toMinutes() / 60.0;

        if (hours <= 0) {
            return totalTraffic;
        }

        return totalTraffic / hours;
    }

    public void addUniquePath(LogEntry logEntry) {
        if (logEntry.getStatus()==200){
            uniquePath.add(logEntry.getPath());
        }
    }

    public void addNotFoundPath(LogEntry logEntry) {
        if (logEntry.getStatus()==404){
            notFound.add(logEntry.getPath());
        }
    }

    public void addOccurrenceOS(UserAgent user) {
        String os = user.getOs();
        if (os != null) {
            if (!occurrenceOS.containsKey(os)) {
                occurrenceOS.put(os, 1);
            } else {
                occurrenceOS.put(os, occurrenceOS.get(os) + 1);
            }

            recalculateOSShare();
        }
    }

    private void recalculateOSShare() {
        osShare.clear();
        int totalOccurrences = occurrenceOS.values().stream().mapToInt(Integer::intValue).sum();

        if (totalOccurrences == 0) {
            return;
        }

        for (String os : occurrenceOS.keySet()) {
            int count = occurrenceOS.get(os);
            double share = (double) count / totalOccurrences;
            osShare.put(os, share);
        }
    }

    public HashMap<String, Double> getOSShare() {
        return osShare;
    }

    public void addOccurrenceBrowser(UserAgent user) {
        String browser = user.getBrowser();
        if (browser != null) {
            if (!occurrenceBrowser.containsKey(browser)) {
                occurrenceBrowser.put(browser, 1);
            } else {
                occurrenceBrowser.put(browser, occurrenceBrowser.get(browser) + 1);
            }

            recalculateBrowserShare();
        }
    }

    private void recalculateBrowserShare() {
        browserShare.clear();
        int totalOccurrences = occurrenceBrowser.values().stream().mapToInt(Integer::intValue).sum();

        if (totalOccurrences == 0) {
            return;
        }

        for (String browser : occurrenceBrowser.keySet()) {
            int count = occurrenceBrowser.get(browser);
            double share = (double) count / totalOccurrences;
            browserShare.put(browser, share);
        }
    }

    public double calculateAverageVisitsPerHour() {
        if (minTime == null || maxTime == null) {
            return 0;
        }

        Duration duration = Duration.between(minTime, maxTime);
        double hours = duration.toMinutes() / 60.0;

        if (hours <= 0) {
            return 0;
        }

        return humanVisitCount / hours;
    }

    public double calculateAverageErrorRequestsPerHour() {
        if (minTime == null || maxTime == null) {
            return 0;
        }

        Duration duration = Duration.between(minTime, maxTime);
        double hours = duration.toMinutes() / 60.0;

        if (hours <= 0) {
            return 0;
        }

        return errorRequestCount / hours;
    }

    public double calculateAverageVisitsPerUser() {
        if (uniqueRealUserIPs.isEmpty()) {
            return 0;
        }

        return (double) humanVisitCount / uniqueRealUserIPs.size();
    }

    public HashMap<String, Double> getBrowserShare() {
        return browserShare;
    }

    public long getTotalTraffic() {
        return totalTraffic;
    }

    public LocalDateTime getMinTime() {
        return minTime;
    }

    public LocalDateTime getMaxTime() {
        return maxTime;
    }
}