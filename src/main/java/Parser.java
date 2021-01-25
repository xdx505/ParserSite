import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

public class Parser {
    private final String filepath;
    private final String REGEX = "[^a-zA-zР-пр-џ0-9][ *][\\s,.!?\";:\\]\\[)(\\n\\r\\t]";

    public Parser(String filepath) {
        this.filepath = filepath;
    }

    public void parse() {
        Document document = setDocFromFilepath(filepath);
        Elements webContent = document.select("body");
        webContent.select("pre").remove();

        String text = webContent.text();
        String onlyWords = text.toUpperCase().replaceAll(REGEX, " ");
        String[] words = onlyWords.split(" ");

        HashMap<String, Integer> wordsMap = wordCount(words);
        printResult(wordsMap);
    }

    private Document setDocFromFilepath(String filepath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))) {
            while (bufferedReader.ready()) {
                content.append(bufferedReader.readLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Jsoup.parse(content.toString());
    }

    private HashMap<String, Integer> wordCount(String[] words) {
        ArrayList<String> wordsList = new ArrayList(Arrays.asList(words));
        HashMap<String, Integer> map = new HashMap();

        for (int i = 0; i < wordsList.size(); i++) {
            String firstWord = wordsList.get(i);
            int count = 1;
            for (int j = 1; j < wordsList.size(); j++) {
                String secondWord = wordsList.get(j);
                if (firstWord.equals(secondWord)) {
                    count++;
                    wordsList.remove(j);
                    j--;
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
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(System.out::println);
    }
}
