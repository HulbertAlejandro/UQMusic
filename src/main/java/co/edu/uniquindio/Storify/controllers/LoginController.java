package co.edu.uniquindio.Storify.controllers;

import co.edu.uniquindio.Storify.model.Storify;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de inicio de sesión de la aplicación Storify.
 * Gestiona las acciones del usuario, como el inicio de sesión y el registro.
 */
public class LoginController {
    /**
     * Instancia de la clase Storify para acceder a la lógica de negocio de la aplicación.
     */
    private final Storify storify = Storify.getInstance();

    /**
     * Recurso de conjunto de mensajes localizado para esta ventana.
     */
    @FXML
    private ResourceBundle resources;

    /**
     * Ubicación URL del archivo FXML para esta ventana.
     */
    @FXML
    private URL location;

    /**
     * Campo de texto para ingresar el nombre de usuario.
     */
    @FXML
    private TextField username;

    /**
     * Campo de texto para ingresar la contraseña.
     */
    @FXML
    private TextField contrasena;

    /**
     * Botón para iniciar sesión.
     */
    @FXML
    private Button ingresar;

    /**
     * Botón para registrarse como nuevo usuario.
     */
    @FXML
    private Button registro;

    /**
     * Método que maneja el evento de inicio de sesión del usuario.
     * Verifica las credenciales ingresadas y redirige a la ventana correspondiente.
     *
     * @param event El evento de acción del botón de inicio de sesión.
     */
    @FXML
    void ingresar(ActionEvent event) {
        if (username.getText().equals("admin") && contrasena.getText().equals("$aDmiN")) {
            storify.loadStage("/windows/admin.fxml", event);
        } else {
            if (storify.verificarUsuario(username.getText(), contrasena.getText())) {
                storify.loadStage("/windows/menu.fxml", event);
            } else {
                storify.mostrarMensaje(Alert.AlertType.ERROR, "Credenciales inválidas");
            }
        }
    }

    /**
     * Método que maneja el evento de registro del usuario.
     * Carga la ventana de registro para que el usuario pueda crear una nueva cuenta.
     *
     * @param event El evento de acción del botón de registro.
     */
    @FXML
    void registrarse(ActionEvent event) {
        storify.loadStage("/windows/registro.fxml", event);
    }
}
