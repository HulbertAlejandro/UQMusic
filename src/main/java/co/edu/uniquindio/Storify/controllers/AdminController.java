package co.edu.uniquindio.Storify.controllers;

import co.edu.uniquindio.Storify.model.Autor;
import co.edu.uniquindio.Storify.model.Storify;
import co.edu.uniquindio.Storify.utils.ArchivoUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de administración de la aplicación Storify.
 * Permite al administrador cargar información desde un archivo de texto, crear nuevos artistas y canciones.
 */
public class AdminController {
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

    /** Botón para cargar información desde un archivo. */
    @FXML
    private Button cargarInformacion;

    /** Botón para crear un nuevo artista. */
    @FXML
    private Button crearArtista;

    /** Botón para crear una nueva canción. */
    @FXML
    private Button crearCancion;

    @FXML
    private Button btnEstadisticas;

    @FXML
    public void initialize() {
        storify.llenarAtributos();
    }

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
     * Método que maneja el evento de ver las estadisticas.
     * Carga la ventana de estadisticas para que el administrador pueda ver los generos y los artistas mas escuchados.
     * @param event El evento de acción del botón de ver estadisticas.
     */
    @FXML
    void verEstadisticas(ActionEvent event) {
        //Se cuenta el numero de artistas y de generos
        storify.contarArtista();
        storify.contarGenero();

        // Crear eje X y eje Y para el primer gráfico (Géneros)
        CategoryAxis xAxisGeneros = new CategoryAxis();
        NumberAxis yAxisGeneros = new NumberAxis();

        // Crear el primer gráfico de barras (Géneros)
        BarChart<String, Number> barChartGeneros = new BarChart<>(xAxisGeneros, yAxisGeneros);
        barChartGeneros.setTitle("Estadísticas de Géneros");
        xAxisGeneros.setLabel("Género");
        yAxisGeneros.setLabel("Cantidad");

        // Crear datos de ejemplo para el primer gráfico (Géneros)
        ObservableList<XYChart.Data<String, Number>> dataGeneros = FXCollections.observableArrayList();
        for (int i = 0; i < storify.listaGeneros.size(); i++) {
            dataGeneros.add(new XYChart.Data<>(storify.listaGeneros.get(i), storify.contadoresGenero.get(i)));
        }

        // Crear lista de colores para cada barra del primer gráfico (Géneros)
        String[] colors = {"#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#00FFFF"};

        // Agregar los datos al primer gráfico con colores
        for (int i = 0; i < dataGeneros.size(); i++) {
            XYChart.Data<String, Number> item = dataGeneros.get(i);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(item);
            barChartGeneros.getData().add(series);
            item.getNode().setStyle("-fx-bar-fill: " + colors[i % colors.length] + ";");
        }

        // Crear eje X y eje Y para el segundo gráfico (Artistas)
        CategoryAxis xAxisArtistas = new CategoryAxis();
        NumberAxis yAxisArtistas = new NumberAxis();

        // Crear el segundo gráfico de barras (Artistas)
        BarChart<String, Number> barChartArtistas = new BarChart<>(xAxisArtistas, yAxisArtistas);
        barChartArtistas.setTitle("Estadísticas de Artistas");
        xAxisArtistas.setLabel("Artista");
        yAxisArtistas.setLabel("Cantidad");

        // Crear datos de ejemplo para el segundo gráfico (Artistas)
        ObservableList<XYChart.Data<String, Number>> dataArtistas = FXCollections.observableArrayList();
        for (int i = 0; i < storify.listaAutores.size(); i++) {
            dataArtistas.add(new XYChart.Data<>(storify.listaAutores.get(i), storify.contadoresArtista.get(i)));
        }

        // Crear lista de colores para cada barra del segundo gráfico (Artistas)
        String[] colorsArtistas = {"#FF5733", "#33FF57", "#5733FF", "#FF3357", "#57FF33"};

        // Agregar los datos al segundo gráfico con colores
        for (int i = 0; i < dataArtistas.size(); i++) {
            XYChart.Data<String, Number> item = dataArtistas.get(i);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(item);
            barChartArtistas.getData().add(series);
            item.getNode().setStyle("-fx-bar-fill: " + colorsArtistas[i % colorsArtistas.length] + ";");
        }


        // Crear el contenedor para los gráficos
        VBox vbox = new VBox(barChartGeneros, barChartArtistas);

        // Crear una nueva ventana para mostrar los gráficos
        Stage stage = new Stage();
        stage.setScene(new Scene(vbox, 800, 600));
        stage.setTitle("Estadísticas de Géneros y Artistas");
        stage.show();
    }



    /**
     * Método que maneja el evento de cargar información desde un archivo.
     * Permite al administrador seleccionar un archivo de texto y cargar la información de artistas y canciones desde él.
     * @param event El evento de acción del botón de cargar información.
     */
    @FXML
    void cargarInformacion(ActionEvent event) {
        // Crear un objeto FileChooser para seleccionar el archivo de texto
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
                // Cargar la información de artistas y canciones desde el archivo
                List<Autor> artistas = ArchivoUtils.cargarArtistas(filePath);
                ArchivoUtils.cargarCanciones(filePath, artistas);
                System.out.println("Información cargada exitosamente desde el archivo: " + filePath);
                for(Autor autor : artistas){
                    storify.cargarArtista(autor);
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

    /**
     * Método que maneja el evento de crear un nuevo artista.
     * Carga la ventana de registro de artista para que el administrador pueda registrar un nuevo artista.
     * @param event El evento de acción del botón de crear artista.
     */
    @FXML
    void crearArtista(ActionEvent event) {
        storify.loadStage("/windows/registroArtista.fxml", event);
    }

    /**
     * Método que maneja el evento de crear una nueva canción.
     * Actualmente no implementado.
     * @param event El evento de acción del botón de crear canción.
     */
    @FXML
    void crearCancion(ActionEvent event) {
        storify.loadStage("/windows/registroCancion.fxml", event);
    }
}
