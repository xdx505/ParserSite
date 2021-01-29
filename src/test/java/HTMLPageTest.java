import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class HTMLPageTest {

    @Test
    public void mainTest() {
        try (FileOutputStream fileOutputStream = new FileOutputStream("c:\\file1.txt");
             BufferedInputStream bufferedInputStream = getStream()) {
            while (bufferedInputStream.available() > 0) {
                byte fragment = (byte) bufferedInputStream.read();
                fileOutputStream.write(fragment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private BufferedInputStream getStream() {
        return null;
    }
}