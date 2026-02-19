package com.mycompany.beautySalonPro;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class employeePagecontroller {
	
	
	
	@FXML
	void mybokkingaction (ActionEvent event)  throws IOException{
		App.setRoot("mybookingEmp");
	}
	
	@FXML
	void myprofileaction (ActionEvent event)  throws IOException{
		App.setRoot("profileEmp");
	}
	
	@FXML
	void logoutaction (ActionEvent event)  throws IOException{
		App.setRoot("login");
	}

}
