import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());
        long lineCount;
        int maxLength = 0;
        int minLength = maxLength;
        int countYandexBot;
        int countGooglebot;
        double ratioGooglebot;
        double ratioYandexBot;
        Scanner scanner = new Scanner(System.in);
        int countValidPatch = 0;
        while (true) {
            lineCount = 0;
            countYandexBot = 0;
            countGooglebot = 0;
            System.out.println("Введите путь к файлу");
            String path = scanner.nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (isDirectory) {
                System.out.println("Указана директория, а не файл. Попробуйте снова.");
                continue;
            }
            if (fileExists) {
                countValidPatch++;
                FileReader fileReader = null;
                try {
                    fileReader = new FileReader(path);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try (BufferedReader reader = new BufferedReader(fileReader)) {
                    String line;
                    while ((line = reader.readLine()) != null && !line.isEmpty()) {
                        lineCount++;
                        if (line.length() > maxLength) {
                            maxLength = line.length();
                        }
                        if (line.length() < maxLength) {
                            minLength = line.length();
                        }
                        if (line.length() > 1024) {
                            throw new LongLineException("Строка в файле длиннее 1024 символов.");
                        }
                        String regex = "^(\\S+) (\\S+) (\\S+) \\[(.+?)\\] \"(.+?)\" (\\d+) (\\d+) \"(.*?)\" \"(.*?)\"$";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            String ip = matcher.group(1);
                            String property1 = matcher.group(2);
                            String property2 = matcher.group(3);
                            String date = matcher.group(4);
                            String request = matcher.group(5);
                            String status = matcher.group(6);
                            String size = matcher.group(7);
                            String referrer = matcher.group(8);
                            String userAgent = matcher.group(9);
                            int startIdx = userAgent.indexOf('(');
                            int endIdx = userAgent.indexOf(')', startIdx);
                            if (startIdx != -1 && endIdx != -1) {
                                String insideParentheses = userAgent.substring(startIdx + 1, endIdx);
                                String[] parts = insideParentheses.split(";");
                                if (parts.length >= 2) {
                                    String fragment = parts[1].trim();
                                    int slashIdx = fragment.indexOf('/');
                                    String partBeforeSlash;
                                    if (slashIdx != -1) {
                                        partBeforeSlash = fragment.substring(0, slashIdx).trim();
                                        if (partBeforeSlash.equals("Googlebot")) {
                                            countGooglebot++;
                                        }
                                        if (partBeforeSlash.equals("YandexBot")) {
                                            countYandexBot++;
                                        }
                                    }
                                }
                            }
                        } else {
                            System.out.println("Не удалось распарсить строку");
                        }
                    }
                    System.out.println("Общее количество строк в файле " + lineCount);
                    if (lineCount > 0) {
                        ratioYandexBot = (double) countYandexBot / lineCount;
                        System.out.printf("Доля запросов с YandexBot: %.2f%%\n", ratioYandexBot * 100);
                    }
                    if (lineCount > 0) {
                        ratioGooglebot = (double) countGooglebot / lineCount;
                        System.out.printf("Доля запросов с Googlebot: %.2f%%\n", ratioGooglebot * 100);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Путь указан верно, это файл " + countValidPatch);
                continue;
            }
            System.out.println("Файл не существует. Попробуйте снова.");
        }
    }
}
