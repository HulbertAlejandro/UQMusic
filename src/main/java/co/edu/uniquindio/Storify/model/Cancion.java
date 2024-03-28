package co.edu.uniquindio.Storify.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * La clase Cancion representa una canción en la aplicación Storify.
 * Implementa la interfaz Serializable para permitir la serialización de objetos.
 */
@Setter
@Getter
@Builder
public class Cancion implements Serializable {

    /**
     * El código único de la canción.
     */
    private String codigo;

    /**
     * El nombre de la canción.
     */
    private String nombreCancion;

    /**
     * El nombre del álbum al que pertenece la canción.
     */
    private String nombreAlbum;

    /**
     * La URL de la carátula del álbum.
     */
    private String caratula;

    /**
     * El género musical de la canción.
     */
    private String genero;

    /**
     * La URL de la canción en línea.
     */
    private String url;

    /**
     * Los artistas asociados a la canción.
     */
    private String artistas;

    /**
     * El año de lanzamiento de la canción.
     */
    private int anio;

    /**
     * La duración de la canción en minutos.
     */
    private double duracion;

    /**
     * Constructor que crea una nueva instancia de Cancion con los datos especificados.
     *
     * @param codigo        El código de la canción.
     * @param nombreCancion El nombre de la canción.
     * @param nombreAlbum   El nombre del álbum al que pertenece la canción.
     * @param caratula      La URL de la carátula del álbum.
     * @param genero        El género musical de la canción.
     * @param url           La URL de la canción en línea.
     * @param artistas      Los artistas asociados a la canción.
     * @param anio          El año de lanzamiento de la canción.
     * @param duracion      La duración de la canción en minutos.
     */
    @Builder
    public Cancion(String codigo, String nombreCancion, String nombreAlbum, String caratula, String genero, String url, String artistas, int anio, double duracion) {
        this.codigo = codigo;
        this.nombreCancion = nombreCancion;
        this.nombreAlbum = nombreAlbum;
        this.caratula = caratula;
        this.genero = genero;
        this.url = url;
        this.artistas = artistas;
        this.anio = anio;
        this.duracion = duracion;
    }

    /**
     * Constructor por defecto.
     */
    public Cancion() {
    }
}