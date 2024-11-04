import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class QuickLinkShortener {
    private static final String FILE_NAME = "urls.txt";
    private static final String BASE_URL = "http://short.ly/";

    private Map<String, String> urlMap = new HashMap<>();

    public QuickLinkShortener() {
        loadUrls();
    }

    private void loadUrls() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    urlMap.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading URLs: " + e.getMessage());
        }
    }

    private void saveUrls() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : urlMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving URLs: " + e.getMessage());
        }
    }

    public String shortenUrl(String longUrl) {
        String shortUrl = BASE_URL + Integer.toHexString(longUrl.hashCode());
        urlMap.put(shortUrl, longUrl);
        saveUrls();
        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        return urlMap.get(shortUrl);
    }

    public static void main(String[] args) {
        QuickLinkShortener shortener = new QuickLinkShortener();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Shorten URL");
            System.out.println("2. Retrieve Original URL");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter the long URL: ");
                String longUrl = scanner.nextLine();
                String shortUrl = shortener.shortenUrl(longUrl);
                System.out.println("Shortened URL: " + shortUrl);
            } else if (choice == 2) {
                System.out.print("Enter the shortened URL: ");
                String shortUrl = scanner.nextLine();
                String originalUrl = shortener.getOriginalUrl(shortUrl);
                if (originalUrl != null) {
                    System.out.println("Original URL: " + originalUrl);
                } else {
                    System.out.println("URL not found!");
                }
            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice! Please try again.");
            }
        }
        scanner.close();
    }
}
