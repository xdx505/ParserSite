import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.text.SimpleDateFormat;

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
        File file = createFile();
        filepath = file.getAbsolutePath();

        Connection connection = Jsoup.connect(url)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.google.com");

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedInputStream bufferedInputStream = returnBodyAsStream(connection)) {
            while (true) {
                assert bufferedInputStream != null;
                if (!(bufferedInputStream.available() > 0)) break;
                byte fragment = (byte) bufferedInputStream.read();
                fileOutputStream.write(fragment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
