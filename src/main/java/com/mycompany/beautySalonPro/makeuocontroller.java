package com.mycompany.beautySalonPro;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

public class makeuocontroller {
	 @FXML
	    private ChoiceBox<String> dayChoiceBox;
	    @FXML
	    private ChoiceBox<String> hourChoiceBox;
	    @FXML
	    private TextArea descriptionTextArea;
	    @FXML
	    private TextArea priceTextArea;
	    @FXML
	    private ListView<String> employeesListView;

	    @FXML
	    void initialize() {
	        List<String> days = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
	        ObservableList<String> dayOptions = FXCollections.observableArrayList(days);
	        dayChoiceBox.setItems(dayOptions);

	        List<String> hours = Arrays.asList("09:00 AM",  "10:00 AM",  "11:00 AM", "12:00 PM", "01:00 PM",  "02:00 PM",  "03:00 PM");
	        ObservableList<String> hourOptions = FXCollections.observableArrayList(hours);
	        hourChoiceBox.setItems(hourOptions);

	        loadServiceInformation("MAKE UP");
	        loadEmployees("MAKE UP");
	    }

	    private void loadServiceInformation(String serviceName) {
	        String query = "SELECT description, price, duration_time FROM services WHERE service_name = ?";
	        try (Connection connection = DatabaseConnection.connect();
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, serviceName);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                String description = resultSet.getString("description");
	                String price = resultSet.getString("price");
	                String duration = resultSet.getString("duration_time");

	                descriptionTextArea.setText(description + "\nDuration: " + duration);
	                priceTextArea.setText("$" + price);
	            }
	        } catch (SQLException e) {
	            showAlert("Error loading service information", e.getMessage());
	        }
	    }

	    private void loadEmployees(String service) {
	        String query = "SELECT employee_username FROM employees WHERE position = ?";
	        ObservableList<String> employeeNames = FXCollections.observableArrayList();
	        
	        try (Connection connection = DatabaseConnection.connect();
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, service);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            while (resultSet.next()) {
	                String username = resultSet.getString("employee_username");
	                employeeNames.add(username);
	            }

	            employeesListView.setItems(employeeNames);
	        } catch (SQLException e) {
	            showAlert("Error loading employees", e.getMessage());
	        }
	    }

	    private boolean isAppointmentBooked(String employeeName, String appointmentDetails) {
	        String query = "SELECT COUNT(*) FROM appointments WHERE employee_username = ? AND appointment_day = ? AND status = 'booked'";
	        try (Connection connection = DatabaseConnection.connect();
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, employeeName);
	            preparedStatement.setString(2, appointmentDetails);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            
	            if (resultSet.next()) {
	                return resultSet.getInt(1) > 0;
	            }
	        } catch (SQLException e) {
	            showAlert("Error checking appointment", e.getMessage());
	        }
	        return false;
	    }

	    private void addAppointmentToDatabase(String employeeName, String selectedDay, String selectedHour) {
	        String query = "INSERT INTO appointments (customer_username, employee_username, service_name, appointment_day, status) VALUES (?, ?, ?, ?, ?)";
	        try (Connection connection = DatabaseConnection.connect();
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, UserSession.getUsername());
	            preparedStatement.setString(2, employeeName);
	            preparedStatement.setString(3, "MAKE UP");
	            
	            String appointmentDetails = selectedDay + " " + selectedHour;
	            preparedStatement.setString(4, appointmentDetails); 
	            preparedStatement.setString(5, "booked");

	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            showAlert("Error adding appointment", e.getMessage());
	        }
	    }

	    private void showAlert(String title, String message) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
	    private void showAlert1(String title, String message) {
	    	
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }

	    @FXML
	    void bookingnowaction(MouseEvent event) {
	        String selectedEmployee = employeesListView.getSelectionModel().getSelectedItem();
	        String selectedDay = dayChoiceBox.getValue();
	        String selectedHour = hourChoiceBox.getValue();

	        if (selectedEmployee == null || selectedDay == null || selectedHour == null) {
	            showAlert("Error", "Please select an employee, day, and hour.");
	            return;
	        }

	        String appointmentDetails = selectedDay + " " + selectedHour;

	        if (isAppointmentBooked(selectedEmployee, appointmentDetails)) {
	            showAlert("Error", "The appointment is already booked for this time.");
	        } else {
	            addAppointmentToDatabase(selectedEmployee, selectedDay, selectedHour);
	            showAlert1("Success", "Appointment booked successfully.");
	        }
	    }

	    @FXML
	    void beforeLabelAction(MouseEvent event) throws IOException {
	        App.setRoot("user");
	    }
	

}
