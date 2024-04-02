package co.edu.uniquindio.Storify.controllers;

import co.edu.uniquindio.Storify.exceptions.CampoObligatorioException;
import co.edu.uniquindio.Storify.exceptions.CampoVacioException;
import co.edu.uniquindio.Storify.model.Autor;
import co.edu.uniquindio.Storify.model.Storify;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Controlador para la ventana de registro de canciones en la aplicación Storify.
 * Permite al administrador agregar nuevas canciones al sistema.
 */
public class RegistroCancionController implements Serializable {
    private final Storify storify = Storify.getInstance();

    /** Campo de texto para el nombre de la canción. */
    @FXML
    private TextField nombreCancion;

    /** Campo de texto para el nombre del álbum de la canción. */
    @FXML
    private TextField nombreAlbum;

    /** Campo de texto para la URL de la carátula del álbum. */
    @FXML
    private TextField caratula;

    /** Campo de texto para el año de lanzamiento de la canción. */
    @FXML
    private TextField anio;

    /** Campo de texto para la duración de la canción en minutos. */
    @FXML
    private TextField duracion;

    /** ChoiceBox para seleccionar el género musical de la canción. */
    @FXML
    private ChoiceBox<String> genero;

    /** Campo de texto para la URL de la canción. */
    @FXML
    private TextField urlCancion,filtroArtista;

    /** Botón para crear la canción en el sistema. */
    @FXML
    private Button crearCancionButton;

    /** Botón para volver a la ventana de administración. */
    @FXML
    private Button back,bttSeleccionar;
    @FXML
    private TableColumn<Autor, String> columnaArtista;
    @FXML
    private TableView<Autor> tablaArtistas;
    private ObservableList<Autor> autores = FXCollections.observableArrayList(storify.enviarAutores());
    private boolean isSelected = false;
    private Autor autorSelected = null;

    /** Instancia de la clase Storify para acceder a la lógica de negocio de la aplicación. */
    /**
     * Método que se llama automáticamente al inicializar el controlador.
     * Llena el ChoiceBox de géneros musicales con opciones predefinidas.
     */
    @FXML
    void initialize() {
        List<String> generos = Arrays.asList("Rock", "Pop", "Punk", "Reggaeton", "Electrónica");
        genero.setItems(FXCollections.observableArrayList(generos));

        columnaArtista.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        tablaArtistas.setItems(autores);

        filtroArtista.textProperty().addListener((observable, oldValue, newValue) ->
                tablaArtistas.setItems(filtrarPorNombre(newValue)));
    }
    private ObservableList<Autor> filtrarPorNombre(String nombre) {
        ObservableList<Autor> artistasFiltrados = FXCollections.observableArrayList();
        for (Autor artista : autores) {
            if (artista.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                artistasFiltrados.add(artista);
            }
        }
        return artistasFiltrados;
    }
    /**
     * Método que maneja el evento de volver a la ventana de administración.
     * Carga la ventana de administración para que el usuario pueda realizar otras acciones.
     * @param event El evento de acción del botón de volver.
     */
    @FXML
    void back(ActionEvent event) {
        storify.loadStage("/windows/admin.fxml", event);
    }

    /**
     * Método que maneja el evento de crear una nueva canción.
     * Recolecta la información ingresada por el usuario y crea una nueva canción en el sistema.
     * @param event El evento de acción del botón de crear canción.
     */
    @FXML
    void crearCancion(ActionEvent event) {
        String nombre = nombreCancion.getText();
        String album = nombreAlbum.getText();
        String urlCaratula = caratula.getText();
        int anioLanzamiento = Integer.parseInt(anio.getText());
        double duracionCancion = Double.parseDouble(duracion.getText());
        String genre = genero.getValue();
        String url = urlCancion.getText();
        // Generar código aleatorio para la canción
        String codigo = generarCodigoAleatorio();

        try {
            storify.guardarCancion(nombre,album,urlCaratula,anioLanzamiento,duracionCancion,genre,url,codigo, autorSelected);
        }catch (CampoVacioException | CampoObligatorioException e ){
            storify.mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    /**
     * Método para generar un código aleatorio de 6 dígitos para la canción.
     * @return El código aleatorio generado como una cadena de texto.
     */
    private String generarCodigoAleatorio() {
        Random random = new Random();
        int codigoAleatorio = random.nextInt(900000) + 100000;
        return String.valueOf(codigoAleatorio);
    }

    public void seleccionarArtista(ActionEvent actionEvent) {
        autorSelected = tablaArtistas.getSelectionModel().getSelectedItem();
    }

}