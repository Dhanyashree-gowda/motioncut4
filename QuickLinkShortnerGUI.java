import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class QuickLinkShortenerGUI extends JFrame implements ActionListener {
    private static final String FILE_NAME = "urls.txt";
    private static final String BASE_URL = "http://short.ly/";

    private Map<String, String> urlMap = new HashMap<>();
    private JTextField longUrlField;
    private JTextField shortUrlField;
    private JTextArea outputArea;

    public QuickLinkShortenerGUI() {
        loadUrls();
        setupUI();
    }

    private void setupUI() {
        setTitle("QuickLink Shortener");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        longUrlField = new JTextField();
        shortUrlField = new JTextField();
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JButton shortenButton = new JButton("Shorten URL");
        shortenButton.addActionListener(this);

        JButton retrieveButton = new JButton("Retrieve Original URL");
        retrieveButton.addActionListener(this);

        add(new JLabel("Long URL:"));
        add(longUrlField);
        add(shortenButton);
        add(new JLabel("Shortened URL:"));
        add(shortUrlField);
        add(retrieveButton);
        add(new JScrollPane(outputArea));

        setVisible(true);
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
            outputArea.append("Error loading URLs: " + e.getMessage() + "\n");
        }
    }

    private void saveUrls() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, String> entry : urlMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            outputArea.append("Error saving URLs: " + e.getMessage() + "\n");
        }
    }

    private String shortenUrl(String longUrl) {
        String shortUrl = BASE_URL + Integer.toHexString(longUrl.hashCode());
        urlMap.put(shortUrl, longUrl);
        saveUrls();
        return shortUrl;
    }

    private String getOriginalUrl(String shortUrl) {
        return urlMap.get(shortUrl);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Shorten URL")) {
            String longUrl = longUrlField.getText();
            String shortUrl = shortenUrl(longUrl);
            shortUrlField.setText(shortUrl);
            outputArea.append("Shortened URL: " + shortUrl + "\n");
        } else if (e.getActionCommand().equals("Retrieve Original URL")) {
            String shortUrl = shortUrlField.getText();
            String originalUrl = getOriginalUrl(shortUrl);
            if (originalUrl != null) {
                outputArea.append("Original URL: " + originalUrl + "\n");
            } else {
                outputArea.append("URL not found!\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuickLinkShortenerGUI::new);
    }
}
