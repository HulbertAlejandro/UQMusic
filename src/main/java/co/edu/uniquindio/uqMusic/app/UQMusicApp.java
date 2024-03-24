package co.edu.uniquindio.uqMusic.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class UQMusicApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(UQMusicApp.class.getResource("/windows/principal.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("UQ Music");
        stage.show();
    }
    public static void main(String[] args) {
        launch(UQMusicApp.class, args);

    }
}
