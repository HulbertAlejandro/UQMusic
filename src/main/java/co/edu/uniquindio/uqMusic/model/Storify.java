package co.edu.uniquindio.uqMusic.model;

import co.edu.uniquindio.uqMusic.exceptions.CampoObligatorioException;
import co.edu.uniquindio.uqMusic.exceptions.CampoRepetido;
import co.edu.uniquindio.uqMusic.exceptions.CampoVacioException;
import co.edu.uniquindio.uqMusic.utils.ArchivoUtils;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.*;

public class Storify {
    private static final String RUTA_USUARIOS = "src/main/java/co/edu/uniquindio/uqMusic/serializable/usuario.ser" ;
    private Usuario USUARIO_SESION = new Usuario();
    private final Map<String, Usuario> usuarios = new HashMap<>();
    private MediaPlayer mediaPlayer;
    private static Storify storify;
    private int proximoCodigoCancion = 0;
    public static Storify getInstance() {
        if (storify == null) {
            storify = new Storify();
        }
        return storify;
    }
    public boolean verificarUsuario(String usuario, String contrasena) {
        boolean state = false;
        for (Usuario c : usuarios.values()) {
            if (c.getUsername().equals(usuario) && c.getContrasena().equals(contrasena)) {
                state = true;
            }
        }
        return state;
    }
    private void leerUsuario() {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(RUTA_USUARIOS))) {
            HashMap<String, Usuario> listaUsuarios = (HashMap<String, Usuario>) entrada.readObject();
            System.out.println("LECTURA DE USUARIOS COMPLETA");
            usuarios.putAll(listaUsuarios);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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
        }else{
            mostrarMensaje(Alert.AlertType.WARNING,"El usuario ya existe");
        }
    }
    /*
    METODO PARA VERIFICAR EL NUMERO DE IDENTIFICACION EXISTA EN EL SISTEMA
     */
    private Usuario verifyId(String usuario) {
        Usuario Usuario = null;
        for (Usuario client : usuarios.values()){
            if(client.getUsername().equals(usuario))
            {
                Usuario =  client;
            }
        }
        return Usuario;
    }
    /*
    SE VERIFICAN LAS CREDENCIALES DEL USUARIO QUE QUIERE INGRESAR AL SISTEMA
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
    //METODO PARA VERIFICAR QUE EL USUARIO SEA UNICO
    private boolean verifyUser(String username, Map<String, Usuario> usuarios) {
        return !usuarios.containsKey(username);
    }
    /*

    METODO PARA CARGAR LAS PESTAÑAS
     */
    public void loadStage(String url, Event event) {
        try {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            Parent root = FXMLLoader.load(Objects.requireNonNull(Storify.class.getResource(url)));
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("UQ Online Store");
            newStage.show();
        } catch (Exception ignored) {
        }
    }
    /*
    METODO PARA VERIFICAR EL USUARIO
     */
    public boolean verifyUser(String user, String password) {
        return findUser(user, password);
    }
    /*
    METODO PARA BUSCAR EL USUARIO
     */
    private boolean findUser(String user, String password) {
        for (Usuario Usuario : usuarios.values()) {
            if (Usuario.getUsername().equals(user) && Usuario.getContrasena().equals(password)) {
                return true;
            }
        }
        return false;
    }
    /*
    METODO PARA MOSTRAR ALERTAS
     */
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
    /*
    METODO PARA INICIALIZAR LOS DATOS
     */

    public void registrarCancion(String name,String artistas, String code, String album, String caratula, String genero, String url, String anio, double duracion) throws CampoObligatorioException, CampoVacioException, CampoRepetido {
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
        if (duracion<0) {
            throw new CampoVacioException("Es necesario ingresar la duracion");
        }
//        if (verifyCode(code)) {
//            throw new CampoRepetido("El codigo del producto no esta disponible");
//        }
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
//      ArchivoUtils.serializarArtistas(RUTA_ARTISTAS, artistas);
    }

    private void almacenarCancion(Cancion cancion, String artistas) {
    }

    public Usuario guardarUsuario(String user, String password) {
        Usuario findUser = null;
        for (Usuario Usuario : usuarios.values()) {
            if (Usuario.getUsername().equals(user) && Usuario.getContrasena().equals(password)) {
                findUser = Usuario;
            }
        }
        return findUser;
    }
    //METODO PARA GENERAR AUTOMATICAMENTE EL CODIGO DE LA CANCION
    private int generarCodigoProducto() {
        proximoCodigoCancion+= 1;
        return proximoCodigoCancion;
    }
    /*
    METODO PARA ALMACENAR EL Usuario
     */
    public void almacenarUsuario(Usuario Usuario) {
        USUARIO_SESION = Usuario;
        System.out.println(USUARIO_SESION.getUsername());
    }
    /*
    METODO PARA SERIALIZAR LOS DATOS ALMACENADOS
     */
    public void serializar() {
        ArchivoUtils.serializarUsuario(RUTA_USUARIOS, (HashMap<String, Usuario>) usuarios);
    }
  
    /*
    METODO PARA INCIALIZAR Y LIMPIAR LAS ESTRUCTURAS DE DATOS
     */
    public void inicializar() {
        leerUsuario();
    }

    /*
    METODO PARA SERIALIZAR LOS DATOS ALMACENADOS
     */
 
}
