package co.edu.uniquindio.Storify.model;

import co.edu.uniquindio.Storify.exceptions.CampoObligatorioException;
import co.edu.uniquindio.Storify.exceptions.CampoRepetido;
import co.edu.uniquindio.Storify.exceptions.CampoVacioException;
import co.edu.uniquindio.Storify.utils.ArchivoUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * La clase Storify representa la lógica de negocio de la aplicación Storify.
 * Contiene métodos para la gestión de usuarios, canciones y otras funcionalidades de la aplicación.
 */
public class Storify {
    /**
     * Ruta del archivo que almacena los datos de los usuarios.
     */
    private static final String RUTA_USUARIOS = "src/main/resources/serializable/usuario.ser";
    /**
     * Ruta del archivo que almacena los datos de los artistas.
     */
    private static final String RUTA_ARTISTAS = "src/main/resources/serializable/artistas.ser";
    /**
     * Instancia única de la clase Storify.
     */
    private static Storify storify;
    /**
     * Mapa que almacena los usuarios registrados en la aplicación.
     */
    private final Map<String, Usuario> usuarios = new HashMap<>();
    /**
     * Usuario en sesión actual.
     */
    private Usuario USUARIO_SESION = new Usuario();
    /**
     * Reproductor de medios para la reproducción de canciones.
     */
    private MediaPlayer mediaPlayer;
    /**
     * Código para la próxima canción a ser registrada.
     */
    private int proximoCodigoCancion = 0;
    /**
     * ArrayList de autores y generos con sus respectivos contadores
     */
    public ArrayList<String> listaAutores = new ArrayList<>();
    public ArrayList<String> listaGeneros = new ArrayList<>();
    public ArrayList<Integer> contadoresGenero = new ArrayList<>();
    public ArrayList<Integer> contadoresArtista = new ArrayList<>();
    public Stack<String> pilaDeshacer = new Stack<>();
    public Stack<String> pilaRehacer = new Stack<>();
    public Stack<Cancion> pilaCancionesDeshacer = new Stack<>();
    public Stack<Cancion> pilaCancionesRehacer = new Stack<>();

    private ArbolBinario autores = new ArbolBinario();

    /**
     * Método que devuelve la instancia única de la clase Storify (patrón Singleton).
     *
     * @return La instancia única de la clase Storify.
     */
    public static Storify getInstance() {
        if (storify == null) {
            storify = new Storify();
        }
        return storify;
    }

    /**
     * Inicializa la aplicación cargando los datos almacenados.
     */
    public void inicializar() {
        leerArtistas();
        leerUsuario();
        autores.recorridoEnOrden(autores.getInicio(), 0);
    }

