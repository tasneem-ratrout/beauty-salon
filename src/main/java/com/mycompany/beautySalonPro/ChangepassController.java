package com.mycompany.beautySalonPro;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangepassController {

    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField confirmPasswordField;

    @FXML
    void handleChangePassword(MouseEvent event) throws IOException {
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Password fields cannot be empty!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match!");
            return;
        }

        if (!isPasswordStrong(newPassword)) {
            showAlert("Error", "Password must be at least 8 characters long and contain letters and numbers.");
            return;
        }

        String username = Verificationcontroller.getSavedUsername();
        String ssn = Verificationcontroller.getSavedSSN();
        String userType = Verificationcontroller.getUserType();

        if (userType == null) {
            showAlert("Error", "User type not specified!");
            return;
        }

        if (!userExists(username, ssn, userType)) {
            showAlert("Error", "User does not exist!");
            return;
        }

        boolean success = changePasswordInDatabase(username, ssn, newPassword, userType);
        if (success) {
            showAlert("Success", "Password changed successfully!");
        } else {
            showAlert("Error", "Failed to change password!");
        }
    }

    private boolean isPasswordStrong(String password) {
        return password.length() >= 8 && password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*");
    }

    private boolean changePasswordInDatabase(String username, String ssn, String newPassword, String userType) {
        String query = "UPDATE " + (userType.equals("User") ? "\"customer\"" : "\"employees\"") + 
                       " SET password = ? WHERE " + (userType.equals("User") ? "username" : "\"employee_username\"") + 
                       " = ? AND ssn = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.setString(3, ssn);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean userExists(String username, String ssn, String userType) {
        String query = "SELECT COUNT(*) FROM " + (userType.equals("User") ? "\"customer\"" : "\"employees\"") + 
                       " WHERE " + (userType.equals("User") ? "username" : "\"employee_username\"") + 
                       " = ? AND ssn = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, ssn);
            ResultSet rs = pstmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void beforeLabelAction(MouseEvent event) throws IOException {
        App.setRoot("Verification");
    }
}
