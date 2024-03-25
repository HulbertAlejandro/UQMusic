package co.edu.uniquindio.uqMusic.app;

import co.edu.uniquindio.uqMusic.model.Storify;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class UQMusicApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(UQMusicApp.class.getResource("/windows/login.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("UQ Music");
        stage.show();
    }
    public static void main(String[] args) {
        Storify.getInstance().inicializar();
        launch(UQMusicApp.class, args);
    }
}