    private void leerArtistas() {
        File archivoUsuarios = new File(RUTA_ARTISTAS);
        if (archivoUsuarios.length() == 0) {
            System.out.println("El archivo de usuarios esta vacio.");
            return;
        }
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivoUsuarios))) {
            autores = (ArbolBinario) entrada.readObject();
            System.out.println("Lectura de artistas exitosa");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifica si un usuario y contraseña coinciden con los registrados en la aplicación.
     *
     * @param usuario    El nombre de usuario.
     * @param contrasena La contraseña del usuario.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean verificarUsuario(String usuario, String contrasena) {
        boolean state = false;
        for (Usuario c : usuarios.values()) {
            if (c.getUsername().equals(usuario) && c.getContrasena().equals(contrasena)) {
                USUARIO_SESION = c;
                state = true;
            }
        }
        return state;
    }

    /**
     * Lee los usuarios almacenados en el archivo de datos y los carga en la aplicación.
     */
    private void leerUsuario() {
        File archivoUsuarios = new File(RUTA_USUARIOS);
        if (archivoUsuarios.length() == 0) {
            System.out.println("El archivo de usuarios esta vacio.");
            return;
        }

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivoUsuarios))) {
            HashMap<String, Usuario> listaUsuarios = (HashMap<String, Usuario>) entrada.readObject();
            System.out.println("Lectura de usuarios exitosa");
            usuarios.putAll(listaUsuarios);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registra un nuevo usuario en la aplicación.
     *
     * @param username   El nombre de usuario del nuevo usuario.
     * @param contrasena La contraseña del nuevo usuario.
     * @param email      El correo electrónico del nuevo usuario.
     * @throws CampoVacioException       Si algún campo obligatorio está vacío.
     * @throws CampoObligatorioException Si algún campo es obligatorio y no se proporciona.
     * @throws CampoRepetido             Si las credenciales proporcionadas ya están en uso.
     */
    public void registrarUsuario(String username, String contrasena, String email) throws CampoVacioException, CampoObligatorioException, CampoRepetido {
        if (username == null || username.isEmpty()) {
            throw new CampoObligatorioException(("Es necesario ingresar el nombre"));
        }
        if (email == null || email.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la direccion.");
        }
        if (contrasena == null || contrasena.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la contraseña");
        }
        if (verifyCredentials(username, contrasena)) {
            throw new CampoRepetido("Las credenciales proporcionadas no estan disponibles");
        }
        if (verifyUser(username, usuarios)) {
            Usuario usuario = Usuario.builder()
                    .username(username)
                    .email(email)
                    .contrasena(contrasena)
                    .canciones(new ListaCircular<>()).build();
            usuarios.put(username, usuario);
            ArchivoUtils.serializarUsuario(RUTA_USUARIOS, (HashMap<String, Usuario>) usuarios);
            storify.mostrarMensaje(Alert.AlertType.INFORMATION, "Registro exitoso");
        } else {
            mostrarMensaje(Alert.AlertType.WARNING, "El usuario ya existe");
        }
    }

    public void guardarCancion(String nombre, String album, String urlCaratula, int anioLanzamiento, double duracionCancion, String genre, String url, String codigo, Autor autorSelected) throws CampoObligatorioException, CampoVacioException {
        if (nombre == null || nombre.isEmpty()) {
            throw new CampoObligatorioException(("Es necesario ingresar el nombre"));
        }
        if (album == null || album.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la direccion.");
        }
        if (urlCaratula == null || urlCaratula.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la contraseña");
        }
        if (anioLanzamiento < 0) {
            throw new CampoVacioException("Es necesario ingresar la contraseña");
        }
        if (duracionCancion < 0) {
            throw new CampoVacioException("Es necesario ingresar la contraseña");
        }
        if (genre == null || genre.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la contraseña");
        }
        if (url == null || url.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la contraseña");
        }
        if (codigo == null || codigo.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la contraseña");
        }
        Cancion cancion = Cancion.builder()
                .nombreCancion(nombre)
                .anio(anioLanzamiento)
                .artistas(autorSelected.getNombre())
                .caratula(urlCaratula)
                .codigo(codigo)
                .genero(genre)
                .url(url)
                .duracion(duracionCancion)
                .nombreAlbum(album)
                .build();
        if (autores.agregarAtributo(autores.getInicio(), autorSelected.getNombre(), cancion)) {
            mostrarMensaje(Alert.AlertType.CONFIRMATION, "La cancion ha sido agregada correctamente al artista: " + autorSelected.getNombre());
        } else {
            mostrarMensaje(Alert.AlertType.ERROR, "No se logró agregar la cancion correctamente");
        }
        ArchivoUtils.serializarArtista(RUTA_ARTISTAS, autores);
    }

    /**
     * Registra un nuevo usuario en la aplicación.
     *
     * @param codigoArtista       El codigo de usuario del nuevo artista.
     * @param nombreArtista       El nombre del artista.
     * @param nacionalidadArtista La nacionalidad del artista.
     * @param esGrupo             Si es grupo el artista.
     * @throws CampoVacioException       Si algún campo obligatorio está vacío.
     * @throws CampoObligatorioException Si algún campo es obligatorio y no se proporciona.
     * @throws CampoRepetido             Si las credenciales proporcionadas ya están en uso.
     */
    public void registrarArtista(String codigoArtista, String nombreArtista, String nacionalidadArtista, boolean esGrupo) throws CampoObligatorioException, CampoVacioException {
        if (codigoArtista == null || codigoArtista.isEmpty()) {
            throw new CampoObligatorioException(("Es necesario ingresar el nombre"));
        }
        if (verificarArtista(codigoArtista)) {
            throw new CampoVacioException("El codigo del artista no es valido");
        }
        if (nombreArtista == null || nombreArtista.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la direccion.");
        }
        if (nacionalidadArtista == null || nacionalidadArtista.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la contraseña");
        }
        Autor autor = Autor.builder()
                .nombre(nombreArtista)
                .codigo(codigoArtista)
                .esGrupo(esGrupo)
                .listaCanciones(new ListaDoblementeEnlazada<>())
                .build();
        autores.insertar(autor);
        autores.recorridoEnOrden(autores.getInicio(), 0);
        storify.mostrarMensaje(Alert.AlertType.INFORMATION, "Registro exitoso");
        ArchivoUtils.serializarArtista(RUTA_ARTISTAS, autores);
    }

    /**
     * Verifica si el codigo de un artista ya existe en la lista de artistas registrados.
     *
     * @param codigoArtista El nombre de usuario a verificar.
     * @return El usuario si es encontrado, o null si no existe.
     */
    private boolean verificarArtista(String codigoArtista) {
        return autores.buscarCodigo(codigoArtista);
    }

    /**
     * Verifica si un usuario ya existe en la lista de usuarios registrados.
     *
     * @param username El nombre de usuario a verificar.
     * @return El usuario si es encontrado, o null si no existe.
     */
    private Usuario buscarUsuarioPorNombre(String username) {
        Usuario usuarioEncontrado = null;
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getUsername().equals(username)) {
                usuarioEncontrado = usuario;
                break;
            }
        }
        return usuarioEncontrado;
    }

    /**
     * Verifica si un usuario y contraseña coinciden con los registrados en la aplicación.
     *
     * @param usuario    El nombre de usuario.
     * @param contrasena La contraseña del usuario.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    private boolean verifyCredentials(String usuario, String contrasena) {
        boolean state = false;
        for (Usuario c : usuarios.values()) {
            if (c.getUsername().equals(usuario) && c.getContrasena().equals(contrasena)) {
                state = true;
                break;
            }
        }
        return state;
    }

    /**
     * Verifica si un usuario es único dentro de la aplicación.
     *
     * @param username El nombre de usuario a verificar.
     * @param usuarios El mapa de usuarios registrados.
     * @return true si el usuario es único, false si ya existe.
     */
    private boolean verifyUser(String username, Map<String, Usuario> usuarios) {
        return !usuarios.containsKey(username);
    }

    /**
     * Busca un usuario en la lista de usuarios registrados.
     *
     * @param user     El nombre de usuario a buscar.
     * @param password La contraseña del usuario a buscar.
     * @return true si el usuario es encontrado, false en caso contrario.
     */
    private boolean findUser(String user, String password) {
        for (Usuario Usuario : usuarios.values()) {
            if (Usuario.getUsername().equals(user) && Usuario.getContrasena().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Registra una nueva canción en la aplicación.
     *
     * @param name     El nombre de la canción.
     * @param artistas Los artistas de la canción.
     * @param code     El código de la canción.
     * @param album    El álbum al que pertenece la canción.
     * @param caratula La URL de la carátula de la canción.
     * @param genero   El género musical de la canción.
     * @param url      La URL de la canción.
     * @param anio     El año de lanzamiento de la canción.
     * @param duracion La duración de la canción en segundos.
     * @throws CampoObligatorioException Si algún campo obligatorio está vacío.
     * @throws CampoVacioException       Si algún campo está vacío.
     * @throws CampoRepetido             Si el código de la canción ya está en uso.
     */
    public void registrarCancion(String name, String artistas, String code, String album, String caratula, String genero, String url, String anio, double duracion) throws CampoObligatorioException, CampoVacioException, CampoRepetido {
        if (name == null || name.isEmpty()) {
            throw new CampoObligatorioException(("Es necesario ingresar el nombre"));
        }
        if (artistas == null || artistas.isEmpty()) {
            throw new CampoObligatorioException(("Es necesario ingresar los artistas"));
        }
        if (code == null || code.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar el codigo.");
        }
        if (album == null || album.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar el album de la cancion");
        }
        if (caratula == null || caratula.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la imagen de la caratula");
        }
        if (genero == null || genero.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar el genero");
        }
        if (url == null || url.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar la URL de la cancion");
        }
        if (anio == null || anio.isEmpty()) {
            throw new CampoVacioException("Es necesario ingresar el año");
        }
        if (duracion < 0) {
            throw new CampoVacioException("Es necesario ingresar la duracion");
        }

        Cancion cancion = Cancion.builder()
                .nombreCancion(name)
                .nombreAlbum(album)
                .codigo(code)
                .caratula(caratula)
                .anio(Integer.parseInt(anio))
                .duracion(duracion)
                .genero(genero)
                .url(url)
                .artistas(artistas)
                .build();
        almacenarCancion(cancion, artistas);
        ArchivoUtils.serializarArtista(RUTA_ARTISTAS, autores);
    }

    private void almacenarCancion(Cancion cancion, String artistas) {

    }

    /**
     * Guarda la información del usuario en sesión.
     *
     * @param user     El nombre de usuario en sesión.
     * @param password La contraseña del usuario en sesión.
     * @return El usuario en sesión.
     */
    public Usuario guardarUsuario(String user, String password) {
        Usuario findUser = null;
        for (Usuario Usuario : usuarios.values()) {
            if (Usuario.getUsername().equals(user) && Usuario.getContrasena().equals(password)) {
                findUser = Usuario;
            }
        }
        return findUser;
    }

    /**
     * Genera un nuevo código para una canción.
     *
     * @return El nuevo código generado.
     */
    private int generarCodigoProducto() {
        proximoCodigoCancion += 1;
        return proximoCodigoCancion;
    }

    /**
     * Serializa los datos de la aplicación y los guarda en archivos.
     */
    public void serializar() {
        ArchivoUtils.serializarUsuario(RUTA_USUARIOS, (HashMap<String, Usuario>) usuarios);
    }

    /**
     * Carga una nueva ventana a partir de un archivo FXML.
     *
     * @param url   La URL del archivo FXML que define la ventana.
     * @param event El evento que desencadena la carga de la ventana.
     */
    public void loadStage(String url, Event event) {
        try {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            Parent root = FXMLLoader.load(Objects.requireNonNull(Storify.class.getResource(url)));
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Storify");
            newStage.getIcons().add(new Image("/imagenes/storify.png"));
            newStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Muestra una alerta con el mensaje especificado.
     *
     * @param tipo    El tipo de alerta (INFORMATION, WARNING, ERROR, etc.).
     * @param mensaje El mensaje a mostrar en la alerta.
     */
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    public ArrayList<Autor> enviarAutores() {
        return autores.toList();
    }

    /**
     * Borra los datos serializados almacenados en el archivo especificado.
     * Si el archivo existe, se eliminan todos los archivos y directorios dentro del directorio del archivo, incluido el archivo mismo.
     *
     * @param archivo El nombre o la ruta del archivo que contiene los datos serializados a borrar.
     */
    public void borrarDatosSerializados(String archivo) {
        Path path = Paths.get(archivo);
        try {
            if (Files.exists(path)) {
                Files.walk(path)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve una lista de todas las canciones almacenadas en la estructura de datos.
     *
     * @return Una lista de todas las canciones.
     */
    public ArrayList<Cancion> enviarCanciones() {
        return autores.recorridoCanciones(autores.getInicio(), new ArrayList<>());
    }

    /**
     * Carga un nuevo artista en la estructura de datos y serializa la información.
     *
     * @param autor El autor a cargar.
     */
    public void cargarArtista(Autor autor) {
        autores.insertar(autor);
        ArchivoUtils.serializarArtista(RUTA_ARTISTAS, autores);
    }

    /**
     * Devuelve el árbol binario que contiene todos los artistas almacenados.
     *
     * @return El árbol binario de artistas.
     */
    public ArbolBinario enviarArtistas() {
        return autores;
    }

    /**
     * Devuelve el usuario actual en sesión.
     *
     * @return El usuario en sesión.
     */
    public Usuario enviarUsuario() {
        return USUARIO_SESION;
    }

    /**
     * Verifica si una canción con el código especificado está contenida en la lista de canciones del usuario.
     *
     * @param cancionesUsuario La lista de canciones del usuario.
     * @param codigo El código de la canción a verificar.
     * @return true si la canción está contenida en la lista del usuario, false de lo contrario.
     */
    public boolean verificarContencion(ArrayList<Cancion> cancionesUsuario, String codigo) {
        for (Cancion cancion : cancionesUsuario) {
            if (cancion.getCodigo().equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo para inicializar los artistas y los generos con los contadores en 0
     */
    public void llenarAtributos(){
        // Llenar el ArrayList contadores de genero con números del 1 al 9
        for (int i = 1; i <= 5; i++) {
            contadoresGenero.add(0);
        }
        // Llenar el ArrayList contadores de artista con números del 1 al 9
        for (int i = 1; i <= 9; i++) {
            contadoresArtista.add(0);
        }
        // Agregar los nombres al ArrayList de generos
        listaGeneros.add("Rock");
        listaGeneros.add("Pop");
        listaGeneros.add("Punk");
        listaGeneros.add("Reggaeton");
        listaGeneros.add("Electronica");

        // Agregar los nombres al ArrayList de artistas
        listaAutores.add("Bad Bunny");
        listaAutores.add("Blessd");
        listaAutores.add("Dei V");
        listaAutores.add("Ele A El Dominio");
        listaAutores.add("Feid");
        listaAutores.add("Jhayco");
        listaAutores.add("Kris Floyd");
        listaAutores.add("Lenny Tavares");
        listaAutores.add("Omar Courtz");
    }

    /**
     * Metodo para contar los generos más escuchados
     */
    public void contarGenero(){
        for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
            Usuario usuario = entry.getValue();
            ListaCircular<Cancion> lista = usuario.getCanciones();
            ArrayList<Cancion> canciones = lista.toArrayList();
            for (int i = 0; i < canciones.size(); i++) {
                Cancion cancion = canciones.get(i);
                String generoCancion = cancion.getGenero();
                for (int j = 0; j < listaGeneros.size(); j++) {
                    if(generoCancion.equals(listaGeneros.get(j))){
                        int c = contadoresGenero.get(j) + 1;
                        contadoresGenero.set(j, c);
                    }
                }
            }
        }
    }

    /**
     * Metodo para contar los artistas más escuchados
     */
    public void contarArtista(){
        for (Map.Entry<String, Usuario> entry : usuarios.entrySet()) {
            Usuario usuario = entry.getValue();
            ListaCircular<Cancion> lista = usuario.getCanciones();
            ArrayList<Cancion> canciones = lista.toArrayList();
            for (int i = 0; i < canciones.size(); i++) {
                Cancion cancion = canciones.get(i);
                String artistaCancion = cancion.getArtistas();
                for (int j = 0; j < listaAutores.size(); j++) {
                    if(artistaCancion.equals(listaAutores.get(j))){
                        int c = contadoresArtista.get(j) + 1;
                        contadoresArtista.set(j, c);
                    }
                }
            }
        }
    }

    /**
     * Recorre el subárbol izquierdo del nodo dado de manera completa, buscando canciones que coincidan con los atributos especificados.
     *
     * @param inicio El nodo a partir del cual comenzar el recorrido.
     * @param cancions La lista de canciones encontradas hasta el momento.
     * @param atributos Los atributos que deben coincidir para que una canción sea añadida a la lista.
     * @return Una lista de canciones que coinciden con los atributos especificados en el subárbol izquierdo del nodo dado.
     */
    public ArrayList<Cancion> recorrerIzquierdaCompleto(NodoArbol inicio, ArrayList<Cancion> cancions, String[] atributos) {
        if (inicio == null) {
            return cancions;
        }
        ArrayList<Cancion> canciones = inicio.getAutor().getListaCanciones().toArrayList();
        for (Cancion cancion : canciones) {
            int atributosCoincidentes = 0;
            for (String atr : atributos) {
                if (cancion.coincideAtributo(atr)) {
                    atributosCoincidentes += 1;
                }
            }
            if (atributosCoincidentes == atributos.length) {
                cancions.add(cancion);
            }
        }
        recorrerIzquierdaCompleto(inicio.getNodoIzquierda(), cancions, atributos);
        recorrerIzquierdaCompleto(inicio.getNodoDerecha(), cancions, atributos);
        return cancions;
    }

    /**
     * Recorre el subárbol derecho del nodo dado de manera completa, buscando canciones que coincidan con los atributos especificados.
     *
     * @param inicio El nodo a partir del cual comenzar el recorrido.
     * @param cancions La lista de canciones encontradas hasta el momento.
     * @param atributos Los atributos que deben coincidir para que una canción sea añadida a la lista.
     * @return Una lista de canciones que coinciden con los atributos especificados en el subárbol derecho del nodo dado.
     */
    public ArrayList<Cancion> recorrerDerechaCompleto(NodoArbol inicio, ArrayList<Cancion> cancions, String[] atributos) {
        if (inicio == null) {
            return cancions;
        }
        ArrayList<Cancion> canciones = inicio.getAutor().getListaCanciones().toArrayList();
        for (Cancion cancion : canciones) {
            int atributosCoincidentes = 0;
            for (String atr : atributos) {
                if (cancion.coincideAtributo(atr)) {
                    atributosCoincidentes += 1;
                }
            }
            if (atributosCoincidentes == atributos.length) {
                cancions.add(cancion);
            }
        }
        recorrerDerechaCompleto(inicio.getNodoIzquierda(), cancions, atributos);
        recorrerDerechaCompleto(inicio.getNodoDerecha(), cancions, atributos);
        return cancions;
    }

    /**
     * Busca canciones filtradas por el nombre del artista.
     *
     * @param nombre El nombre del artista a buscar.
     * @return Una lista observable de canciones filtradas por el nombre del artista.
     */
    public ObservableList<Cancion> buscarPorArtistas(String nombre) {
        if (nombre.isBlank() || nombre.isEmpty()) {
            return FXCollections.observableArrayList(enviarCanciones());
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

    /**
     * Busca canciones filtradas por un conjunto de atributos utilizando la operación lógica "o".
     *
     * @param atributo La cadena de atributos separados por comas.
     * @return Una lista observable de canciones que coinciden con al menos uno de los atributos proporcionados.
     */
    public ObservableList<Cancion> buscarPorO(String atributo) {

        ObservableList<Cancion> cancionesCoincidentes = FXCollections.observableArrayList();

        if (atributo == null || atributo.isEmpty()) {
            return FXCollections.observableArrayList(enviarCanciones());
        }

        String[] atributos = atributo.split(",");

        if (atributos.length == 0) {
            return FXCollections.observableArrayList(enviarCanciones());
        }

        ArbolBinario arbolAutores = storify.enviarArtistas();
        ArrayList<Cancion> cancionesIzquierda = new ArrayList<>();
        ArrayList<Cancion> cancionesDerecha = new ArrayList<>();
        ArrayList<Cancion> cancionesRaiz;

        cancionesRaiz = evaluarRaizO(arbolAutores.getInicio(),new ArrayList<>(), atributos);
        Thread hiloIzquierda = new Thread(() -> cancionesIzquierda.addAll(recorrerIzquierda(arbolAutores.getInicio().getNodoIzquierda(), new ArrayList<>(), atributos)));
        Thread hiloDerecha = new Thread(() -> cancionesDerecha.addAll(recorrerDerecha(arbolAutores.getInicio().getNodoDerecha(), new ArrayList<>(), atributos)));

        hiloIzquierda.start();
        hiloDerecha.start();

        try {
            hiloIzquierda.join();
            hiloDerecha.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cancionesCoincidentes.addAll(cancionesRaiz);
        cancionesCoincidentes.addAll(cancionesIzquierda);
        cancionesCoincidentes.addAll(cancionesDerecha);

        return cancionesCoincidentes;
    }

    /**
     * Evalúa las canciones en el nodo raíz del árbol utilizando la operación lógica "o".
     *
     * @param inicio     El nodo raíz del árbol.
     * @param cancions   La lista de canciones actualmente encontradas.
     * @param atributos  Los atributos para los cuales se busca coincidencia.
     * @return Una lista de canciones que coinciden con al menos uno de los atributos proporcionados.
     */
    public ArrayList<Cancion> evaluarRaizO(NodoArbol inicio, ArrayList<Cancion> cancions, String[] atributos) {
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
        return cancions;
    }

    /**
     * Evalúa las canciones en el nodo raíz del árbol utilizando la operación lógica "y".
     *
     * @param inicio     El nodo raíz del árbol.
     * @param cancions   La lista de canciones actualmente encontradas.
     * @param atributos  Los atributos para los cuales se busca coincidencia.
     * @return Una lista de canciones que coinciden con todos los atributos proporcionados.
     */
    public ArrayList<Cancion> evaluarRaizY(NodoArbol inicio, ArrayList<Cancion> cancions, String[] atributos) {
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
        return cancions;
    }

    /**
     * Recorre el subárbol izquierdo del nodo proporcionado en busca de canciones que coincidan con los atributos especificados.
     *
     * @param inicio     El nodo raíz del subárbol izquierdo.
     * @param cancions   La lista de canciones actualmente encontradas.
     * @param atributos  Los atributos para los cuales se busca coincidencia.
     * @return Una lista de canciones que coinciden con los atributos proporcionados en el subárbol izquierdo.
     */
    public ArrayList<Cancion> recorrerIzquierda(NodoArbol inicio, ArrayList<Cancion> cancions, String[] atributos) {
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

    /**
     * Recorre el subárbol derecho del nodo proporcionado en busca de canciones que coincidan con los atributos especificados.
     *
     * @param inicio     El nodo raíz del subárbol derecho.
     * @param cancions   La lista de canciones actualmente encontradas.
     * @param atributos  Los atributos para los cuales se busca coincidencia.
     * @return Una lista de canciones que coinciden con los atributos proporcionados en el subárbol derecho.
     */
    public ArrayList<Cancion> recorrerDerecha(NodoArbol inicio, ArrayList<Cancion> cancions, String[] atributos) {
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

    /**
     * Busca canciones que coincidan con todos los atributos especificados mediante el operador lógico "Y".
     *
     * @param atributo El conjunto de atributos separados por comas para los cuales se busca coincidencia.
     * @return Una lista de canciones que coinciden con todos los atributos proporcionados.
     */
    public ObservableList<Cancion> buscarPorY(String atributo) {

        ObservableList<Cancion> cancionesCoincidentes = FXCollections.observableArrayList();

        if (atributo == null || atributo.isEmpty()) {
            return FXCollections.observableArrayList(enviarCanciones());
        }
        String[] atributos = atributo.split(",");

        if (atributos.length == 0) {
            return FXCollections.observableArrayList(enviarCanciones());
        }

        ArbolBinario arbolAutores = storify.enviarArtistas();


        ArrayList<Cancion> cancionesIzquierda = new ArrayList<>();
        ArrayList<Cancion> cancionesDerecha = new ArrayList<>();
        ArrayList<Cancion> cancionesRaiz;

        cancionesRaiz = storify.evaluarRaizY(arbolAutores.getInicio(),new ArrayList<>(), atributos);
        Thread hiloIzquierda = new Thread(() -> cancionesIzquierda.addAll(storify.recorrerIzquierdaCompleto(arbolAutores.getInicio().getNodoIzquierda(), new ArrayList<>(), atributos)));
        Thread hiloDerecha = new Thread(() -> cancionesDerecha.addAll(storify.recorrerDerechaCompleto(arbolAutores.getInicio().getNodoDerecha(), new ArrayList<>(), atributos)));

        hiloIzquierda.start();
        hiloDerecha.start();

        try {
            hiloIzquierda.join();
            hiloDerecha.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cancionesCoincidentes.addAll(cancionesRaiz);
        cancionesCoincidentes.addAll(cancionesIzquierda);
        cancionesCoincidentes.addAll(cancionesDerecha);

        return cancionesCoincidentes;
    }
}
