package co.edu.uniquindio.Storify.controllers;

import co.edu.uniquindio.Storify.model.Autor;
import co.edu.uniquindio.Storify.model.Cancion;
import co.edu.uniquindio.Storify.model.Storify;
import co.edu.uniquindio.Storify.model.Usuario;
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
import java.util.Comparator;
import java.util.Random;

public class LikesController {

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
    private ImageView imagenView, imgLike, imgAleatorio, imgDeshacer, imgRehacer;
    @FXML
    private Label labelCancion, labelArtista;

    @FXML
    public ComboBox<String> comboBoxOrdenamiento;

    private boolean stateAletorio = false;

    private boolean stateEliminada;
    private boolean stateInsertada;
    private boolean stateLike;
    private int indiceTabla;
    private ToggleGroup toggleGroup;
    private final Storify storify = Storify.getInstance();
    private ArrayList<Cancion> cancionesSistema = storify.enviarUsuario().getCanciones().toArrayList();
    private ArrayList<Cancion> cancionesUsuario = storify.enviarUsuario().getCanciones().toArrayList();
    private Usuario usuario = storify.enviarUsuario();

    ObservableList<String> ordenamientoList =
            FXCollections.observableArrayList("Título", "Artista", "Año", "Álbum", "Género", "Duración");

    @FXML
    public void initialize() {
        // Configurar la columna de carátula para mostrar imágenes
        columnaCaratula.setCellValueFactory(new PropertyValueFactory<>("caratula"));

        //Inicializar el ComboBox con sus valores y sus tipos de ordenamiento para el usuario
        llenarCombo(comboBoxOrdenamiento, ordenamientoList);
        comboBoxOrdenamiento.setOnAction(this::ordenarLikes);

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
        buscador.textProperty().addListener((observable, oldValue, newValue) ->
                tablaCanciones.setItems(buscarPorO(newValue)));

    }

    /**
     * Llena un ComboBox con los elementos de una lista observable.
     *
     * @param comboBoxOrdenamiento El ComboBox que se llenará con los elementos.
     * @param ordenamientoList La lista observable que contiene los elementos a agregar al ComboBox.
     */

    private void llenarCombo(ComboBox<String> comboBoxOrdenamiento, ObservableList<String> ordenamientoList) {
        comboBoxOrdenamiento.getItems().addAll(ordenamientoList);
    }

    /**
     * Reproduce la canción seleccionada en la tabla de canciones.
     * Utiliza la URL de la canción para cargar un enlace embebido de YouTube en una ventana nueva.
     * En caso de no validarse, salta una exception.
     */

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

    /**
     * Reproduce una canción seleccionada de una tabla de canciones.
     *
     * @param ind El índice de la canción seleccionada en la tabla.
     */

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

    /**
     * Convierte una URL de YouTube en un enlace embebido válido.
     * Si la URL es válida y corresponde a un enlace de YouTube, se extrae el ID del vídeo y se crea el enlace embebido.
     *
     * @param youtubeUrl La URL de YouTube que se va a convertir.
     * @return El enlace embebido correspondiente al vídeo de YouTube, o null si la URL no es válida o no es un enlace de YouTube.
     */
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

    /**
     * Extrae el ID de vídeo de una URL de YouTube.
     *
     * @param youtubeUrl La URL de YouTube de la que se extraerá el ID de vídeo.
     * @return El ID de vídeo extraído de la URL, o null si la URL no es válida o no contiene un ID de vídeo.
     */
    private static String extractVideoId(String youtubeUrl) {
        String videoId = null;
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0) {
            // Divide la URL usando expresiones regulares para extraer el ID del vídeo
            String[] splitUrl = youtubeUrl.split("(vi/|v=|/v/|youtu.be/|/embed/)");
            if (splitUrl.length > 1) {
                // Si se encuentra un ID válido, lo extrae
                videoId = splitUrl[1].split("[^0-9a-zA-Z_-]")[0];
            }
        }
        return videoId; // Devuelve el ID de vídeo extraído de la URL, o null si no se puede extraer
    }
    // Método para buscar por artistas
