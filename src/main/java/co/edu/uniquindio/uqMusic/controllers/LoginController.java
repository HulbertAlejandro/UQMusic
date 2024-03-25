package co.edu.uniquindio.uqMusic.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.uqMusic.model.Storify;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
public class LoginController {
    private final Storify storify = Storify.getInstance();
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="contrasena"
    private TextField contrasena; // Value injected by FXMLLoader

    @FXML // fx:id="ingresar"
    private Button ingresar; // Value injected by FXMLLoader

    @FXML // fx:id="registro"
    private Button registro; // Value injected by FXMLLoader

    @FXML // fx:id="username"
    private TextField username; // Value injected by FXMLLoader

    @FXML
    void ingresar(ActionEvent event) {
        if(username.getText().equals("admin") && contrasena.getText().equals("admin")){
            storify.loadStage("/windows/admin.fxml", event);
        }else{
            if(storify.verificarUsuario(username.getText(),contrasena.getText())){
                storify.loadStage("/windows/menu.fxml", event);
            }else{
                storify.mostrarMensaje(Alert.AlertType.ERROR, "Credenciales invalidas");
            }
        }
    }
    @FXML
    void registrarse(ActionEvent event) {
        storify.loadStage("/windows/registro.fxml", event);
    }
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

}
