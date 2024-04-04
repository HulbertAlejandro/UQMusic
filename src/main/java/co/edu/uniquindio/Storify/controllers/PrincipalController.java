package co.edu.uniquindio.Storify.controllers;

import co.edu.uniquindio.Storify.model.Autor;
import co.edu.uniquindio.Storify.model.Cancion;
import co.edu.uniquindio.Storify.model.Storify;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import static co.edu.uniquindio.Storify.utils.ArchivoUtils.cargarCanciones;

/**
 * Controlador para la ventana principal de la aplicación Storify (menu.fxml).
 * Maneja las acciones del usuario en la pantalla principal, como reproducir canciones,
 * navegar entre páginas, agregar canciones a la lista de reproducción, entre otras.
 */
public class PrincipalController {

    /**
     * ResourceBundle proporcionado al FXMLLoader.
     */
    @FXML
    private ResourceBundle resources;

    /**
     * URL de la ubicación del archivo FXML proporcionado al FXMLLoader.
     */
    @FXML
    private URL location;

    /**
     * Botón para activar el modo aleatorio de reproducción de canciones.
     */
    @FXML
    private Button aleatorio;

    /**
     * Etiqueta que muestra el nombre del artista o grupo musical actualmente seleccionado.
     */
    @FXML
    private Label artistas;

    /**
     * Botón para retroceder a la canción anterior en la lista de reproducción.
     */
    @FXML
    private Button atras;

    /**
     * Botón para seleccionar una canción y ver sus detalles.
     */
    @FXML
    private Button cancion, cancion1, cancion2, cancion3, cancion4, cancion5, cancion6;

    /**
     * ImageView para mostrar la imagen de la canción actualmente seleccionada.
     */
    @FXML
    private ImageView imgCancion, imgCancion1, imgCancion2, imgCancion3, imgCancion4, imgCancion5, imgCancion6;

    /** Botón para volver a la página de inicio. */
    @FXML
    private Button home;

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
    private Label nombreCancion,titulo1,titulo2,titulo3,titulo4,titulo5,titulo6;

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
    private final Storify storify = Storify.getInstance();
    private ArrayList<Cancion> cancionesSistema = storify.enviarCanciones();
    private ArrayList<Cancion> cancionesBox = new ArrayList<>();
    public void isAgregada(ActionEvent actionEvent) {
        System.out.println("PRESIONADO");
    }

    public void cancion(ActionEvent actionEvent) {
        System.out.println("PRESIONADO");
    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
      cargarArray();
      loadBox();
    }
    private void loadBox(){
        Image image = new Image(cancionesBox.get(0).getCaratula());
        titulo1.setText(cancionesBox.get(0).getNombreCancion());
        imgCancion1.setImage(image);
        Image image1 = new Image(cancionesBox.get(1).getCaratula());
        titulo2.setText(cancionesBox.get(1).getNombreCancion());
        imgCancion2.setImage(image1);
        Image image2 = new Image(cancionesBox.get(2).getCaratula());
        titulo3.setText(cancionesBox.get(2).getNombreCancion());
        imgCancion3.setImage(image2);
        Image image3 = new Image(cancionesBox.get(3).getCaratula());
        titulo4.setText(cancionesBox.get(3).getNombreCancion());
        imgCancion4.setImage(image3);
        Image image4 = new Image(cancionesBox.get(4).getCaratula());
        titulo5.setText(cancionesBox.get(4).getNombreCancion());
        imgCancion5.setImage(image4);
        Image image5 = new Image(cancionesBox.get(5).getCaratula());
        titulo6.setText(cancionesBox.get(5).getNombreCancion());
        imgCancion6.setImage(image5);
    }
    private void cargarArray() {
        Random rand = new Random();
        // Generar 6 números aleatorios dentro del rango del tamaño de la lista
        for (int i = 0; i < 6; i++) {
            int indiceAleatorio = rand.nextInt(cancionesSistema.size());
            cancionesBox.add(cancionesSistema.get(indiceAleatorio));
        }
    }
    public void cancion1(ActionEvent actionEvent) {
        Image imagen = new Image(cancionesBox.get(0).getCaratula());
        imgCancion.setImage(imagen);
        nombreCancion.setText(cancionesBox.get(0).getNombreCancion());
        artistas.setText(cancionesBox.get(0).getArtistas());
    }
    public void cancion2(ActionEvent actionEvent) {
        Image imagen = new Image(cancionesBox.get(1).getCaratula());
        imgCancion.setImage(imagen);
        nombreCancion.setText(cancionesBox.get(1).getNombreCancion());
        artistas.setText(cancionesBox.get(1).getArtistas());
    }
    public void cancion3(ActionEvent actionEvent) {
        Image imagen = new Image(cancionesBox.get(2).getCaratula());
        imgCancion.setImage(imagen);
        nombreCancion.setText(cancionesBox.get(2).getNombreCancion());
        artistas.setText(cancionesBox.get(2).getArtistas());
    }
    public void cancion4(ActionEvent actionEvent) {
        Image imagen = new Image(cancionesBox.get(3).getCaratula());
        imgCancion.setImage(imagen);
        nombreCancion.setText(cancionesBox.get(3).getNombreCancion());
        artistas.setText(cancionesBox.get(3).getArtistas());
    }
    public void cancion5(ActionEvent actionEvent) {
        Image imagen = new Image(cancionesBox.get(4).getCaratula());
        imgCancion.setImage(imagen);
        nombreCancion.setText(cancionesBox.get(4).getNombreCancion());
        artistas.setText(cancionesBox.get(4).getArtistas());
    }
    public void cancion6(ActionEvent actionEvent) {
        Image imagen = new Image(cancionesBox.get(5).getCaratula());
        imgCancion.setImage(imagen);
        nombreCancion.setText(cancionesBox.get(5).getNombreCancion());
        artistas.setText(cancionesBox.get(5).getArtistas());
    }
}
