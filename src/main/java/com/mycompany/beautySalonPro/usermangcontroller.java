package com.mycompany.beautySalonPro;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class usermangcontroller {

    @FXML
    private TextArea usertextarea;

    @FXML
    void buttenmangeaction(MouseEvent event) throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    void enteractionevent(ActionEvent event) throws IOException {
        String username = usertextarea.getText();
        if (username == null || username.isEmpty()) {
            showAlert("Input Error", "Username cannot be empty.");
            return;
        }

        // التحقق من وجود المستخدم والحصول على معلوماته
        if (getUserInfo(username)) {
            // حفظ معلومات المستخدم في UserSession
            UserSession.setUsername(username);
            UserSession.setFirstName(currentFirstName);
            UserSession.setLastName(currentLastName);
            UserSession.setContact(currentContact);

            // إذا وُجد المستخدم، قم بتحميل الصفحة ProfileAdmain
            App.setRoot("ProfileAdmain");
        } else {
            showAlert("User Not Found", "The username does not exist. Please try again.");
        }
    }

    private String currentFirstName;
    private String currentLastName;
    private String currentContact;

    public boolean getUserInfo(String username) {
        boolean exists = false; 
        String query = "SELECT first_name, last_name, contact FROM customer WHERE username = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // احفظ معلومات المستخدم
                exists = true; // المستخدم موجود
                currentFirstName = rs.getString("first_name");
                currentLastName = rs.getString("last_name");
                currentContact = rs.getString("contact");
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
