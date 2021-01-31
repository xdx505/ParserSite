import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class ParserTest {
    final String pagesFile = "C:\\Users\\Dan\\IdeaProjects\\ParserSite\\build\\libs\\pages\\habr_29.01_1545.html";
    final String filepath = "C:\\Users\\Dan\\IdeaProjects\\ParserSite\\build\\libs\\pages\\testNodes.html";

    @Test
    public void parseTest() throws IOException {
        File file = new File(pagesFile);
        Document document = Jsoup.parse(file, "UTF-8");
        document.getElementsByTag("pre").remove();
        document.getElementsByTag("img").remove();
        document.normalise(); // Moves any text content that is not in the body element into the body.


//        printResult(wordCount(wordsExtractor(document)));
    }

    @Test
    public void getAllNodesTest() throws IOException, InterruptedException {
        File file = new File(pagesFile);
        Document page = Jsoup.parse(file, "UTF-8");
        Element bodyFrame = page.body();

        ArrayList<TextNode> textNodeList = getAllTextNodes(bodyFrame);
        ArrayList<String> linesFromBlocks = injectLines(textNodeList);
        ArrayList<String> textFromLines = wordsExtractor(linesFromBlocks);
        printResult(wordCount(textFromLines));

    }

    private void print(ArrayList list) throws InterruptedException {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).toString());
            Thread.sleep(1000);
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
                .sorted(Map.Entry.<String, Integer>comparingByValue())
                .forEach(System.out::println);
    }
}