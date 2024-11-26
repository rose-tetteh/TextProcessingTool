package com.example.textprocessingtool;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataManagement {

    /**
     * Find matches list.
     *
     * @param pattern   the pattern
     * @param inputText the input text
     * @return the list
     */
    public List<Text> findMatches(String pattern, String inputText) {
        List<Text> textSegments = new ArrayList<>();

        try {
            Matcher matcher = getMatcher(pattern, inputText);
            int lastEnd = 0;

            while (matcher.find()) {
                if (matcher.start() > lastEnd) {
                    Text normalText = new Text(inputText.substring(lastEnd, matcher.start()));
                    textSegments.add(normalText);
                }

                Text matchText = new Text(matcher.group());
                matchText.setFill(Color.BLUE);
                textSegments.add(matchText);

                lastEnd = matcher.end();
            }

            if (lastEnd < inputText.length()) {
                Text remainingText = new Text(inputText.substring(lastEnd));
                textSegments.add(remainingText);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error processing pattern: " + e.getMessage());
        }

        return textSegments;
    }


    /**
     * Replace text string.
     *
     * @param pattern     the pattern
     * @param text        the text
     * @param replacement the replacement
     * @return the string
     */
    public String replaceText(String pattern, String text, String replacement){
        try {
            Matcher matcher = getMatcher(pattern, text);
            return matcher.replaceAll(replacement);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Find count int.
     *
     * @param pattern   the pattern
     * @param inputText the input text
     * @return the int
     */
    public int findCount(String pattern, String inputText) {
        Matcher matcher = getMatcher(pattern, inputText);
        int count = 0;

        while (matcher.find()) {
            count++;
        }

        return count;
    }

    /**
     * Count words int.
     *
     * @param inputText the input text
     * @return the int
     */
    public int countWords(String inputText) {
        if (inputText == null || inputText.trim().isEmpty()) {
            return 0;
        }
        String[] words = inputText.trim().split("\\s+");
        return words.length;
    }


    private Matcher getMatcher(String pattern, String text){
        Pattern searchPattern = Pattern.compile(pattern);
        return searchPattern.matcher(text);
    }
}
