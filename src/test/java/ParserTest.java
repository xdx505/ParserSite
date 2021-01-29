import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParserTest {
    final String pagesFile = "C:\\Users\\Dan\\IdeaProjects\\ParserSite\\build\\libs\\pages\\meduza_28.01_2213.html";

    @Test
    public void parseTest() throws IOException {
        File file = new File(pagesFile);
        Document document = Jsoup.parse(file, "UTF-8");
        printResult(wordCount(wordsExtractor(document)));
    }

    private String[] wordsExtractor(Document doc) {
        doc.getElementsByTag("pre").remove();
        doc.getElementsByTag("img").remove();
        String text = doc.body().text().toUpperCase().replaceAll("[^A-ZА-Я]", " ");
        return text.split(" ");
    }

    private HashMap<String, Integer> wordCount(String[] words) {
        ArrayList<String> wordsList = new ArrayList(Arrays.asList(words));
        HashMap<String, Integer> map = new HashMap();

        for (int i = 0; i < wordsList.size(); i++) {
            String firstWord = wordsList.get(i);
            int count = 1;
            if (!firstWord.equals("")) {
                for (int j = 1; j < wordsList.size(); j++) {
                    String secondWord = wordsList.get(j);
                    if (firstWord.equals(secondWord)) {
                        count++;
                        wordsList.remove(j);
                        j--;
                    }
                }
            }
            map.put(firstWord, count);
            wordsList.remove(i);
            i--;
            count = 1;
        }

        return map;
    }

    private void printResult(HashMap<String, Integer> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue())
                .forEach(System.out::println);
    }
}