package com.mycompany.beautySalonPro;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private TextField usernameField;  
    @FXML
    private TextField passwordField;  
    @FXML
    private Button loginButton;        

    private static String userRole;
    private static String loggedInUsername; 
    @FXML
    public void initialize() {
        roleChoiceBox.getItems().addAll("user", "admin", "employee");
    }

    @FXML
    void handleLogin(MouseEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleChoiceBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert("Fields cannot be empty", "Please fill in all fields.");
            return;
        }

        try (Connection connection = DatabaseConnection.connect()) {
            String query = "";
            if (role.equals("user")) {
                query = "SELECT * FROM customer WHERE username = ? AND password = ?";
            } else if (role.equals("admin")) {
                query = "SELECT * FROM employees WHERE employee_username = ? AND password = ? AND position = 'admin'";
            } else if (role.equals("employee")) {
               
                query = "SELECT * FROM employees WHERE employee_username = ? AND password = ?";
            }

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                
                userRole = role;
                loggedInUsername = username;
                if (role.equals("user")) {
                    UserSession.setUsername(resultSet.getString("username"));
                    UserSession.setFirstName(resultSet.getString("first_name"));
                    UserSession.setLastName(resultSet.getString("last_name"));
                    UserSession.setContact(resultSet.getString("contact"));
                    
                    App.setRoot("user");
                } else if (role.equals("admin")) {
                    App.setRoot("dashboard"); 
                } else if (role.equals("employee")) {
                	
                	Employee employee = new Employee();
                    Employee.setUsername(resultSet.getString("employee_username"));
                    Employee.setFirstName(resultSet.getString("first_name"));
                    Employee.setLastName(resultSet.getString("last_name"));
                    Employee.setContact(resultSet.getString("contact"));

                	
                	
                	
                    App.setRoot("employeePage"); 
                }
            } else {
                showAlert("Login failed", "Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getUserRole() {     
    	return userRole;  // إرجاع دور المستخدم
    }

    public static String getLoggedInUsername() {
        return loggedInUsername;  // إرجاع اسم المستخدم المسجل
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void RLabelAction(MouseEvent event) throws IOException {
        App.setRoot("Verification");
    }

    @FXML
    void signupLabelAction(MouseEvent event) throws IOException {
        App.setRoot("signup");
    }
}
