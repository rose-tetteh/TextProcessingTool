package com.example.textprocessingtool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataManagement {

    public String searchText(String pattern, String text){
        try {
            Pattern searchPattern = Pattern.compile(pattern);
            Matcher matcher = searchPattern.matcher(text);
            StringBuilder foundText = new StringBuilder();
            while (matcher.find()) {
                foundText.append(matcher.group()).append("\n");
            }
            return foundText.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String replaceText(String pattern, String text, String replacement){
        try {
            Pattern searchPattern = Pattern.compile(pattern);
            Matcher matcher = searchPattern.matcher(text);
            return matcher.replaceAll(replacement);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
