package co.edu.uniquindio.Storify.controllers;

import co.edu.uniquindio.Storify.exceptions.CampoObligatorioException;
import co.edu.uniquindio.Storify.exceptions.CampoRepetido;
import co.edu.uniquindio.Storify.exceptions.CampoVacioException;
import co.edu.uniquindio.Storify.model.Storify;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistroController {
    private final Storify storify = Storify.getInstance();
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="back"
    private Button back; // Value injected by FXMLLoader

    @FXML // fx:id="contrasena"
    private TextField contrasena; // Value injected by FXMLLoader

    @FXML // fx:id="correo"
    private TextField correo; // Value injected by FXMLLoader

    @FXML // fx:id="registro"
    private Button registro; // Value injected by FXMLLoader

    @FXML // fx:id="username"
    private TextField username; // Value injected by FXMLLoader

    @FXML
    void back(ActionEvent event) {
        storify.loadStage("/windows/login.fxml", event);
    }

    @FXML
    void registrarse(ActionEvent event) throws CampoRepetido, CampoObligatorioException, CampoVacioException {
        try {
            storify.registrarUsuario(username.getText(), contrasena.getText(), correo.getText());
            username.setText("");
            contrasena.setText("");
            correo.setText("");
        } catch (CampoRepetido e) {
            storify.mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

}
