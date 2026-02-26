import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());
        long lineCount = 1;
        int maxLength = 0;
        int minLength = 0;

        Scanner scanner = new Scanner(System.in);
        int countValidPatch = 0;
        while (true) {
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
                    }
                    System.out.println("Общее количество строк в файле " + lineCount);
                    System.out.println("Длина самой длинной строки в файле " + maxLength);
                    System.out.println("Длина самой короткой строки в файле " + minLength);

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



