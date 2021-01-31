import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.text.SimpleDateFormat;

public final class HTMLPage {
    private static String url;
    private String filepath;

    public HTMLPage(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filepath;
    }

    public void downloadHTML() {
        File file = createHTMLFile();
        filepath = file.getAbsolutePath();

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             BufferedInputStream bufferedInputStream = returnBodyAsStream(siteConnection(url))) {
            while (bufferedInputStream.available() > 0) {
                byte fragment = (byte) bufferedInputStream.read();
                fileOutputStream.write(fragment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createHTMLFile() {
        String filename = String.format("%s%s%s", getHostname(), getCurrentDate(), ".html");
        return new File(new File("pages"), filename);
    }

    private BufferedInputStream returnBodyAsStream(Connection connection) {
        BufferedInputStream stream = null;
        try {
            stream = connection.execute().bodyStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

    private Connection siteConnection(String url) {
        return Jsoup.connect(url)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.google.com")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .followRedirects(true)
                .maxBodySize(0)
                .timeout(5000);
    }

    private static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_dd.MM_HHmm");
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    private static String getHostname() {
        String[] domainParts = new String[0];
        try {
            domainParts = url.split("//")[1].split("/")[0].split("\\.");
            if (domainParts.length == 3) return domainParts[1];
            if (domainParts.length > 3) return domainParts[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return domainParts[0];
    }
}
