package co.edu.uniquindio.uqMusic.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Cancion {
    private String codigo;
    private String nombreCancion;
    private String nombreAlbum;
    private String caratula;
    private int anio;
    private double duracion;
    private String genero;
    private String url;

    public Cancion(String codigo, String nombreCancion, String nombreAlbum, String caratula, int anio, double duracion, String genero, String url) {
        this.codigo = codigo;
        this.nombreCancion = nombreCancion;
        this.nombreAlbum = nombreAlbum;
        this.caratula = caratula;
        this.anio = anio;
        this.duracion = duracion;
        this.genero = genero;
        this.url = url;
    }
}
