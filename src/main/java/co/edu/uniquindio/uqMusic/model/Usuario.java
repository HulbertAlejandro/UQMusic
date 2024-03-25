package co.edu.uniquindio.uqMusic.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Usuario {
    private String username,contrasena,email;
    private ListaCircular<Cancion> canciones;
}
