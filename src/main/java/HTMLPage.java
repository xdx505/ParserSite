import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HTMLPage {
    private final String url;
    private String filepath;

    public HTMLPage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getFilePath() {
        return filepath;
    }

    public void downloadHTML() {
        Document doc = connectSite(url);
        File file = createFile();
        filepath = file.getAbsolutePath();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath));
             BufferedReader bufferedReader = new BufferedReader(new StringReader(doc.html()))) {
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) break;
                bufferedWriter.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document connectSite(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("http://www.google.com")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    private String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_dd.MM_HHmm");
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    private String getHostname() {
        String[] domainParts = url.split("//")[1].split("/")[0].split("\\.");
        if (domainParts.length == 3) return domainParts[1];
        if (domainParts.length > 3) return domainParts[2];
        return domainParts[0];
    }

    private File createFile() {
        String filename = String.format("%s%s%s", getHostname(), getCurrentDate(), ".html");
        return new File(new File("data"), filename);
    }
}
