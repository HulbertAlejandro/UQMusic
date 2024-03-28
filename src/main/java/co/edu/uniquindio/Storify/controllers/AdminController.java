package co.edu.uniquindio.Storify.controllers;

import co.edu.uniquindio.Storify.model.Autor;
import co.edu.uniquindio.Storify.model.Storify;
import co.edu.uniquindio.Storify.utils.ArchivoUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de información");

        // Filtrar archivos por extensión si es necesario
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de texto", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Mostrar el diálogo de selección de archivo
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            try {
                List<Autor> artistas = ArchivoUtils.cargarArtistas(filePath);
                ArchivoUtils.cargarCanciones(filePath, artistas);
                System.out.println("Información cargada exitosamente desde el archivo: " + filePath);
                // Mostrar la lista de artistas en la consola
                System.out.println("Lista de artistas cargada:");
                for (Autor artista : artistas) {
                    System.out.println(artista); // Imprime la información del artista

                    artista.getListaCanciones().imprimirLista(); // Imprime la lista de canciones del artista
                }

            } catch (IOException e) {
                // Manejar errores al cargar el archivo
                e.printStackTrace();
                System.err.println("Error al cargar la información desde el archivo: " + e.getMessage());
                // Mostrar una alerta al usuario
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error al cargar la información desde el archivo");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            // El usuario canceló la selección del archivo
            System.out.println("Selección de archivo cancelada");
        }
    }


    @FXML
    void crearArtista(ActionEvent event) {

    }

    @FXML
    void crearCancion(ActionEvent event) {

    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }
}
