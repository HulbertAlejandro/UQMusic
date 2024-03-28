package co.edu.uniquindio.Storify.model;

import lombok.*;

import java.io.Serializable;

/**
 * La clase Usuario representa a un usuario registrado en la aplicación Storify.
 * Contiene información básica del usuario, como nombre de usuario, contraseña y correo electrónico,
 * así como una lista de canciones asociadas al usuario.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario implements Serializable {
    /**
     * El nombre de usuario del usuario.
     */
    private String username;

    /**
     * La contraseña del usuario.
     */
    private String contrasena;

    /**
     * El correo electrónico del usuario.
     */
    private String email;

    /**
     * Lista circular de canciones asociadas al usuario.
     */
    private ListaCircular<Cancion> canciones;
}
