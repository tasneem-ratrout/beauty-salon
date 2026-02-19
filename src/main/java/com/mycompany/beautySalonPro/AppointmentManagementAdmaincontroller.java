package com.mycompany.beautySalonPro;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class AppointmentManagementAdmaincontroller {
    @FXML
    private ListView<String> appointmentListView;
    
    private ObservableList<String> appointments;
    @FXML
    void backActionEvent(MouseEvent event) throws IOException {
        App.setRoot("dashboard");
    }
   


    @FXML
    public void initialize() {
        appointments = FXCollections.observableArrayList();
        fetchAppointmentsFromDatabase();
        appointmentListView.setItems(appointments);
    }

    private void fetchAppointmentsFromDatabase() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.connect();

            String query = "SELECT * FROM appointments"; // Check if the table name is correct

            preparedStatement = connection.prepareStatement(query);
            
            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Loop through the results
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String customerUsername = resultSet.getString("customer_username");
                String employeeUsername = resultSet.getString("employee_username");
                String serviceUsername = resultSet.getString("service_name"); // Make sure this column exists
                String appointmentTime = resultSet.getString("appointment_day");
                String status = resultSet.getString("status");

                // Format the appointment record
                String appointmentRecord = String.format("ID: %d,Customer: %s,Employee: %s,Service: %s,Time: %s,Status: %s",
                        id, customerUsername, employeeUsername, serviceUsername, appointmentTime, status);
                
                // Add to appointments list
                appointments.add(appointmentRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
