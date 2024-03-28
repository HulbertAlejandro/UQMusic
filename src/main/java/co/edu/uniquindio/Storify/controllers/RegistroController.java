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

/**
 * Controlador para la ventana de registro de usuarios en la aplicación Storify.
 * Permite que los usuarios nuevos se registren proporcionando un nombre de usuario, contraseña y correo electrónico.
 */
public class RegistroController {
    /** Instancia de la clase Storify para acceder a la lógica de negocio de la aplicación. */
    private final Storify storify = Storify.getInstance();

    /** Recurso de conjunto de mensajes localizado para esta ventana. */
    @FXML
    private ResourceBundle resources;

    /** Ubicación URL del archivo FXML para esta ventana. */
    @FXML
    private URL location;

    /** Botón para volver a la ventana de inicio de sesión. */
    @FXML
    private Button back;

    /** Campo de texto para ingresar el nombre de usuario. */
    @FXML
    private TextField username;

    /** Campo de texto para ingresar la contraseña. */
    @FXML
    private TextField contrasena;

    /** Campo de texto para ingresar el correo electrónico. */
    @FXML
    private TextField correo;

    /**
     * Método que maneja el evento de volver a la ventana de inicio de sesión.
     * Carga la ventana de inicio de sesión para que el usuario pueda iniciar sesión si lo desea.
     * @param event El evento de acción del botón de volver.
     */
    @FXML
    void back(ActionEvent event) {
        storify.loadStage("/windows/login.fxml", event);
    }

    /**
     * Método que maneja el evento de registro de un nuevo usuario.
     * Registra un nuevo usuario en el sistema con los datos proporcionados.
     * @param event El evento de acción del botón de registro.
     * @throws CampoRepetido Si el nombre de usuario ya está en uso por otro usuario registrado.
     * @throws CampoObligatorioException Si falta algún campo obligatorio para completar el registro.
     * @throws CampoVacioException Si algún campo necesario está vacío.
     */
    @FXML
    void registrarse(ActionEvent event) throws CampoRepetido, CampoObligatorioException, CampoVacioException {
        try {
            storify.registrarUsuario(username.getText(), contrasena.getText(), correo.getText());
            // Limpia los campos de texto después de un registro exitoso
            username.setText("");
            contrasena.setText("");
            correo.setText("");
        } catch (CampoRepetido e) {
            storify.mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }
    }
}

