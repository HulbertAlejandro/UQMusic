package co.edu.uniquindio.Storify.app;

import co.edu.uniquindio.Storify.model.Storify;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * La clase Storfy es la clase principal de la aplicación Storify.
 * Extiende la clase Application de JavaFX y proporciona el método de inicio
 * para iniciar la aplicación.
 */
public class StorifyApp extends Application {

    /**
     * El método start es el punto de entrada de la aplicación JavaFX.
     * Carga la interfaz de usuario desde un archivo FXML y muestra la ventana principal de la aplicación.
     *
     * @param stage El escenario principal de la aplicación.
     * @throws Exception Si ocurre un error al cargar o mostrar la interfaz de usuario.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(StorifyApp.class.getResource("/windows/login.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Storify");
        stage.show();
    }

    /**
     * El método main es el punto de entrada principal de la aplicación.
     * Se invoca cuando se ejecuta la aplicación y se encarga de inicializar la instancia
     * de Storify y lanzar la aplicación JavaFX.
     *
     * @param args Los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        // Inicializar la instancia de Storify
        Storify.getInstance().inicializar();

        // Lanzar la aplicación JavaFX
        launch(StorifyApp.class, args);
    }
}