//    private ObservableList<Cancion> buscarPorArtistas(String nombre) {
//        ObservableList<Cancion> artistasFiltrados = FXCollections.observableArrayList();
//        for (Autor artista : storify.enviarAutores()) {
//            if (artista.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
//                artistasFiltrados.addAll(artista.getListaCanciones().toArrayList());
//            }
//        }
//        return artistasFiltrados;
//    }

    /**
     * Busca canciones en base a un atributo dado.
     * Si el atributo está vacío o en blanco, devuelve todas las canciones del sistema.
     * Si el atributo contiene uno o más valores separados por comas, busca canciones que coincidan con al menos uno de esos valores en sus atributos.
     *
     * @param atributo El atributo por el cual se realizará la búsqueda.
     * @return Una lista observable de canciones que coinciden con el atributo especificado, o todas las canciones del sistema si el atributo está vacío o en blanco.
     */
    private ObservableList<Cancion> buscarPorO(String atributo) {
        // Verifica si el atributo está vacío o en blanco
        if(atributo.isEmpty() || atributo.isBlank()){
            // Devuelve todas las canciones del sistema si el atributo está vacío o en blanco
            return FXCollections.observableArrayList(cancionesSistema);
        }else{
            String [] atributos = atributo.split(",");
            ObservableList<Cancion> cancionesCoincidentes = FXCollections.observableArrayList();
            // Verifica si la lista de atributos está vacía
            if (atributos.length == 0) {
                // Devuelve todas las canciones del sistema si la lista de atributos está vacía
                return FXCollections.observableArrayList(cancionesSistema);
            }
            // Itera sobre todas las canciones del sistema
            for (Cancion cancion : cancionesSistema) {
                // Itera sobre todos los atributos proporcionados
                for (String atr : atributos) {
                    // Si la canción coincide con alguno de los atributos, se agrega a la lista de coincidencias
                    if (cancion.coincideAtributo(atr)) {
                        cancionesCoincidentes.add(cancion);
                    }
                }
            }
            // Devuelve la lista de canciones que coinciden con los atributos especificados
            return cancionesCoincidentes;
        }

    }

    // Método para buscar por Y
