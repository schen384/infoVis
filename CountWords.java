import java.io.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.System;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Comparator;
import java.lang.String;
import java.io.BufferedReader;



public class CountWords {

    private Map<String, Integer> wordCounts;
    private int totalCount;

    public CountWords(String fileName) throws FileNotFoundException {
        wordCounts = new HashMap<>();
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String line = null;
            int index = 0;
            while ((line = fileReader.readLine()) != null) {
                totalCount++;
                String[] curWords = line.split("\\s+");
                for(String curWord: curWords) {
                    //System.out.println(curWord);
                    if (wordCounts.get(curWord) == null) {
                        // new word
                        wordCounts.put(curWord, 1);
                    } else {
                        // existing word
                        int curWordCount = wordCounts.get(curWord);
                        wordCounts.put(curWord, ++curWordCount);
                        //System.out.println("cur word count = " + curWordCount);
                    }
                }
            }
        } catch (IOException e) {}
    }


//        Scanner fileScanner = new Scanner(new File(fileName));
//        while (fileScanner.hasNext()) {
//            totalCount++;
//            String curWord = fileScanner.next();
//            //System.out.println(curWord + "   " + wordCounts.get(curWord));
//            if (wordCounts.get(curWord) == null) {
//                // new word
//                wordCounts.put(curWord, 1);
//            } else {
//                // existing word
//                int curWordCount = wordCounts.get(curWord);
//                wordCounts.put(curWord, ++curWordCount);
//                //System.out.println("cur word count = " + curWordCount);
//            }
//        }
//    }

    public Set<String> getWords() {
        return wordCounts.keySet();
    }

    public Set<String> getWordsRankedOld() {
        Comparator<String> rankComparator = new Comparator<String>() {
            public int compare(String k1, String k2) {
                return wordCounts.get(k1) - wordCounts.get(k2);
            }
        };
        TreeSet<String> rankedWords = new TreeSet<>(rankComparator);
        rankedWords.addAll(wordCounts.keySet());
        return rankedWords;
    }

    public Set<String> getWordsRanked() {
        Comparator<String> rankComparator =

                (String k1, String k2) ->
                        wordCounts.get(k1) - wordCounts.get(k2);

        TreeSet<String> rankedWords = new TreeSet<>(rankComparator);
        rankedWords.addAll(wordCounts.keySet());
        return rankedWords;
    }

    public int getCount(String word) {
        return wordCounts.get(word);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public static void main(String[] args) throws FileNotFoundException {
//        CountWords wc = new CountWords(args[0]);
//        Set<String> stopWords = new HashSet<>(Arrays.asList(
//                "a", "an", "and", "are", "as", "be", "by", "is", "in", "of",
//                "for", "from", "not", "to", "the", "that", "this", "with", "which"
//        ));
////        wc.wordCounts.entrySet().stream()
////                .filter(entry -> !stopWords.contains(entry.getKey().toLowerCase()))
////                .sorted((e1, e2) -> e1.getValue() - e2.getValue())
////                .forEach(entry ->
////                        System.out.printf("%s occurs %d times%n", entry.getKey(),
////                                entry.getValue()));
//
//        try {
//            PrintWriter writer = new PrintWriter("liberianGeneral.csv", "UTF-8");
//            writer.println("\"name\",\"word\",\"count\"");
//            wc.wordCounts.entrySet().stream()
//                    .filter(entry -> (!stopWords.contains(entry.getKey().toLowerCase())))
//                            .sorted((e1, e2) -> e1.getValue() - e2.getValue())
//                            //.sorted(entry -> entry.getKey().replaceAll("\\d+", ""))
//                            .forEach(entry ->
//                            {
//                                System.out.printf("%s occurs %d times%n", entry.getKey(), entry.getValue());
//                                writer.printf("\"%s\",\"%s\",%d%n", entry.getKey(), entry.getKey(), entry.getValue());
//                            });
//            writer.close();
//        } catch (IOException e) {
//            System.out.println("IOException Unable to write to file");
//        }

        CountWords wc = new CountWords(args[0]);


        Set<String> tagWords = new HashSet<>(Arrays.asList(
                "#Liberia", "Liberia", "election", "#elections", "vote", "2014", "excited", "peace", "#Liberia2011",
                "politics", "voting", "polls", "Nobel", "liberia", "liberiaelection", "liberia2011"
        ));

        try {
            PrintWriter writer = new PrintWriter("liberianGeneral.csv", "UTF-8");
            writer.println("\"name\",\"word\",\"count\"");
            wc.wordCounts.entrySet().stream()
                    //.filter(entry -> entry.getValue() > 1) //tells you which words occur more than once
                    .filter(entry -> (tagWords.contains(entry.getKey()))) //only includes tag words
                    .sorted((e1, e2) -> e1.getValue() - e2.getValue())
                    .forEach(entry ->
                    {
                        //System.out.printf("%s occurs %d times%n", entry.getKey(), entry.getValue());
                        writer.printf("\"%s\",\"%s\",%d%n", entry.getKey(), entry.getKey(), entry.getValue());
                    });
            writer.close();
        } catch (IOException e) {
            System.out.println("IOException Unable to write to file");
        }



        for (String word: wc.getWordsRanked()) {
             System.out.println("\"" + word + "\"" + " occurs " + wc.getCount(word)
                                + " times in " + args[0]);
        }
        System.out.println(args[0] + " contains " + wc.getTotalCount()
                            + "  words.");
        System.out.println(args[0] + " contains " + wc.getWords().size()
                            + " distinct words.");
    }
}