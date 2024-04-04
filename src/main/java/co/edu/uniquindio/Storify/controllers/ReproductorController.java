package co.edu.uniquindio.Storify.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class ReproductorController implements Initializable {

    @FXML
    private WebView webView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine webEngine = webView.getEngine();
        String youtubeURL = "https://www.youtube.com/embed/VIDEO_ID"; // Reemplaza VIDEO_ID con el ID de tu video de YouTube
        webEngine.load(youtubeURL);
    }
}