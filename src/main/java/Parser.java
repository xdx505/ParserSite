import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

public class Parser {
    private final String path;

    public Parser(HTMLPage page) {
        this.path = page.getFilePath();
    }

    public void parse() {
        try {
            File file = new File(path);
            Document page = Jsoup.parse(file, "UTF-8");
            Element bodyFrame = page.body();

            ArrayList<TextNode> textNodeList = getAllTextNodes(bodyFrame);
            ArrayList<String> linesFromBlocks = injectLines(textNodeList);
            ArrayList<String> textFromLines = wordsExtractor(linesFromBlocks);
            printResult(wordCount(textFromLines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<TextNode> getAllTextNodes(Node element) {
        ArrayList<TextNode> arrayList = new ArrayList<>();
        for (Node node : element.childNodes()) {
            if (node instanceof TextNode && !((TextNode) node).isBlank()) {
                arrayList.add((TextNode) node);
            } else {
                arrayList.addAll(getAllTextNodes(node));
            }
        }
        //TODO if arrayList is null
        return arrayList;
    }

    private ArrayList<String> injectLines(ArrayList<TextNode> textNodes) {
        ArrayList arrayList = new ArrayList<String>();

        if (!textNodes.isEmpty())
            for (int i = 0; i < textNodes.size(); i++) {
                arrayList.add(textNodes.get(i).text().toUpperCase().trim().replaceAll("[^A-ZА-Я]", " "));
            }

        return arrayList;
    }

    private ArrayList<String> wordsExtractor(ArrayList<String> list) {
        ArrayList<String> wordsList = new ArrayList<>();
        for (String line : list) {
            if (line.contains(" ")) {
                String[] wordsFromLine = line.split(" ");
                wordsList.addAll(Arrays.asList(wordsFromLine));
            }
        }
        return wordsList;
    }

    private HashMap<String, Integer> wordCount(ArrayList<String> words) {
        List<String> wordsList = words;
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
                .sorted(Map.Entry.<String, Integer>comparingByValue()).forEach(System.out::println);
    }
}
