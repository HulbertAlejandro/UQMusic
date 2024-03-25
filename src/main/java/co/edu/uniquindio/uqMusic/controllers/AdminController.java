package co.edu.uniquindio.uqMusic.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.uqMusic.model.Storify;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
public class AdminController {
    private final Storify storify = Storify.getInstance();
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="back"
    private Button back; // Value injected by FXMLLoader

    @FXML // fx:id="cargarInformacion"
    private Button cargarInformacion; // Value injected by FXMLLoader

    @FXML // fx:id="crearArtista"
    private Button crearArtista; // Value injected by FXMLLoader

    @FXML // fx:id="crearCancion"
    private Button crearCancion; // Value injected by FXMLLoader

    @FXML
    void back(ActionEvent event) {
        storify.loadStage("/windows/login.fxml", event);
    }

    @FXML
    void cargarInformacion(ActionEvent event) {

    }

    @FXML
    void crearArtista(ActionEvent event) {

    }

    @FXML
    void crearCancion(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }
}
