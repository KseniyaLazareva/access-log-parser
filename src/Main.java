import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        int countValidPatch = 0;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String logFilePath = askFileName(scanner);
            countValidPatch++;
            System.out.println("Путь указан верно, это файл " + countValidPatch);
            try {
                List<String> linesToParse = readLines(logFilePath);
                long lineCount = linesToParse.size();
                System.out.println("Общее количество строк в файле " + lineCount);
                long countYandexBot = 0;
                long countGooglebot = 0;
                List<LogEntry> parsedEntries = new ArrayList<>();
                Statistics statistics = new Statistics();
                for (String lineToParse : linesToParse) {
                    try {
                        LogEntry entry = new LogEntry(lineToParse);
                        parsedEntries.add(entry);
                        statistics.addEntry(entry);
                        if (entry.getUserAgent().getBrowser().contains("YandexBot")) countYandexBot++;
                        if (entry.getUserAgent().getBrowser().contains("Googlebot")) countGooglebot++;
                    } catch (LogParseException e) {
                        System.out.println("Ошибка парсинга строки \"" + lineToParse);
                        e.printStackTrace();
                    }
                }
                if (lineCount > 0) {
                    double ratioYandexBot = (double) countYandexBot / lineCount;
                    System.out.printf("Доля запросов с YandexBot: %.2f%%\n", ratioYandexBot * 100);
                    double ratioGooglebot = (double) countGooglebot / lineCount;
                    System.out.printf("Доля запросов с Googlebot: %.2f%%\n", ratioGooglebot * 100);
                    System.out.printf("Tраффик за час: %.2f%n", statistics.getTrafficRate());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static String askFileName(Scanner scanner) {

        while (true) {
            System.out.println("Введите путь к файлу");
            String path = scanner.nextLine();
            File file = new File(path);
            if (file.isDirectory()) {
                System.out.println("Указана директория, а не файл. Попробуйте снова.");
                continue;
            }
            if (!file.exists()) {
                System.out.println("Файл не существует. Попробуйте снова.");
                continue;
            }
            return path;
        }
    }

    static List<String> readLines(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        FileReader fileReader = new FileReader(path);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.length() > 1024) {
                throw new LongLineException("Строка в файле длиннее 1024 символов.");
            }
            lines.add(line);
        }
        return lines;
    }
}
