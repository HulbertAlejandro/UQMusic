package co.edu.uniquindio.uqMusic.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class Cancion implements Serializable {
    private String codigo, nombreCancion,nombreAlbum,caratula,genero,url,artistas;
    private int anio;
    private double duracion;
}
