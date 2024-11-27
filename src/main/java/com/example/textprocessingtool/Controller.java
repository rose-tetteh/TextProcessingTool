package com.example.textprocessingtool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * The type Controller.
 */
public class Controller {
    /**
     * The Replace button.
     */
    @FXML public Button replaceButton;
    /**
     * The Search button.
     */
    @FXML public Button searchButton;
    /**
     * The Word count label.
     */
    @FXML public Label wordCountLabel;
    /**
     * The Open button.
     */
    @FXML public Button openButton;
    /**
     * The Save button.
     */
    @FXML public Button saveButton;
    @FXML private TextArea inputTextArea;
    @FXML private TextField searchPattern;
    @FXML private TextField replaceText;
    @FXML private TextFlow resultTextFlow;
    @FXML private Label findCountLabel;

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
            int findCount = dataManagement.findCount(pattern, inputText);

            if (textSegments.isEmpty() || textSegments.size() == 1 && textSegments.getFirst().getText().equals(inputText)) {
                showAlert("No Matches Found", "No Matches Found in Text!");
               return;
            }

            resultTextFlow.getChildren().addAll(textSegments);
            findCountLabel.setText(String.valueOf(findCount));
            wordCountLabel.setText(String.valueOf(dataManagement.countWords(inputText)));
        } catch (PatternSyntaxException e) {
            showAlert("Invalid Pattern", "Please check your regular expression syntax");
        }
    }

    /**
     * Handle replace.
     */
    @FXML
    public void handleReplace() {
        resultTextFlow.getChildren().clear();
        try {
            String pattern = searchPattern.getText();
            String replacement = replaceText.getText();
            String result = dataManagement.replaceText(pattern, inputTextArea.getText(), replacement);
            resultTextFlow.getChildren().add(new Text(result));
            inputTextArea.setText(result);
            wordCountLabel.setText(String.valueOf(dataManagement.countWords(inputTextArea.getText())));
        } catch (RuntimeException e) {
            showAlert("Error", e.getMessage());
        }
    }

    /**
     * Handle add.
     * add an item to the list
     */
    @FXML
    public void handleAdd() {
        String[] inputs = validateAndGetInputs();

        if (inputs == null) return;

        String name = inputs[0];
        String value = inputs[1];

        DataEntry newEntry = new DataEntry(name, value);
        if (!dataEntries.contains(newEntry)) {
            dataEntries.add(newEntry);
            clearFields();
        } else {
            showAlert("Duplicate Entry", "An entry with this name already exists");
        }
    }

    /**
     * Handle update.
     * Update a selected item in the List
     */
    @FXML
    public void handleUpdate() {
       try {
           DataEntry selected = dataTable.getSelectionModel().getSelectedItem();
           if (selected == null) {
               showAlert("No Selection", "Please select an entry to update");
               return;
           }

           String[] inputs = validateAndGetInputs();
           if (inputs == null) return;

           String name = inputs[0];
           String value = inputs[1];

           selected.setName(name);
           selected.setValue(value);
           dataTable.refresh();
           clearFields();
       } catch (RuntimeException e) {
           showAlert("Error", e.getMessage());
       }
    }

    /**
     * Handle delete.
     */
    @FXML
    public void handleDelete() {
        DataEntry selected = dataTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an entry to delete");
            return;
        }

        dataEntries.remove(selected);
        clearFields();
    }

    private void clearFields() {
        nameField.clear();
        valueField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Use regular expression to validate that the name contains only letters and spaces
     * @param name the name to validate
     * @return true if the name is valid, false otherwise
     */
    private boolean validateName(String name) {
        if (name == null || name.isEmpty()) {
            return true;
        }
        name = name.trim();
        return name.matches("^[a-zA-Z ]+$");
    }

    private String[] validateAndGetInputs() {
        String keyName = nameField.getText();
        String value = valueField.getText();

        if (!validateName(keyName)) {
            showAlert("Invalid Name", "Please enter a valid name (letters and spaces only)");
            return null;
        }

        if (keyName.isEmpty() || value.isEmpty()) {
            showAlert("Invalid Input", "Please fill in both name and value");
            return null;
        }

        return new String[]{keyName, value};
    }

    /**
     * Handle save option clicked.
     * Save a file onto local storage of machine
     */
    @FXML
    public void handleSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        Stage stage = (Stage) inputTextArea.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            saveFile(file);
            showAlert("Saved", "File has been Saved");
        }
    }

    private void saveFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write(inputTextArea.getText());
        } catch (IOException e) {
           showAlert("Error", e.getMessage());
        }
    }


    /**
     * Handle open option clicked.
     * Open a file from local storage of machine
     */
    @FXML
    public void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        Stage stage = (Stage) inputTextArea.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) openFile(file);
    }

    private void openFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            inputTextArea.setText(content.toString());
        } catch (IOException e) {
            showAlert("Error", e.getMessage());
        }
    }
}
