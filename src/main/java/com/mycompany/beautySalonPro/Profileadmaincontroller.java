package com.mycompany.beautySalonPro;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Profileadmaincontroller {

    @FXML
    private TextField usernameField;
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField contactField;

    @FXML
    void initialize() {
        usernameField.setText(UserSession.getUsername());
        firstNameField.setText(UserSession.getFirstName());
        lastNameField.setText(UserSession.getLastName());
        contactField.setText(UserSession.getContact());
    }

    private boolean isUsernameAvailable(String username) {
        String query = "SELECT COUNT(*) FROM customer WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    void updateUser(ActionEvent event) {
        String username = usernameField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String contact = contactField.getText();

        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || contact.isEmpty()) {
            showAlert("Input Error", "All fields must be filled.");
            return;
        }

        boolean isUsernameChanged = !username.equals(UserSession.getUsername());
        if (isUsernameChanged) {
            if (!isUsernameAvailable(username)) {
                showAlert("Username Taken", "The username already exists. Please choose a different username.");
                return;
            }
        }

        if (updateUserData(username, firstName, lastName, contact)) {
            UserSession.setUsername(username);
            UserSession.setFirstName(firstName);
            UserSession.setLastName(lastName);
            UserSession.setContact(contact);
            showAlert("Success", "User information updated successfully.");
        } else {
            showAlert("Update Error", "Failed to update user information.");
        }
    }

    private boolean updateUserData(String username, String firstName, String lastName, String contact) {
        String query = "UPDATE customer SET first_name = ?, last_name = ?, contact = ? WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, contact);
            pstmt.setString(4, UserSession.getUsername());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void backActionEvent(MouseEvent event) throws IOException {
        App.setRoot("usermang");
    }
}
