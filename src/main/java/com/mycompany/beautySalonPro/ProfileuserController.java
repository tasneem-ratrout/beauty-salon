package com.mycompany.beautySalonPro;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileuserController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField contactField;

    private String originalUsername;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        originalUsername = UserSession.getUsername();
        usernameField.setText(originalUsername);
        firstNameField.setText(UserSession.getFirstName());
        lastNameField.setText(UserSession.getLastName());
        contactField.setText(UserSession.getContact());
    }

    @FXML void homeaction(MouseEvent event) throws IOException {
        App.setRoot("user");
    }
    
    @FXML void profileaction(MouseEvent event) throws IOException {
        App.setRoot("profileuser");
    } 
    
    @FXML void Myreservationsaction(MouseEvent event) throws IOException {
        App.setRoot("mybooking");
    } 
    
    @FXML void logoutaction(MouseEvent event) throws IOException {
        UserSession.clearSession(); 
        App.setRoot("login");
    }
   
    
    @FXML void  passwordaction(MouseEvent event) throws IOException {
        UserSession.clearSession(); 
        App.setRoot("Verification");
    }
    
    

    @FXML
    void editProfileAction(MouseEvent event) throws IOException {
        String username = usernameField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String contact = contactField.getText();

        // تحقق من username فقط إذا تم تغييره
        if (!username.equals(originalUsername)) {
            if (isUsernameTaken(username)) {
                showAlert("Username already exists", "The username you entered is already taken. Please choose a different one.");
                return; // إرجاع هنا إذا كان الاسم موجودًا
            }
        }

        // تحديث البيانات في قاعدة البيانات
        updateProfile(username, firstName, lastName, contact);
        showAlert("Profile Updated", "Your profile has been updated successfully.");
    }

    private boolean isUsernameTaken(String username) {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT COUNT(*) FROM customer WHERE username = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateProfile(String username, String firstName, String lastName, String contact) {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "UPDATE customer SET username = ?, first_name = ?, last_name = ?, contact = ? WHERE username = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, contact);
            pstmt.setString(5, originalUsername); 

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
