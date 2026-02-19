package com.mycompany.beautySalonPro;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Verificationcontroller {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField ssnField;

    @FXML
    private CheckBox userCheckBox;

    @FXML
    private CheckBox adminCheckBox;

    @FXML
    private Label beforepage;

    
    private static String savedUsername;
    private static String savedSSN;
    private static String userType; 

    @FXML
    private void handleVerification() {
        String username = usernameField.getText().trim();
        String ssn = ssnField.getText().trim();

       
        if (username.isEmpty() || ssn.isEmpty()) {
            showAlert("Error", "Username and SSN cannot be empty.");
            return;
        }

        
        String query = "";
        if (userCheckBox.isSelected()) {
            query = "SELECT * FROM customer WHERE username = ? AND ssn = ?";
            userType = "User";
        } else if (adminCheckBox.isSelected()) {
            query = "SELECT * FROM employees WHERE \"employee_username\" = ? AND ssn = ?";
            userType = "Admin";
        } else {
            showAlert("Error", "Please select either User or Admin.");
            return;
        }

        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, ssn);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                
                savedUsername = username; 
                savedSSN = ssn; 

                
                App.setRoot("changepass"); 
            } else {
                showAlert("Error", "Invalid username or SSN.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while verifying.");
        }
    }

    
    @FXML
    private void handleCheckBoxSelection() {
        if (userCheckBox.isSelected()) {
            adminCheckBox.setSelected(false);
        } else if (adminCheckBox.isSelected()) {
            userCheckBox.setSelected(false);
        }
    }

    public static String getSavedUsername() {
        return savedUsername;
    }

    public static String getSavedSSN() {
        return savedSSN;
    }

    public static String getUserType() {
        return userType;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void beforeLabelAction(MouseEvent event) throws IOException {
        App.setRoot("login"); 
    }
}
