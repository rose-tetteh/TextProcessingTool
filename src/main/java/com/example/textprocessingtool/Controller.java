package com.example.textprocessingtool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Controller {
    public Button replaceButton;
    public Button searchButton;
    @FXML private TextArea inputTextArea;
    @FXML private TextField searchPattern;
    @FXML private TextField replaceText;
    @FXML private TextFlow resultTextFlow;

    @FXML private TextField nameField;
    @FXML private TextField valueField;
    @FXML private TableView<DataEntry> dataTable;
    @FXML private TableColumn<DataEntry, String> nameColumn;
    @FXML private TableColumn<DataEntry, String> valueColumn;

    private final ObservableList<DataEntry> dataEntries = FXCollections.observableArrayList();
    private final DataManagement dataManagement = new DataManagement();

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        dataTable.setItems(dataEntries);
    }

    /**
     * Handle search.
     */
    @FXML
    public void handleSearch() {
        resultTextFlow.getChildren().clear();
        String pattern = searchPattern.getText();
        String inputText = inputTextArea.getText();

        if (pattern.isEmpty() || inputText.isEmpty()) {
            showAlert("Invalid Input", "Please enter both pattern and text");
            return;
        }

        try{
            List<Text> textSegments = dataManagement.findMatches(pattern, inputText);

            if (textSegments.isEmpty() || textSegments.size() == 1 && textSegments.getFirst().getText().equals(inputText)) {
                showAlert("No Matches Found", "Please enter a text-pattern to search for!!!");
               return;
            }

            resultTextFlow.getChildren().addAll(textSegments);
        } catch (PatternSyntaxException e) {
            showAlert("Invalid Pattern", "Please check your regular expression syntax");
        }
    }

    /**
     * Handle replace.
     */
    @FXML
    public void handleReplace() {
        try {
            String pattern = searchPattern.getText();
            String replacement = replaceText.getText();
            String result = dataManagement.replaceText(pattern, inputTextArea.getText(), replacement);
            resultTextFlow.getChildren().add(new Text(result));
        } catch (RuntimeException e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
