import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());

        Scanner scanner = new Scanner(System.in);
        int countValidPatch = 0;
        while (true) {
            System.out.println("Введите путь к файлу");
            String path = scanner.nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (isDirectory == true && fileExists == true) {
                System.out.println("Указана директория, а не файл. Попробуйте снова.");
            } else if (fileExists == false && isDirectory == false) {
                System.out.println("Файл не существует. Попробуйте снова.");
                continue;
            }
            if (fileExists == true && isDirectory == false) {
                countValidPatch++;
                System.out.println("Путь указан верно, это файл " + countValidPatch);

            }
        }
    }
}

