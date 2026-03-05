import java.time.LocalDateTime;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
        this.uniquePath = new HashSet<>();
        this.occurrenceOS=new HashMap<>();
        this.osShare = new HashMap<>();
    }

    private final HashSet<String> uniquePath;
    private final HashMap<String, Integer> occurrenceOS;
    private final HashMap<String, Double> osShare;


    public void addEntry(LogEntry logEntry) {
        this.totalTraffic += logEntry.getSize();
        updateTimeBounds(logEntry.getTime());
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
            return; // Если нет вхождений, ничего не делаем
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