package com.example.textprocessingtool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataManagement {

    public String searchText(String pattern, String text){
        try {
            Matcher matcher = getMatcher(pattern, text);
            StringBuilder foundText = new StringBuilder();
            int lastIndex = 0;
            while (matcher.find()) {
                foundText.append(text, lastIndex, matcher.start());
                foundText.append(matcher.group());
                lastIndex = matcher.end();
            }
            foundText.append(text.substring(lastIndex));
            return foundText.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String replaceText(String pattern, String text, String replacement){
        try {
            Matcher matcher = getMatcher(pattern, text);
            return matcher.replaceAll(replacement);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Matcher getMatcher(String pattern, String text){
        Pattern searchPattern = Pattern.compile(pattern);
        return searchPattern.matcher(text);
    }
}
