/**
 * Sample Skeleton for 'menu.fxml' Controller Class
 */

package co.edu.uniquindio.uqMusic.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PrincipalController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="play"
    private Button play; // Value injected by FXMLLoader

    @FXML
    void play(ActionEvent event) {
        System.out.println("PRESIONADO");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert play != null : "fx:id=\"play\" was not injected: check your FXML file 'menu.fxml'.";

    }

}
