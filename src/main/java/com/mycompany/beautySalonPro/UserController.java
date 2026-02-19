package com.mycompany.beautySalonPro;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class UserController {
  
    @FXML
    void pagehairaction(ActionEvent event) throws IOException {
        App.setRoot("booking");
    }
    
    @FXML
    void coloraction(ActionEvent event) throws IOException {
        App.setRoot("coloring");
    }
    
    @FXML
    void stylingaction(ActionEvent event) throws IOException {
        App.setRoot("HairStyling");
    }
    
    @FXML
    void makeupaction(ActionEvent event) throws IOException {
        App.setRoot("makeup");
    }
    
    @FXML
    void nailsaction(ActionEvent event) throws IOException {
        App.setRoot("nails");
    }
    
    @FXML
    void spaaction(ActionEvent event) throws IOException {
        App.setRoot("bodyspa");
    }

    @FXML
    void logoutaction(MouseEvent event) throws IOException {
        App.setRoot("login");
    }
    
    @FXML
    void  profileuseraction(MouseEvent event) throws IOException {
        App.setRoot("profileuser");
    }
    
    @FXML
    void  Myreservationsaction(MouseEvent event) throws IOException {
        App.setRoot("mybooking");
    }
    
    @FXML
    void  homeaction(MouseEvent event) throws IOException {
        App.setRoot("user");
    }
    
    
}
