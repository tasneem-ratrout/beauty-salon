package com.mycompany.beautySalonPro;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class profileEmpcontroller {
	 @FXML
	    private TextField usernameField;
	    @FXML
	    private TextField firstNameField;
	    @FXML
	    private TextField lastNameField;
	    @FXML
	    private TextField salaryField;
	    @FXML
	    private TextField ssnField;
	    @FXML
	    private TextField oldUsernameField;
	@FXML
	void backaction (MouseEvent event)  throws IOException{
		App.setRoot("employeePage");
	}
	
	@FXML
	void changepassaction (ActionEvent event)  throws IOException{
		App.setRoot("Verification");
	}
	
	  @FXML
	    void initialize() {
	        loadEmployeeData();
	    }

	    private void loadEmployeeData() {
	        String employee_username = LoginController.getLoggedInUsername(); // الحصول على اسم المستخدم المسجل
	        String query = "SELECT * FROM employees WHERE employee_username = ?";

	        try (Connection conn = DatabaseConnection.connect(); // الاتصال بقاعدة البيانات
	             PreparedStatement pstmt = conn.prepareStatement(query)) {

	            pstmt.setString(1, employee_username); // تعيين اسم المستخدم في الاستعلام
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) { // إذا تم العثور على سجل
	                // ملء حقول النص بالمعلومات المسترجعة
	                firstNameField.setText(rs.getString("first_name"));
	                lastNameField.setText(rs.getString("last_name"));
	                salaryField.setText(String.valueOf(rs.getDouble("salary")));
	                ssnField.setText(rs.getString("ssn"));
	                usernameField.setText(employee_username); // تعيين اسم المستخدم
	            } else {
	                // إذا لم يتم العثور على موظف
	                System.out.println("No employee found with username: " + employee_username);
	            }
	        } catch (Exception e) {
	            e.printStackTrace(); // طباعة الخطأ في حالة حدوث استثناء
	        }
	    }
	    @FXML
	    public void update() {
	        String currentUsername = usernameField.getText();
	        // Get the updated values from the text fields
	        String firstName = firstNameField.getText();
	        String lastName = lastNameField.getText();
	        String salary = salaryField.getText(); // Assuming salary is editable in the future
	        String ssn = ssnField.getText();

	        // SQL update query
	        String updateQuery = "UPDATE employees SET first_name = ?, last_name = ?, salary = ?, ssn = ? WHERE employee_username = ?";

	        try (Connection conn = DatabaseConnection.connect(); // Connect to the database
	             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

	            // Set the parameters for the update query
	            pstmt.setString(1, firstName);
	            pstmt.setString(2, lastName);
	            pstmt.setDouble(3, Double.parseDouble(salary)); // Ensure salary is a double
	            pstmt.setString(4, ssn);
	            pstmt.setString(5, currentUsername);

	            // Execute the update
	            int rowsAffected = pstmt.executeUpdate();
	            if (rowsAffected > 0) {
	                JOptionPane.showMessageDialog(null, "Profile updated successfully!", "Update Success", JOptionPane.INFORMATION_MESSAGE);
	                // Optionally, you can reload the employee data to reflect the changes
	                loadEmployeeData();
	            } else {
	                JOptionPane.showMessageDialog(null, "No profile found to update.", "Update Error", JOptionPane.ERROR_MESSAGE);
	            }
	        } catch (Exception e) {
	            e.printStackTrace(); // Print error in case of an exception
	        }
	    }

	    }
