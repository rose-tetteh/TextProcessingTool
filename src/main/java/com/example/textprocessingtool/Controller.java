package com.example.textprocessingtool;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    @FXML public TextArea inputTextArea;
    @FXML public TextField searchPattern;
    @FXML public TextField replaceText;
    @FXML public Button replaceButton;
    @FXML public Button searchButton;

    private final DataManagement dataManagement = new DataManagement();

    @FXML
    public void handleSearch(){
        String pattern = searchPattern.getText();
        inputTextArea.setText(dataManagement.searchText(inputTextArea.getText(), pattern));
    }

    @FXML
    public void handleReplace(){
        String pattern = searchPattern.getText();
        String replacement = replaceText.getText();
        inputTextArea.setText(dataManagement.replaceText(inputTextArea.getText(), pattern, replacement));
    }
}
