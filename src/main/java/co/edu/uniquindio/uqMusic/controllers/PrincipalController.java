package co.edu.uniquindio.uqMusic.controllers; /**
 * Sample Skeleton for 'menu.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

public class PrincipalController{

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="aleatorio"
    private Button aleatorio; // Value injected by FXMLLoader

    @FXML // fx:id="artistas"
    private Label artistas; // Value injected by FXMLLoader

    @FXML // fx:id="atras"
    private Button atras; // Value injected by FXMLLoader

    @FXML // fx:id="cancion"
    private Button cancion; // Value injected by FXMLLoader

    @FXML // fx:id="home"
    private Button home; // Value injected by FXMLLoader

    @FXML // fx:id="imgCancion"
    private ImageView imgCancion; // Value injected by FXMLLoader

    @FXML // fx:id="isAgregada"
    private Button isAgregada; // Value injected by FXMLLoader

    @FXML // fx:id="newPlaylist"
    private Button newPlaylist; // Value injected by FXMLLoader

    @FXML // fx:id="next"
    private Button next; // Value injected by FXMLLoader

    @FXML // fx:id="nombreCancion"
    private Label nombreCancion; // Value injected by FXMLLoader

    @FXML // fx:id="play"
    private Button play; // Value injected by FXMLLoader

    @FXML // fx:id="search"
    private Button search; // Value injected by FXMLLoader

    @FXML // fx:id="slider"
    private Slider slider; // Value injected by FXMLLoader

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
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

}
