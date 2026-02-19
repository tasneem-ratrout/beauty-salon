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

public class spaupdatecontroller {
	 @FXML
	    private TextArea descriptionTextArea;
	 @FXML
	    private TextArea priceTextArea;
	
	@FXML
	void bodyspaactionbutten (MouseEvent event)  throws IOException{
		App.setRoot("servicemang");
	}
	
	

	@FXML
	void Pricechangeaction (ActionEvent event)  throws IOException{
		String newPrice = priceTextArea.getText().trim();
	    if (newPrice.isEmpty()) {
	        showAlert("Invalid input", "Please enter a valid price.", Alert.AlertType.ERROR);
	        return;
	    }
	    updatePriceInDatabase("BODY SPA", newPrice);
	
	}
	@FXML
	void Descriptionchangedaction (ActionEvent event)  throws IOException{
		String newDescription = descriptionTextArea.getText().trim();
	    if (newDescription.isEmpty()) {
	        showAlert("Invalid input", "Please enter a valid description.", Alert.AlertType.ERROR);
	        return;
	    }
	    updateDescriptionInDatabase("BODY SPA", newDescription);
	}
	@FXML
	void initialize()
    {
	   loadServiceInformation("BODY SPA");
	}
	private void loadServiceInformation(String serviceName) {
        String query = "SELECT description,price FROM services WHERE service_name = ?";
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) 
        {
            preparedStatement.setString(1, serviceName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String description = resultSet.getString("description");
                descriptionTextArea.setText(description);
                String price = resultSet.getString("price");
                priceTextArea.setText(price);
            } else {
                showAlert("No service found", "Service with name " + serviceName + " does not exist.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error loading service information", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
	
    private void updatePriceInDatabase(String serviceName, String newPrice) {
        String query = "UPDATE services SET price = ? WHERE service_name = ?";
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setDouble(1, Double.parseDouble(newPrice)); // تحويل السعر إلى Double
            preparedStatement.setString(2, serviceName);
            
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Price updated successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "No service found with that name.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error updating price", e.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            showAlert("Invalid price format", "Please enter a valid number for the price.", Alert.AlertType.ERROR);
        }
    }
    
    private void updateDescriptionInDatabase(String serviceName, String newDescription) {
        String query = "UPDATE services SET description = ? WHERE service_name = ?";
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setString(1, newDescription); // تحديث الوصف
            preparedStatement.setString(2, serviceName);
            
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Description updated successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "No service found with that name.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error updating description", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
