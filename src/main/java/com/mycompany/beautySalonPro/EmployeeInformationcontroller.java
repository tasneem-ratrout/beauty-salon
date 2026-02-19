package com.mycompany.beautySalonPro;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField; // إضافة لتضمين TextField
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class EmployeeInformationcontroller {
    @FXML
    private TextArea textArea;
    @FXML
    private TextArea textArea2;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button loadButton;

    private Connection connection;

    // دالة تهيئة الاتصال بقاعدة البيانات
    public void initialize() {
        connection = DatabaseConnection.connect();
    }

    @FXML
    void backActionEvent(MouseEvent event) throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    private void handleaddButtonAction(ActionEvent event) throws IOException {
        App.setRoot("Addemployees");
    }

    // دالة لتحميل معلومات الموظف عند الضغط على زر التحميل
    @FXML
    void handleLoadButtonAction() {
        String employeeName = textArea2.getText().trim(); // أخذ اسم الموظف من textArea2
        loadEmployeeInfo(employeeName);
    }

    // دالة لتحميل المعلومات عند الضغط على Enter
    @FXML
    void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLoadButtonAction(); // استدعاء دالة التحميل عند الضغط على Enter
        }
    }

    // دالة لجلب معلومات الموظف من قاعدة البيانات
    public void loadEmployeeInfo(String employeeName) {
        String query = "SELECT * FROM employees WHERE employee_username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, employeeName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String employeeInfo = "Username: " + rs.getString("employee_username") + "\n" +
                                      "Password: " + rs.getString("password") + "\n" +
                                      "Last Name: " + rs.getString("last_name") + "\n" +
                                      "First Name: " + rs.getString("first_name") + "\n" +
                                      "Contact: " + rs.getString("contact") + "\n" +
                                      "Salary: " + rs.getString("salary") + "\n" +
                                      "Position: " + rs.getString("position");
                textArea.setText(employeeInfo);
            } else {
                textArea.setText("Employee not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            textArea.setText("Error loading employee information.");
        }
    }

    // حدث الزر "تحديث"
    @FXML
    void handleEditButtonAction() {
        String[] employeeData = textArea.getText().split("\n");
        String originalUsername = employeeData[0].split(": ")[1]; // استخراج اسم المستخدم الحالي
        String username = employeeData[0].split(": ")[1]; // اسم المستخدم الجديد
        String password = employeeData[1].split(": ")[1];
        String lastname = employeeData[2].split(": ")[1];
        String firstname = employeeData[3].split(": ")[1];
        String contact = employeeData[4].split(": ")[1];
        String salaryStr = employeeData[5].split(": ")[1];
        String position = employeeData[6].split(": ")[1];

        // تحويل salary إلى BigDecimal
        BigDecimal salary;
        try {
            salary = new BigDecimal(salaryStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            textArea.setText("Invalid salary format. Please enter a valid number.");
            return;
        }

        // التحقق إذا قام المستخدم بتعديل اسم المستخدم
        if (!username.equals(originalUsername)) {
            // إذا تم تعديل اسم المستخدم، تحقق من وجوده مسبقًا في الجدول
            String checkQuery = "SELECT COUNT(*) FROM employees WHERE employee_username = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    textArea.setText("Username already exists. Please choose a different username.");
                    return; // خروج من الدالة إذا كان اسم المستخدم موجودًا بالفعل
                }
            } catch (SQLException e) {
                e.printStackTrace();
                textArea.setText("An error occurred while checking the username.");
                return;
            }
        }

        // تحديث معلومات الموظف
        String updateQuery = "UPDATE employees SET password = ?, last_name = ?, first_name = ?, contact = ?, salary = ?, position = ? WHERE employee_username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setString(1, password);
            pstmt.setString(2, lastname);
            pstmt.setString(3, firstname);
            pstmt.setString(4, contact);
            pstmt.setBigDecimal(5, salary);
            pstmt.setString(6, position);
            pstmt.setString(7, originalUsername); // استخدام اسم المستخدم الأصلي لتحديث البيانات
            pstmt.executeUpdate();
            textArea.setText("Employee information updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            textArea.setText("An error occurred while updating employee information.");
        }
    }

    // حدث الزر "حذف"
    @FXML
    void handleDeleteButtonAction() {
        String[] employeeData = textArea.getText().split("\n");
        String username = employeeData[0].split(": ")[1]; // استخراج اسم المستخدم

        String query = "DELETE FROM employees WHERE employee_username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            textArea.setText("Employee deleted successfully.");
            System.out.println("Employee deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
            textArea.setText("Error deleting employee.");
        }
    }
}
