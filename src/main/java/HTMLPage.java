import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HTMLPage {
    private final String url;
    private Document doc;
    private String filePath;

    public HTMLPage(String url) {
        this.url = url;
        this.doc = connectSite(url);
    }

    public String getUrl() {
        return url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void downloadHTML() {
        File file = createFile();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
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
        String hostname = "";
        String regex = "^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$";

        Pattern pattern = Pattern.compile(regex, 2);
        Matcher matcher = pattern.matcher(url);

        while (matcher.find()) {
            hostname = matcher.group(3);
        }
        String[] hostnameParts = hostname.split("\\.");

        if (hostnameParts.length > 3) return hostnameParts[2];
        if (hostnameParts.length == 3) return hostnameParts[1];
        return hostnameParts[0];
    }

    private File createFile() {
        String fileSeparator = System.getProperty("file.separator");
        String path = "data" + fileSeparator;
        String filename = String.format("%s%s%s", getHostname(), getCurrentDate(), ".html");

        filePath = path + filename;

        File file = new File(path, filename);
        return file;
    }
}
