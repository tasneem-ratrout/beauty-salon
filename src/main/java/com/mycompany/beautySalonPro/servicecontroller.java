package com.mycompany.beautySalonPro;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class servicecontroller {
	@FXML
	void hairaction (ActionEvent event)  throws IOException{
		App.setRoot("hairupdate");
	}
	
	@FXML
	void colraction (ActionEvent event)  throws IOException{
		App.setRoot("colorupdate");
	}
	
	@FXML
	void styleaction (ActionEvent event)  throws IOException{
		App.setRoot("styleupdate");
	}
	@FXML
	void makeuoaction (ActionEvent event)  throws IOException{
		App.setRoot("makeupdate");
	}
	@FXML
	void nailsaction (ActionEvent event)  throws IOException{
		App.setRoot("nailsupdate");
	}
	@FXML
	void spaaction (ActionEvent event)  throws IOException{
		App.setRoot("spaupdate");
	}
	@FXML
	void backaction (MouseEvent event)  throws IOException{
		App.setRoot("dashboard");
	}
	
	

}
