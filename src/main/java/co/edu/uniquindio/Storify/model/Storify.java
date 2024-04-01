package co.edu.uniquindio.Storify.model;

import co.edu.uniquindio.Storify.exceptions.CampoObligatorioException;
import co.edu.uniquindio.Storify.exceptions.CampoRepetido;
import co.edu.uniquindio.Storify.exceptions.CampoVacioException;
import co.edu.uniquindio.Storify.utils.ArchivoUtils;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
     * Código para la próxima canción a ser registrada.
     */
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
        leerUsuario();
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
                state = true;
                break;
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
    /**
     * Registra un nuevo usuario en la aplicación.
     *
     * @param codigoArtista   El codigo de usuario del nuevo artista.
     * @param nombreArtista  El nombre del artista.
     * @param nacionalidadArtista      La nacionalidad del artista.
     * @param esGrupo      Si es grupo el artista.
     * @throws CampoVacioException       Si algún campo obligatorio está vacío.
     * @throws CampoObligatorioException Si algún campo es obligatorio y no se proporciona.
     * @throws CampoRepetido             Si las credenciales proporcionadas ya están en uso.
     */
    public void registrarArtista(String codigoArtista, String nombreArtista, String nacionalidadArtista, boolean esGrupo) throws CampoObligatorioException,CampoVacioException {
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
                .build();
        autores.insertar(autor);
        autores.recorridoEnOrden(autores.getInicio(),0);
        storify.mostrarMensaje(Alert.AlertType.INFORMATION, "Registro exitoso");
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
        //ArchivoUtils.serializarArtistas(RUTA_ARTISTAS, artistas);
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
     * Almacena un usuario en la lista de usuarios registrados.
     *
     * @param Usuario El usuario a almacenar.
     */
    public void almacenarUsuario(Usuario Usuario) {
        USUARIO_SESION = Usuario;
        System.out.println(USUARIO_SESION.getUsername());
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


}
