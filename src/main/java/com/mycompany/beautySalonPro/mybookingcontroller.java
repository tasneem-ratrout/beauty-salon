package com.mycompany.beautySalonPro;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class mybookingcontroller {

    @FXML
    private ListView<HBox> bookingListView;

    @FXML
    void initialize() {
        loadBookings();
    }

    private void loadBookings() {
        String query = "SELECT appointment_day, service_name, employee_username FROM appointments WHERE customer_username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, UserSession.getUsername());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String appointmentDay = rs.getString("appointment_day");
                String serviceName = rs.getString("service_name");
                String employeeUsername = rs.getString("employee_username");

                HBox bookingItem = new HBox();
                bookingItem.setSpacing(10);

                VBox detailsBox = new VBox();
                detailsBox.setSpacing(5);

                Label detailsLabel = new Label(appointmentDay + " - " + serviceName + "\nwith " + employeeUsername);
                detailsLabel.setWrapText(true);

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(e -> deleteBooking(appointmentDay, bookingItem)); // تمرير bookingItem

                detailsBox.getChildren().addAll(detailsLabel, deleteButton);
                bookingItem.getChildren().add(detailsBox);
                bookingListView.getItems().add(bookingItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteBooking(String appointmentDay, HBox bookingItem) { 
        String deleteQuery = "DELETE FROM appointments WHERE appointment_day = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setString(1, appointmentDay);
            pstmt.executeUpdate();
            
            
            bookingListView.getItems().remove(bookingItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void homeaction(MouseEvent event) throws IOException {
        App.setRoot("user");
    }
    
    @FXML
    void deleteBookingAction(MouseEvent event) throws IOException {
        App.setRoot("user");
    }
    
    

    @FXML
    void profileaction(MouseEvent event) throws IOException {
        App.setRoot("profileuser");
    }

    @FXML
    void Myreservationsaction(MouseEvent event) throws IOException {
        App.setRoot("mybooking");
    }

    @FXML
    void logoutaction(MouseEvent event) throws IOException {
        App.setRoot("login");
    }
}
