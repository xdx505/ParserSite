import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String url = br.readLine();
            HTMLPage page = new HTMLPage(url);
            page.downloadHTML();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
