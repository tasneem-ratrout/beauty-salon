package com.mycompany.beautySalonPro;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class dashboardcontroller {
	@FXML
	void buttonaction (ActionEvent event)  throws IOException{
		App.setRoot("Appointment ManagementAdmain");
	}
	@FXML
	void serviceaction (ActionEvent event)  throws IOException{
		App.setRoot("Servicemang");
	}
	
	@FXML
	void useraction (ActionEvent event)  throws IOException{
		App.setRoot("usermang");
	}
	@FXML
	void staffaction (ActionEvent event)  throws IOException{
		App.setRoot("Employee InformationAdmain");
	}
	@FXML
	void backuuuu (MouseEvent event)  throws IOException{
		App.setRoot("login");
	}
	
	
	@FXML
	void employeesalaryaction(ActionEvent event) throws IOException {
	    String reportPath = "C:\\Users\\A.Z\\OneDrive\\Desktop\\beautySalonPronn444 (1)\\beautySalonPronn444\\beautySalonPronn\\tasneem.jrxml";
	    try {
	        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

	        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345")) {
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
	            JasperViewer viewer = new JasperViewer(jasperPrint, false);
	            viewer.setVisible(true);
	        } catch (SQLException e) {
	            System.err.println("Database connection error: " + e.getMessage());
	            e.printStackTrace();
	        }

	    } catch (JRException e) {
	        System.err.println("Error compiling or filling the report: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	@FXML
	void customeraction (ActionEvent event)  throws IOException{
		
		
	    String reportPath = "C:\\Users\\A.Z\\OneDrive\\Desktop\\beautySalonPronn444 (1)\\beautySalonPronn444\\beautySalonPronn\\tasneem2.jrxml";
	    try {
	        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

	        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345")) {
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
	            JasperViewer viewer = new JasperViewer(jasperPrint, false);
	            viewer.setVisible(true);
	        } catch (SQLException e) {
	            System.err.println("Database connection error: " + e.getMessage());
	            e.printStackTrace();
	        }

	    } catch (JRException e) {
	        System.err.println("Error compiling or filling the report: " + e.getMessage());
	        e.printStackTrace();
	    }
		
	}

	
	
	
	
	
}