//    private ObservableList<Cancion> buscarPorY(String[] atributos) {
//        ObservableList<Cancion> cancionesCoincidentes = FXCollections.observableArrayList();
//        if (atributos.length == 0) {
//            return FXCollections.observableArrayList(cancionesSistema);
//        }
//        for (Cancion cancion : cancionesSistema) {
//            int atributosCoincidentes = 0;
//            for (String atributo : atributos) {
//                if (cancion.coincideAtributo(atributo)) {
//                    atributosCoincidentes+=1;
//                }
//            }
//            if (atributosCoincidentes == atributos.length) {
//                cancionesCoincidentes.add(cancion);
//            }
//        }
//        return cancionesCoincidentes;
//    }

    /**
     * Cierra la aplicación y carga la ventana de inicio de sesión.
     *
     * @param actionEvent El evento que desencadenó la acción de salir.
     */
    public void exit(ActionEvent actionEvent) {
        storify.loadStage("/windows/login.fxml", actionEvent);
    }

    /**
     * Carga la ventana de menú principal.
     *
     * @param actionEvent El evento que desencadenó la acción de volver a la página de inicio.
     */
    public void home(ActionEvent actionEvent) {
        storify.loadStage("/windows/menu.fxml", actionEvent);
    }

    /**
     * Carga la ventana de likes.
     *
     * @param actionEvent El evento que desencadenó la acción de ir a la página de Likes.
     */
    public void likes(ActionEvent actionEvent) {
        storify.loadStage("/windows/likes.fxml",actionEvent);
    }

    /**
     * Retrocede a la canción anterior en la tabla de canciones y actualiza la interfaz gráfica.
     * Si se está reproduciendo en modo aleatorio, selecciona una canción aleatoria.
     * Si se está reproduciendo en modo secuencial, retrocede a la canción anterior en la tabla.
     *
     * @param actionEvent El evento que desencadenó la acción de retroceder.
     */
    public void back(ActionEvent actionEvent) {
        if(indiceTabla!= -1){
            if(stateAletorio){
                // Si está en modo aleatorio, elige una canción aleatoria
                Random random = new Random();
                int tamañoTabla = tablaCanciones.getItems().size();
                int indiceAleatorio = random.nextInt(tamañoTabla);

                String nombreCancion = tablaCanciones.getItems().get(indiceAleatorio).getNombreCancion();
                String artistas = tablaCanciones.getItems().get(indiceAleatorio).getArtistas();
                Image caratula = new Image(tablaCanciones.getItems().get(indiceAleatorio).getCaratula());
                imagenView.setImage(caratula);
                indiceTabla = indiceAleatorio;

                // Verifica si la canción está marcada como favorita y actualiza la interfaz gráfica en consecuencia
                if(cancionesUsuario.contains(tablaCanciones.getItems().get(indiceTabla))){
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
                // Si está en modo secuencial, retrocede a la canción anterior en la tabla
                if(indiceTabla == 0){
                    // Si está en la primera canción, retrocede a la última
                    String nombreCancion = tablaCanciones.getItems().get(tablaCanciones.getItems().size()-1).getNombreCancion();
                    String artistas = tablaCanciones.getItems().get(tablaCanciones.getItems().size()-1).getArtistas();

                    Image caratula = new Image(tablaCanciones.getItems().get(tablaCanciones.getItems().size()-1).getCaratula());
                    imagenView.setImage(caratula);

                    indiceTabla = tablaCanciones.getItems().size()-1;
                    // Verifica si la canción está marcada como favorita y actualiza la interfaz gráfica en consecuencia
                    if(cancionesUsuario.contains(tablaCanciones.getItems().get(indiceTabla))){
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
                    // Retrocede a la canción anterior en la tabla
                    indiceTabla = indiceTabla-1;

                    String nombreCancion = tablaCanciones.getItems().get(indiceTabla).getNombreCancion();
                    String artistas = tablaCanciones.getItems().get(indiceTabla).getArtistas();
                    Image caratula = new Image(tablaCanciones.getItems().get(indiceTabla).getCaratula());
                    imagenView.setImage(caratula);
                    // Verifica si la canción está marcada como favorita y actualiza la interfaz gráfica en consecuencia
                    if(cancionesUsuario.contains(tablaCanciones.getItems().get(indiceTabla))){
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
    /**
     * Avanza a la siguiente canción en la tabla de canciones y actualiza la interfaz gráfica.
     * Si se está reproduciendo en modo aleatorio, selecciona una canción aleatoria.
     * Si se está reproduciendo en modo secuencial, avanza a la siguiente canción en la tabla.
     *
     * @param actionEvent El evento que desencadenó la acción de avanzar.
     */
    public void next(ActionEvent actionEvent) {
        if(indiceTabla!= -1){
            if(stateAletorio){
                // Si está en modo aleatorio, elige una canción aleatoria
                Random random = new Random();
                int tamañoTabla = tablaCanciones.getItems().size()-1;
                int indiceAleatorio = random.nextInt(tamañoTabla);

                indiceTabla = indiceAleatorio;

                Image caratula = new Image(tablaCanciones.getItems().get(indiceTabla).getCaratula());
                imagenView.setImage(caratula);

                if(cancionesUsuario.contains(tablaCanciones.getItems().get(indiceAleatorio))){
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
                // Si está en modo secuencial, avanza a la siguiente canción en la tabla
                if(indiceTabla == tablaCanciones.getItems().size()-1){

                    indiceTabla = 0;

                    if(cancionesUsuario.contains(tablaCanciones.getItems().get(0))){
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
                    // Avanza a la siguiente canción en la tabla
                    indiceTabla = indiceTabla + 1;
                    if(cancionesUsuario.contains(tablaCanciones.getItems().get(indiceTabla))){
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

    /**
     * Gestiona la acción de marcar o desmarcar una canción como favorita.
     * Si la canción no está marcada como favorita, la marca como favorita y la agrega a la lista de canciones del usuario.
     * Si la canción ya está marcada como favorita, la desmarca como favorita y la elimina de la lista de canciones del usuario.
     * Actualiza la interfaz gráfica y guarda los cambios en la persistencia de datos.
     *
     * @param actionEvent El evento que desencadenó la acción de marcar o desmarcar como favorita.
     */
    public void like(ActionEvent actionEvent) {
        // Obtiene la canción seleccionada en la tabla de canciones
        Cancion cancionSeleccionada = tablaCanciones.getSelectionModel().getSelectedItem();

        // Verifica si la canción está marcada como favorita o no
        if(!stateLike){
            // Si la canción no está marcada como favorita, la marca como favorita y la agrega a la lista de canciones del usuario
            usuario.getCanciones().agregar(tablaCanciones.getItems().get(indiceTabla));
            storify.serializar(); // Guarda los cambios en la persistencia de datos
            Image image = new Image("/imagenes/check.png");
            imgLike.setImage(image); // Actualiza la imagen del botón "Me gusta"
            stateLike = true; // Actualiza el estado de la canción como favorita
            storify.pilaDeshacer.push("Insertar"); // Agrega la acción a la pila de deshacer
            storify.pilaCancionesDeshacer.push(cancionSeleccionada); // Agrega la canción a la pila de deshacer

        }else{
            // Si la canción ya está marcada como favorita, la desmarca como favorita y la elimina de la lista de canciones del usuario
            usuario.getCanciones().eliminar(tablaCanciones.getItems().get(indiceTabla));
            storify.serializar(); // Guarda los cambios en la persistencia de datos
            Image image = new Image("/imagenes/checkOFF.png");
            imgLike.setImage(image); // Actualiza la imagen del botón "Me gusta"
            stateLike = false; // Actualiza el estado de la canción como no favorita
            storify.pilaDeshacer.push("Eliminar"); // Agrega la acción a la pila de deshacer
            storify.pilaCancionesDeshacer.push(cancionSeleccionada); // Agrega la canción a la pila de deshacer
        }
        // Actualiza la tabla de canciones en la interfaz gráfica con la lista actualizada de canciones del usuario
        tablaCanciones.setItems(FXCollections.observableArrayList(storify.enviarCanciones()));

        // Carga la ventana de "Me gusta" para mostrar las canciones favoritas del usuario
        storify.loadStage("/windows/likes.fxml", actionEvent);
    }

    /**
     * Cambia el estado del modo aleatorio entre activado y desactivado.
     * Si el modo aleatorio está activado, lo desactiva. Si está desactivado, lo activa.
     * Actualiza la interfaz gráfica para reflejar el cambio de estado.
     *
     * @param actionEvent El evento que desencadenó la acción de cambiar el modo aleatorio.
     */
    public void aleatorio(ActionEvent actionEvent) {
        if(stateAletorio){
            // Si el modo aleatorio está activado, lo desactiva
            stateAletorio = false;
            Image image = new Image("/imagenes/aleatorioOFF.png");
            imgAleatorio.setImage(image); // Actualiza la imagen del botón de modo aleatorio
        }else {
            // Si el modo aleatorio está desactivado, lo activa
            Image image = new Image("/imagenes/aleatorioON.png");
            imgAleatorio.setImage(image); // Actualiza la imagen del botón de modo aleatorio
            stateAletorio = true;
        }
    }

    /**
     * Deshace la última acción realizada por el usuario.
     * Si hay una acción para deshacer en la pila de deshacer, la deshace y actualiza la interfaz gráfica.
     * Si no hay acciones para deshacer, muestra un mensaje de error.
     */
    public void deshacer () {
        // Verifica si hay una acción para deshacer en la pila
        if(!storify.pilaDeshacer.empty()){
            Cancion c = storify.pilaCancionesDeshacer.pop(); // Obtiene la última canción afectada por la acción
            if(storify.pilaDeshacer.pop().equals("Eliminar")){ // Si la acción a deshacer fue eliminar una canción, la añade nuevamente a la lista de canciones del usuario
                cancionesUsuario.add(c);
                usuario.getCanciones().agregar(c);
                storify.serializar();
                Image image = new Image("/imagenes/check.png");
                imgLike.setImage(image);
                stateLike = true;
                storify.pilaRehacer.push("Insertar"); // Agrega la acción deshecha a la pila de rehacer
                storify.pilaCancionesRehacer.push(c); // Agrega la canción deshecha a la pila de rehacer
            }else{
                // Si la acción a deshacer fue insertar una canción, la elimina de la lista de canciones del usuario
                cancionesUsuario.remove(c);
                usuario.getCanciones().eliminar(c);
                storify.serializar();
                Image image = new Image("/imagenes/checkOFF.png");
                imgLike.setImage(image);
                stateLike = false;
                storify.pilaRehacer.push("Eliminar"); // Agrega la acción deshecha a la pila de rehacer
                storify.pilaCancionesRehacer.push(c); // Agrega la canción deshecha a la pila de rehacer
            }
        }else{
            // Si no hay acciones para deshacer, muestra un mensaje de error
            storify.mostrarMensaje(Alert.AlertType.ERROR,"No hay acciones para deshacer");
        }
        // Actualiza la tabla de canciones en la interfaz gráfica con la lista actualizada de canciones del usuario
        tablaCanciones.setItems(FXCollections.observableArrayList(storify.enviarCanciones()));
    }

    /**
     * Rehace la última acción deshecha por el usuario.
     * Si hay una acción para rehacer en la pila de rehacer, la ejecuta y actualiza la interfaz gráfica.
     * Si no hay acciones para rehacer, muestra un mensaje de error.
     */
    public void rehacer () {
        if(!storify.pilaRehacer.empty()){
            // Verifica si hay una acción para rehacer en la pila
            Cancion c = storify.pilaCancionesRehacer.pop();
            if(storify.pilaRehacer.pop().equals("Eliminar")){
                // Si la acción a rehacer fue eliminar una canción, la añade nuevamente a la lista de canciones del usuario
                cancionesUsuario.add(c);
                usuario.getCanciones().agregar(c);
                storify.serializar();
                Image image = new Image("/imagenes/check.png");
                imgLike.setImage(image);
                stateLike = true;
            }else{
                // Si la acción a rehacer fue insertar una canción, la elimina de la lista de canciones del usuario
                cancionesUsuario.remove(c);
                usuario.getCanciones().eliminar(c);
                storify.serializar();
                Image image = new Image("/imagenes/checkOFF.png");
                imgLike.setImage(image);
                stateLike = false;
            }
        }else{
            // Si no hay acciones para rehacer, muestra un mensaje de error
            storify.mostrarMensaje(Alert.AlertType.ERROR,"No hay acciones para rehacer");
        }
        // Actualiza la tabla de canciones en la interfaz gráfica con la lista actualizada de canciones del usuario
        tablaCanciones.setItems(FXCollections.observableArrayList(storify.enviarCanciones()));
    }

    /**
     * Deshace la última acción realizada por el usuario.
     * Si hay una acción para deshacer en la pila de deshacer, la deshace y actualiza la interfaz gráfica.
     * Si no hay acciones para deshacer, muestra un mensaje de error.
     *
     * @param actionEvent El evento que desencadenó la acción de deshacer.
     */
    public void deshacer (ActionEvent actionEvent) {
        deshacer();
        storify.loadStage("/windows/likes.fxml", actionEvent);
    }

    /**
     * Rehace la última acción deshecha por el usuario.
     * Si hay una acción para rehacer en la pila de rehacer, la ejecuta y actualiza la interfaz gráfica.
     * Si no hay acciones para rehacer, muestra un mensaje de error.
     *
     * @param actionEvent El evento que desencadenó la acción de rehacer.
     */
    public void rehacer (ActionEvent actionEvent) {
        rehacer();
        storify.loadStage("/windows/likes.fxml", actionEvent);
    }

    /**
     * Ordena las canciones favoritas del usuario según el atributo seleccionado.
     *
     * @param event El evento que desencadenó la acción de ordenar.
     */
    @FXML
    void ordenarLikes(ActionEvent event) {

        //String selectedAttribute = String.valueOf(comboBoxOrdenamiento.getValue());
        // Imprime las canciones favoritas antes de ordenar
        System.out.println("Antes de ordenar: " + cancionesUsuario.toString());
        // Obtiene el atributo seleccionado para ordenar las canciones
        String selectedAttribute = comboBoxOrdenamiento.getValue();

        // Ordena las canciones favoritas según el atributo seleccionado
        switch (selectedAttribute) {
            case "Título":
                cancionesUsuario.sort(Comparator.comparing(Cancion::getNombreCancion));
                break;
            case "Género":
                cancionesUsuario.sort(Comparator.comparing(Cancion::getGenero));
                break;
            case "Año":
                cancionesUsuario.sort(Comparator.comparingInt(Cancion::getAnio));
                break;
            case "Artista":
                cancionesUsuario.sort(Comparator.comparing(Cancion::getArtistas));
                break;
            case "Álbum":
                cancionesUsuario.sort(Comparator.comparing(Cancion::getNombreAlbum));
                break;
            case "Duración":
                cancionesUsuario.sort(Comparator.comparingDouble(Cancion::getDuracion));
            default:
                System.out.println("Atributo de ordenamiento no válido.");
                break;
        }
        System.out.println("Después de ordenar: " + cancionesUsuario.toString());
        // Convierte la lista de canciones favoritas a una lista observable
        ObservableList<Cancion> listaObservable = FXCollections.observableArrayList(cancionesUsuario);
        // Establece la tabla de canciones con la lista observable ordenada
        tablaCanciones.setItems(listaObservable);
    }

}
