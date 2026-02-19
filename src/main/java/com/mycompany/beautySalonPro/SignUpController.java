package com.mycompany.beautySalonPro;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SignUpController {
    @FXML
    private TextField usernameTF;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField firstnameTF;
    @FXML
    private TextField lastnameTF;
    @FXML
    private TextField contactTF;
    @FXML
    private TextField contactTF1;

    @FXML
    void signInButton(ActionEvent event) throws IOException {
        String username = usernameTF.getText();
        String password = passwordField.getText();
        String firstName = firstnameTF.getText();
        String lastName = lastnameTF.getText();
        String contact = contactTF.getText();
        String ssn = contactTF1.getText();

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || contact.isEmpty() || ssn.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        String passwordError = validatePassword(password);
        if (passwordError != null) {
            showAlert("Password Error", passwordError);
            return;
        }

        if (isUsernameOrSsnExists(username, ssn)) {
            showAlert("Error", "Username or SSN already exists.");
            return;
        }

        try (Connection connection = DatabaseConnection.connect()) {
            String query = "INSERT INTO customer (username, ssn, password, first_name, last_name, contact) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, ssn);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, firstName);
            preparedStatement.setString(5, lastName);
            preparedStatement.setString(6, contact);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Account created successfully.");
            } else {
                showAlert("Error", "Account creation failed.");
            }
        } catch (SQLException e) {
            showAlert("Error", "An error occurred during registration. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    void loginLabelAction(MouseEvent event) throws IOException {
        App.setRoot("login");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isUsernameOrSsnExists(String username, String ssn) {
        String query = "SELECT COUNT(*) FROM customer WHERE username = ? OR ssn = ?";
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, ssn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String validatePassword(String password) {
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        if (!Pattern.compile(".*[0-9].*").matcher(password).find()) {
            return "Password must contain at least one number.";
        }
        if (!Pattern.compile(".*[a-zA-Z].*").matcher(password).find()) {
            return "Password must contain at least one letter.";
        }
        return null; // No error
    }
}
