package com.mycompany.beautySalonPro; 

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

public class Addemployeescontror {
    @FXML
    private TextArea SSN;
    @FXML
    private TextArea usernameField;
    @FXML
    private TextArea firstNameField;
    @FXML
    private TextArea lastNameField;
    @FXML
    private TextArea contactField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextArea salaryField;
    @FXML
    private ChoiceBox<String> positionField; // Change TextArea to ChoiceBox

    @FXML
    private void backActionEventeWW(MouseEvent event) throws IOException {
        App.setRoot("Employee InformationAdmain");
    }

    @FXML
    private void initialize() {
        loadPositions(); // Load positions when the controller is initialized
    }

    private void loadPositions() {
        // Adding positions directly
        String[] positionsArray = {
            "Hair Styling",
            "Make Up",
            "Nails",
            "Body Spa",
            "Hair Cuts",
            "Coloring"
        };
        
        positionField.getItems().addAll(positionsArray);
    }

    @FXML
    private void addUser(ActionEvent event) {
        String ssn = SSN.getText().trim();
        String username = usernameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String contact = contactField.getText().trim();
        String password = passwordField.getText().trim();
        String position = positionField.getValue(); // Get selected position
        String salary = salaryField.getText().trim();

        if (ssn.isEmpty() || username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || 
            contact.isEmpty() || password.isEmpty() || position == null || salary.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (isSSNExists(ssn)) {
                JOptionPane.showMessageDialog(null, "The SSN already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (isUsernameExists(username)) {
                JOptionPane.showMessageDialog(null, "The username already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            addEmployeeToDatabase(username, firstName, lastName, contact, password, position, salary, ssn);
            JOptionPane.showMessageDialog(null, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while adding the employee. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean isSSNExists(String ssn) throws SQLException {
        String query = "SELECT COUNT(*) FROM employees WHERE ssn = ?";
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, ssn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    private boolean isUsernameExists(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM employees WHERE employee_username = ?"; // استخدام employee_username
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    private void addEmployeeToDatabase(String username, String firstName, String lastName, 
            String contact, String password, String position, String salary, 
            String ssn) throws SQLException {
// تحويل salary إلى BigDecimal
BigDecimal salaryDecimal;
try {
salaryDecimal = new BigDecimal(salary); // تحويل السلسلة النصية إلى BigDecimal
} catch (NumberFormatException e) {
throw new SQLException("Salary must be a valid number.");
}

String sql = "INSERT INTO employees (employee_username, ssn, password, first_name, last_name, contact, position, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

try (Connection conn = DatabaseConnection.connect();
PreparedStatement pstmt = conn.prepareStatement(sql)) {
pstmt.setString(1, ssn); // employee_username
pstmt.setString(2, username);
pstmt.setString(3, password);
pstmt.setString(4, firstName);
pstmt.setString(5, lastName);
pstmt.setString(6, contact);
pstmt.setString(7, position);
pstmt.setBigDecimal(8, salaryDecimal); // استخدم setBigDecimal لإدخال salary

pstmt.executeUpdate();
}
}
}
