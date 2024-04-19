package co.edu.uniquindio.Storify.controllers;

import co.edu.uniquindio.Storify.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.web.WebView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class PrincipalController {

    @FXML
    private TableView<Cancion> tablaCanciones;
    @FXML
    private TextField buscador;
    @FXML
    private RadioButton bttO, bttArtista, bttY;
    @FXML
    private TableColumn<Cancion, String> columnaCaratula;
    @FXML
    private TableColumn<Cancion, String> columnaNombreCancion;
    @FXML
    private TableColumn<Cancion, String> columnaNombreAlbum;
    @FXML
    private TableColumn<Cancion, String> columnaArtistas;
    @FXML
    private TableColumn<Cancion, Double> columnaDuracion;
    @FXML
    private ImageView imagenView, imgLike, imgAleatorio;
    @FXML
    private Label labelCancion, labelArtista;
    private boolean stateAletorio = false;
    private boolean stateLike;
    private int indiceTabla;
    private ToggleGroup toggleGroup;
    private final Storify storify = Storify.getInstance();
    private ArrayList<Cancion> cancionesSistema = storify.enviarCanciones();
    private ArrayList<Cancion> cancionesUsuario = storify.enviarUsuario().getCanciones().toArrayList();
    private Usuario usuario = storify.enviarUsuario();
    @FXML
    public void initialize() {
        toggleGroup = new ToggleGroup();

        // Asigna el grupo de toogle a los RadioButtons
        bttO.setToggleGroup(toggleGroup);
        bttArtista.setToggleGroup(toggleGroup);
        bttY.setToggleGroup(toggleGroup);

        // Configura un listener para los eventos de selección
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                // Si no hay RadioButton seleccionado, selecciona el anterior
                oldValue.setSelected(true);
            }
        });

        // Configurar la columna de carátula para mostrar imágenes
        columnaCaratula.setCellValueFactory(new PropertyValueFactory<>("caratula"));
        columnaCaratula.setCellFactory(param -> new javafx.scene.control.TableCell<>() {
        private final ImageView imageView = new ImageView();

        @Override
        protected void updateItem(String caratulaPath, boolean empty) {
           super.updateItem(caratulaPath, empty);
           if (empty || caratulaPath == null) {
               setGraphic(null);
           } else {
               try {
                   Image image = new Image(caratulaPath);
                   imageView.setImage(image);
                   imageView.setFitWidth(50); // Tamaño de la imagen
                   imageView.setPreserveRatio(true);
                   setGraphic(imageView);
               } catch (Exception e) {
                        System.out.println("Error al cargar la imagen: " + caratulaPath);
                        System.out.println("Excepción: " + e.getMessage());
                        e.printStackTrace();
                        setGraphic(null); // Mostrar celda vacía si no se puede cargar la imagen
               }
           }
        }
    });

        // Configurar otras columnas para mostrar propiedades de la canción
        columnaNombreCancion.setCellValueFactory(new PropertyValueFactory<>("nombreCancion"));
        columnaNombreAlbum.setCellValueFactory(new PropertyValueFactory<>("nombreAlbum"));
        columnaArtistas.setCellValueFactory(new PropertyValueFactory<>("artistas"));
        columnaDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        // Agregar listener para cambios de selección en la tabla
        tablaCanciones.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Obtener la canción seleccionada
                Cancion cancionSeleccionada = newValue;

                // Actualizar la ImageView con la carátula de la canción
                if (cancionSeleccionada.getCaratula() != null) {
                    Image caratulaImage = new Image(cancionSeleccionada.getCaratula());
                    imagenView.setImage(caratulaImage);
                } else {
                    imagenView.setImage(null); // Limpiar la ImageView si no hay carátula
                }

                // Actualizar el Label con el nombre de la canción y los artistas
                String nombreCancion = cancionSeleccionada.getNombreCancion();
                String artistas = cancionSeleccionada.getArtistas();
                labelCancion.setText(nombreCancion);
                labelArtista.setText(artistas);
                indiceTabla = tablaCanciones.getSelectionModel().getSelectedIndex();
                if(cancionesUsuario.contains(cancionSeleccionada)){
                    Image image = new Image("/imagenes/check.png");
                    imgLike.setImage(image);
                    stateLike = true;
                }else{
                    Image image = new Image("/imagenes/checkOFF.png");
                    imgLike.setImage(image);
                    stateLike = false;
                }
            } else {
                // Limpiar la ImageView y el Label si no hay canción seleccionada
                imagenView.setImage(null);
                labelCancion.setText("");
                labelArtista.setText("");
            }
        });

        // Asignar las canciones al TableView
        tablaCanciones.getItems().addAll(cancionesSistema);
    }
    @FXML
    void play() {
        Cancion cancionSeleccionada = tablaCanciones.getSelectionModel().getSelectedItem();
        if (cancionSeleccionada != null) {
            String youtubeEmbed = convertToEmbedUrl(cancionSeleccionada.getUrl());
            System.out.println(youtubeEmbed);

            try {
                Stage stage = new Stage();
                WebView webView = new WebView();
                webView.getEngine().load(youtubeEmbed);

                stage.setScene(new javafx.scene.Scene(webView, 640, 390));
                stage.show();
            } catch (Exception e) {
                System.out.println("Error al reproducir la canción: " + e.getMessage());
            }
        }
    }
    void playBtt(int ind) {
        Cancion cancionSeleccionada = tablaCanciones.getItems().get(ind);
        if (cancionSeleccionada != null) {
            String youtubeEmbed = convertToEmbedUrl(cancionSeleccionada.getUrl());
            System.out.println(youtubeEmbed);

            try {
                Stage stage = new Stage();
                WebView webView = new WebView();
                webView.getEngine().load(youtubeEmbed);

                stage.setScene(new javafx.scene.Scene(webView, 640, 390));
                stage.show();
            } catch (Exception e) {
                System.out.println("Error al reproducir la canción: " + e.getMessage());
            }
        }
    }
    public static String convertToEmbedUrl(String youtubeUrl) {
        // Verifica si la URL es válida
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0) {
            // Verifica si la URL contiene el formato de un enlace de YouTube
            if (youtubeUrl.contains("youtube.com") || youtubeUrl.contains("youtu.be")) {
                // Extrae el ID del vídeo de la URL
                String videoId = extractVideoId(youtubeUrl);
                // Crea el enlace embebido usando el ID del vídeo
                return "https://www.youtube.com/embed/" + videoId;
            }
        }
        return null;
    }

    // Método auxiliar para extraer el ID del vídeo de la URL
    private static String extractVideoId(String youtubeUrl) {
        String videoId = null;
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0) {
            String[] splitUrl = youtubeUrl.split("(vi/|v=|/v/|youtu.be/|/embed/)");
            if (splitUrl.length > 1) {
                videoId = splitUrl[1].split("[^0-9a-zA-Z_-]")[0];
            }
        }
        return videoId;
    }

    // Método para buscar por artistas
    private ObservableList<Cancion> buscarPorArtistas(String nombre) {
        if (nombre.isBlank() || nombre.isEmpty()) {
            return FXCollections.observableArrayList(cancionesSistema);
        }
        else {
            ObservableList<Cancion> artistasFiltrados = FXCollections.observableArrayList();
            for (Autor artista : storify.enviarAutores()) {
                if (artista.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                    artistasFiltrados.addAll(artista.getListaCanciones().toArrayList());
                }
            }
            return artistasFiltrados;
        }
    }

    // Método para buscar por O
    private ObservableList<Cancion> buscarPorO(String atributo) {

        ObservableList<Cancion> cancionesCoincidentes = FXCollections.observableArrayList();

        if (atributo == null || atributo.isEmpty()) {
            return FXCollections.observableArrayList(cancionesSistema);
        }

        String[] atributos = atributo.split(",");

        if (atributos.length == 0) {
            return FXCollections.observableArrayList(cancionesSistema);
        }

        ArbolBinario arbolAutores = storify.enviarArtistas();
        ArrayList<Cancion> cancionesIzquierda = new ArrayList<>();
        ArrayList<Cancion> cancionesDerecha = new ArrayList<>();

        Thread hiloIzquierda = new Thread(() -> cancionesIzquierda.addAll(recorrerIzquierda(arbolAutores.getInicio(), new ArrayList<>(), atributos)));
        Thread hiloDerecha = new Thread(() -> cancionesDerecha.addAll(recorrerDerecha(arbolAutores.getInicio(), new ArrayList<>(), atributos)));

        hiloIzquierda.start();
        hiloDerecha.start();

        try {
            hiloIzquierda.join();
            hiloDerecha.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cancionesCoincidentes.addAll(cancionesIzquierda);
        cancionesCoincidentes.addAll(cancionesDerecha);

        HashSet<Cancion> cancionesUnicas = new HashSet<>(cancionesCoincidentes);

        cancionesCoincidentes.clear();
        cancionesCoincidentes.addAll(cancionesUnicas);

        return cancionesCoincidentes;
    }

    private ArrayList<Cancion> recorrerIzquierda(NodoArbol inicio, ArrayList<Cancion> cancions, String[] atributos) {
        if(inicio == null){
            return cancions;
        }
        ArrayList<Cancion> canciones = inicio.getAutor().getListaCanciones().toArrayList();
        for(String atributo : atributos){
            for(Cancion cancion : canciones){
                if(cancion.coincideAtributo(atributo)){
                    cancions.add(cancion);
                }
            }
        }
        recorrerIzquierda(inicio.getNodoIzquierda(), cancions,atributos);
        recorrerIzquierda(inicio.getNodoDerecha(), cancions,atributos);
        return cancions;
    }

    private ArrayList<Cancion> recorrerDerecha(NodoArbol inicio, ArrayList<Cancion> cancions, String[] atributos) {
        if(inicio == null){
            return cancions;
        }
        ArrayList<Cancion> canciones = inicio.getAutor().getListaCanciones().toArrayList();
        for(String atributo : atributos){
            for(Cancion cancion : canciones){
                if(cancion.coincideAtributo(atributo)){
                    cancions.add(cancion);
                }
            }
        }
        recorrerDerecha(inicio.getNodoIzquierda(), cancions,atributos);
        recorrerDerecha(inicio.getNodoDerecha(), cancions,atributos);
        return cancions;
    }

    // Método para buscar por Y
    private ObservableList<Cancion> buscarPorY(String atributo) {

        ObservableList<Cancion> cancionesCoincidentes = FXCollections.observableArrayList();

        if (atributo == null || atributo.isEmpty()) {
            return FXCollections.observableArrayList(cancionesSistema);
        }
        String[] atributos = atributo.split(",");

        if (atributos.length == 0) {
            return FXCollections.observableArrayList(cancionesSistema);
        }

        ArbolBinario arbolAutores = storify.enviarArtistas();

        ArrayList<Cancion> cancionesIzquierda = new ArrayList<>();
        ArrayList<Cancion> cancionesDerecha = new ArrayList<>();

        Thread hiloIzquierda = new Thread(() -> cancionesIzquierda.addAll(recorrerIzquierdaCompleto(arbolAutores.getInicio(), new ArrayList<>(), atributos)));
        Thread hiloDerecha = new Thread(() -> cancionesDerecha.addAll(recorrerDerechaCompleto(arbolAutores.getInicio(), new ArrayList<>(), atributos)));

        hiloIzquierda.start();
        hiloDerecha.start();

        try {
            hiloIzquierda.join();
            hiloDerecha.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cancionesCoincidentes.addAll(cancionesIzquierda);
        cancionesCoincidentes.addAll(cancionesDerecha);

        HashSet<Cancion> cancionesUnicas = new HashSet<>(cancionesCoincidentes);

        cancionesCoincidentes.clear();
        cancionesCoincidentes.addAll(cancionesUnicas);

        return cancionesCoincidentes;
    }
    private ArrayList<Cancion> recorrerIzquierdaCompleto(NodoArbol inicio, ArrayList<Cancion> cancions, String[] atributos) {
        if(inicio == null){
            return cancions;
        }
        ArrayList<Cancion> canciones = inicio.getAutor().getListaCanciones().toArrayList();
        for (Cancion cancion : canciones) {
            int atributosCoincidentes = 0;
            for (String atr : atributos) {
                if (cancion.coincideAtributo(atr)) {
                    atributosCoincidentes+=1;
                }
            }
            if (atributosCoincidentes == atributos.length) {
                cancions.add(cancion);
            }
        }
        recorrerIzquierdaCompleto(inicio.getNodoIzquierda(), cancions,atributos);
        recorrerIzquierdaCompleto(inicio.getNodoDerecha(), cancions,atributos);
        return cancions;
    }

    private ArrayList<Cancion> recorrerDerechaCompleto(NodoArbol inicio, ArrayList<Cancion> cancions, String[] atributos) {
        if(inicio == null){
            return cancions;
        }
        ArrayList<Cancion> canciones = inicio.getAutor().getListaCanciones().toArrayList();
        for (Cancion cancion : canciones) {
            int atributosCoincidentes = 0;
            for (String atr : atributos) {
                if (cancion.coincideAtributo(atr)) {
                    atributosCoincidentes+=1;
                }
            }
            if (atributosCoincidentes == atributos.length) {
                cancions.add(cancion);
            }
        }
        recorrerDerechaCompleto(inicio.getNodoIzquierda(), cancions,atributos);
        recorrerDerechaCompleto(inicio.getNodoDerecha(), cancions,atributos);
        return cancions;
    }
    public void buscar (ActionEvent actionEvent) {
        if (bttArtista.isSelected()){
            System.out.println("BUSQUEDA POR ARTISTA");
            buscador.textProperty().addListener((observable, oldValue, newValue) ->
                    tablaCanciones.setItems(buscarPorArtistas(newValue)));
        }
        if (bttO.isSelected()){
            System.out.println("BUSQUEDA POR O");
            buscador.textProperty().addListener((observable, oldValue, newValue) ->
                    tablaCanciones.setItems(buscarPorO(newValue)));
        }
        if (bttY.isSelected()){
            System.out.println("BUSQUEDA POR Y");
            buscador.textProperty().addListener((observable, oldValue, newValue) ->
                    tablaCanciones.setItems(buscarPorY(newValue)));
        }
    }

    public void exit(ActionEvent actionEvent) {
        storify.loadStage("/windows/login.fxml", actionEvent);
    }

    public void home(ActionEvent actionEvent) {
        storify.loadStage("/windows/menu.fxml", actionEvent);
    }

    public void likes(ActionEvent actionEvent) {
        storify.loadStage("/windows/likes.fxml",actionEvent);
    }

    public void back(ActionEvent actionEvent) {
        if(indiceTabla!= -1){
            if(stateAletorio){
                Random random = new Random();
                int tamañoTabla = tablaCanciones.getItems().size();
                int indiceAleatorio = random.nextInt(tamañoTabla);

                String nombreCancion = tablaCanciones.getItems().get(indiceAleatorio).getNombreCancion();
                String artistas = tablaCanciones.getItems().get(indiceAleatorio).getArtistas();
                Image caratula = new Image(tablaCanciones.getItems().get(indiceAleatorio).getCaratula());
                imagenView.setImage(caratula);
                indiceTabla = indiceAleatorio;

                if(storify.verificarContencion(cancionesUsuario,tablaCanciones.getItems().get(indiceTabla).getCodigo())){
                    Image image = new Image("/imagenes/check.png");
                    imgLike.setImage(image);
                    stateLike = true;
                }else{
                    Image image = new Image("/imagenes/checkOFF.png");
                    imgLike.setImage(image);
                    stateLike = false;
                }

                labelCancion.setText(nombreCancion);
                labelArtista.setText(artistas);

                playBtt(indiceAleatorio);
            }else{
                if(indiceTabla == 0){

                    String nombreCancion = tablaCanciones.getItems().get(tablaCanciones.getItems().size()-1).getNombreCancion();
                    String artistas = tablaCanciones.getItems().get(tablaCanciones.getItems().size()-1).getArtistas();

                    Image caratula = new Image(tablaCanciones.getItems().get(tablaCanciones.getItems().size()-1).getCaratula());
                    imagenView.setImage(caratula);

                    indiceTabla = tablaCanciones.getItems().size()-1;

                    if(storify.verificarContencion(cancionesUsuario,tablaCanciones.getItems().get(indiceTabla).getCodigo())){
                        Image image = new Image("/imagenes/check.png");
                        imgLike.setImage(image);
                        stateLike = true;
                    }else{
                        Image image = new Image("/imagenes/checkOFF.png");
                        imgLike.setImage(image);
                        stateLike = false;
                    }
                    labelCancion.setText(nombreCancion);
                    labelArtista.setText(artistas);

                    playBtt(indiceTabla);
                }
                else {

                    indiceTabla = indiceTabla-1;

                    String nombreCancion = tablaCanciones.getItems().get(indiceTabla).getNombreCancion();
                    String artistas = tablaCanciones.getItems().get(indiceTabla).getArtistas();
                    Image caratula = new Image(tablaCanciones.getItems().get(indiceTabla).getCaratula());
                    imagenView.setImage(caratula);

                    if(storify.verificarContencion(cancionesUsuario,tablaCanciones.getItems().get(indiceTabla).getCodigo())){
                        Image image = new Image("/imagenes/check.png");
                        imgLike.setImage(image);
                        stateLike = true;
                    }else{
                        Image image = new Image("/imagenes/checkOFF.png");
                        imgLike.setImage(image);
                        stateLike = false;
                    }

                    labelCancion.setText(nombreCancion);
                    labelArtista.setText(artistas);

                    playBtt(indiceTabla);
                }
            }
        }
    }

    public void next(ActionEvent actionEvent) {
        if(indiceTabla!= -1){
            if(stateAletorio){
                Random random = new Random();
                int tamañoTabla = tablaCanciones.getItems().size()-1;
                int indiceAleatorio = random.nextInt(tamañoTabla);

                indiceTabla = indiceAleatorio;

                Image caratula = new Image(tablaCanciones.getItems().get(indiceTabla).getCaratula());
                imagenView.setImage(caratula);

                if(storify.verificarContencion(cancionesUsuario,tablaCanciones.getItems().get(indiceAleatorio).getCodigo())){
                    Image image = new Image("/imagenes/check.png");
                    imgLike.setImage(image);
                    stateLike = true;
                }else{
                    Image image = new Image("/imagenes/checkOFF.png");
                    imgLike.setImage(image);
                    stateLike = false;
                }

                String nombreCancion = tablaCanciones.getItems().get(indiceAleatorio).getNombreCancion();
                String artistas = tablaCanciones.getItems().get(indiceAleatorio).getArtistas();

                labelCancion.setText(nombreCancion);
                labelArtista.setText(artistas);

                playBtt(indiceAleatorio);
            }else{
                if(indiceTabla == tablaCanciones.getItems().size()-1){

                    indiceTabla = 0;

                    if(storify.verificarContencion(cancionesUsuario,tablaCanciones.getItems().get(0).getCodigo())){
                        Image image = new Image("/imagenes/check.png");
                        imgLike.setImage(image);
                        stateLike = true;
                    }else{
                        Image image = new Image("/imagenes/checkOFF.png");
                        imgLike.setImage(image);
                        stateLike = false;
                    }

                    String nombreCancion = tablaCanciones.getItems().get(0).getNombreCancion();
                    String artistas = tablaCanciones.getItems().get(0).getArtistas();

                    Image caratula = new Image(tablaCanciones.getItems().get(indiceTabla).getCaratula());
                    imagenView.setImage(caratula);

                    labelCancion.setText(nombreCancion);
                    labelArtista.setText(artistas);
                    playBtt(0);
                } else {

                    indiceTabla = indiceTabla + 1;
                    if(storify.verificarContencion(cancionesUsuario,tablaCanciones.getItems().get(indiceTabla).getCodigo())){
                        Image image = new Image("/imagenes/check.png");
                        imgLike.setImage(image);
                        stateLike = true;
                    }else{
                        Image image = new Image("/imagenes/checkOFF.png");
                        imgLike.setImage(image);
                        stateLike = false;
                    }

                    String nombreCancion = tablaCanciones.getItems().get(indiceTabla).getNombreCancion();
                    String artistas = tablaCanciones.getItems().get(indiceTabla).getArtistas();
                    Image caratula = new Image(tablaCanciones.getItems().get(indiceTabla).getCaratula());
                    imagenView.setImage(caratula);
                    labelCancion.setText(nombreCancion);
                    labelArtista.setText(artistas);

                    playBtt(indiceTabla);
                }
            }
        }
    }

    public void like(ActionEvent actionEvent) {
        if(!stateLike){
            usuario.getCanciones().agregar(tablaCanciones.getItems().get(indiceTabla));
            storify.serializar();
            Image image = new Image("/imagenes/check.png");
            imgLike.setImage(image);
            stateLike = true;
        }else{
            usuario.getCanciones().eliminar(tablaCanciones.getItems().get(indiceTabla));
            storify.serializar();
            Image image = new Image("/imagenes/checkOFF.png");
            imgLike.setImage(image);
            stateLike = false;
        }
    }

    public void aleatorio(ActionEvent actionEvent) {
        if(stateAletorio){
            stateAletorio = false;
            Image image = new Image("/imagenes/aleatorioOFF.png");
            imgAleatorio.setImage(image);
        }else {
            Image image = new Image("/imagenes/aleatorioON.png");
            imgAleatorio.setImage(image);
            stateAletorio = true;
        }
    }
}
