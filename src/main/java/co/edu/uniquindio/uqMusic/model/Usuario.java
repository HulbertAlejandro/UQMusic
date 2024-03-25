package co.edu.uniquindio.uqMusic.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario implements Serializable {
    private String username,contrasena,email;
    private ListaCircular<Cancion> canciones;
}
