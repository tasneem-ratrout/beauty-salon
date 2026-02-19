package com.mycompany.beautySalonPro;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class mybookingEmpcontroller {
	  @FXML
	    private Button delete;
    @FXML
    private ListView<String> appointmentListView;

    @FXML
    void backAction(MouseEvent event) throws IOException {
        App.setRoot("employeePage");
    }
    
    @FXML
    void initialize() {
        String employee_username = LoginController.getLoggedInUsername();

        loadAppointments(employee_username); // تحميل المواعيد للموظف
    }

    private void loadAppointments(String employee_username) {
        boolean hasAppointments = false; // علامة للتحقق من وجود مواعي
        String query = "SELECT * FROM appointments WHERE employee_username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employee_username); //ت
            ResultSet rs = pstmt.executeQuery();

            // Clear existing items in the ListView
            appointmentListView.getItems().clear(); // مسح العناصر الموجودة في ListView

            // Check if there are appointments

            // Retrieve and display the appointments in the ListView
            while (rs.next()) {
                int appointmentId = rs.getInt("id");
                String appointmentDay = rs.getString("appointment_day");
                String serviceName = rs.getString("service_name");
                String clientName = rs.getString("customer_username"); // معلومات عن العميل
                String status = rs.getString("status"); // حالة الموعد
                
                // إنشاء سلسلة معلومات الموعد
                String appointmentInfo =appointmentId+" - "+ appointmentDay + " - " + serviceName + " - " + clientName + " - " + status;
                appointmentListView.getItems().add(appointmentInfo); // إضافة المعلومات إلى ListView
                hasAppointments = true; // تعيين علامة إذا كان هناك موعد واحد على الأقل
            }

            // إذا لم يتم العثور على مواعيد، أضف رسالة
                if (!hasAppointments) {
                appointmentListView.getItems().add("لا توجد مواعيد حالياً."); // رسالة عدم وجود مواعيد
            }
        } catch (SQLException e) {
            e.printStackTrace(); // التعامل مع استثناءات SQL
        }
    }
    @FXML
    void handleDelete() {
        // الحصول على السلسلة المختارة من ListView
        String selectedAppointment = appointmentListView.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            try {
                // تحليل السلسلة بشكل صحيح
                String[] parts = selectedAppointment.split(" - "); // فصل الأجزاء بناءً على " - "
                if (parts.length > 0) {
                    // استخراج ID الموعد من الجزء الأول
                    String idPart = parts[0].trim(); // الجزء الأول هو ID الموعد
                    int appointmentId = Integer.parseInt(idPart); // تحويله إلى عدد صحيح

                    String deleteQuery = "DELETE FROM appointments WHERE id = ?";
                    try (Connection conn = DatabaseConnection.connect();
                         PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

                        pstmt.setInt(1, appointmentId);
                        int affectedRows = pstmt.executeUpdate();

                        if (affectedRows > 0) {
                            // تم الحذف بنجاح، إعادة تحميل المواعيد
                            loadAppointments(LoginController.getLoggedInUsername());
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Appointment deleted successfully.");
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "No appointment found with that ID.");
                            alert.showAndWait();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Database error occurred.");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Could not extract ID from the selected appointment.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid appointment ID format. Please check the selection.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an appointment to delete.");
            alert.showAndWait();
        }
    }


}
