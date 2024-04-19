package co.edu.uniquindio.Storify.controllers;

import co.edu.uniquindio.Storify.exceptions.CampoObligatorioException;
import co.edu.uniquindio.Storify.exceptions.CampoRepetido;
import co.edu.uniquindio.Storify.exceptions.CampoVacioException;
import co.edu.uniquindio.Storify.model.Storify;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controlador para la ventana de registro de artistas en la aplicación Storify.
 * Permite al administrador agregar nuevos artistas al sistema.
 */
public class RegistroArtistaController {

    /** Campo de texto para ingresar el código del artista. */
    @FXML
    private TextField codigo;

    /** Campo de texto para ingresar el nombre del artista. */
    @FXML
    private TextField nombre;

    /** Campo de texto para ingresar la nacionalidad del artista. */
    @FXML
    private TextField nacionalidad;

    /** CheckBox para indicar si el artista es un grupo musical. */
    @FXML
    private CheckBox esGrupoCheckBox;

    /** ComboBox para seleccionar las canciones asociadas al artista. */
    @FXML
    private ComboBox<String> cancionesComboBox;

    /** Botón para agregar una canción al artista. */
    @FXML
    private Button addCancionButton;

    /** Botón para registrar el artista en el sistema. */
    @FXML
    private Button registro;

    /** Botón para volver a la ventana de administración. */
    @FXML
    private Button back;

    /** Instancia de la clase Storify para acceder a la lógica de negocio de la aplicación. */
    private final Storify storify = Storify.getInstance();

    /**
     * Método que maneja el evento de agregar una canción al artista.
     * Recolecta la canción seleccionada y la agrega al artista en el sistema.
     * @param event El evento de acción del botón de agregar canción.
     */
    @FXML
    void agregarCancion(ActionEvent event) {
        String cancionSeleccionada = cancionesComboBox.getValue();
        // Aquí puedes agregar la lógica para manejar la adición de la canción al artista
        System.out.println("Canción seleccionada: " + cancionSeleccionada);
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
     * Método que maneja el evento de registrar el artista en el sistema.
     * Recolecta la información ingresada por el usuario y registra el artista en el sistema.
     * @param event El evento de acción del botón de registro.
     */
    @FXML
    void registrarse(ActionEvent event) throws CampoVacioException, CampoObligatorioException {
        String codigoArtista = codigo.getText();
        String nombreArtista = nombre.getText();
        String nacionalidadArtista = nacionalidad.getText();
        boolean esGrupo = esGrupoCheckBox.isSelected();

        try {
            storify.registrarArtista(codigoArtista,nombreArtista,nacionalidadArtista,esGrupo);
            storify.listaAutores.add(nombreArtista);
        }catch (CampoVacioException | CampoObligatorioException e){
            storify.mostrarMensaje(Alert.AlertType.ERROR,e.getMessage());
        }
    }
}
