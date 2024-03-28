package co.edu.uniquindio.Storify.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana principal de la aplicación Storify (menu.fxml).
 * Maneja las acciones del usuario en la pantalla principal, como reproducir canciones,
 * navegar entre páginas, agregar canciones a la lista de reproducción, entre otras.
 */
public class PrincipalController {

    /** ResourceBundle proporcionado al FXMLLoader. */
    @FXML
    private ResourceBundle resources;

    /** URL de la ubicación del archivo FXML proporcionado al FXMLLoader. */
    @FXML
    private URL location;

    /** Botón para activar el modo aleatorio de reproducción de canciones. */
    @FXML
    private Button aleatorio;

    /** Etiqueta que muestra el nombre del artista o grupo musical actualmente seleccionado. */
    @FXML
    private Label artistas;

    /** Botón para retroceder a la canción anterior en la lista de reproducción. */
    @FXML
    private Button atras;

    /** Botón para seleccionar una canción y ver sus detalles. */
    @FXML
    private Button cancion;

    /** Botón para volver a la página de inicio. */
    @FXML
    private Button home;

    /** ImageView para mostrar la imagen de la canción actualmente seleccionada. */
    @FXML
    private ImageView imgCancion;

    /** Botón para marcar una canción como agregada a la lista de reproducción del usuario. */
    @FXML
    private Button isAgregada;

    /** Botón para crear una nueva lista de reproducción. */
    @FXML
    private Button newPlaylist;

    /** Botón para avanzar a la siguiente canción en la lista de reproducción. */
    @FXML
    private Button next;

    /** Etiqueta que muestra el nombre de la canción actualmente seleccionada. */
    @FXML
    private Label nombreCancion;

    /** Botón para reproducir o pausar la canción actualmente seleccionada. */
    @FXML
    private Button play;

    /** Botón para buscar canciones dentro de la aplicación. */
    @FXML
    private Button search;

    /** Slider para controlar el volumen de reproducción de la canción. */
    @FXML
    private Slider slider;

    /**
     * Método que maneja el evento de activar el modo aleatorio de reproducción de canciones.
     * @param event El evento de acción del botón de modo aleatorio.
     */
    @FXML
    void aleatorio(ActionEvent event) {
        System.out.println("PRESIONADO");
    }

    @FXML
    void atras(ActionEvent event) {
        System.out.println("PRESIONADO");
    }

    @FXML
    void home(ActionEvent event) {
        System.out.println("PRESIONADO");
    }

    @FXML
    void newPlaylist(ActionEvent event) {
        System.out.println("PRESIONADO");
    }

    @FXML
    void next(ActionEvent event) {
        System.out.println("PRESIONADO");
    }

    @FXML
    void play(ActionEvent event) {
        System.out.println("PRESIONADO");
    }

    @FXML
    void search(ActionEvent event) {
        System.out.println("PRESIONADO");
    }

    public void isAgregada(ActionEvent actionEvent) {
        System.out.println("PRESIONADO");
    }

    public void cancion(ActionEvent actionEvent) {
        System.out.println("PRESIONADO");
    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

}
