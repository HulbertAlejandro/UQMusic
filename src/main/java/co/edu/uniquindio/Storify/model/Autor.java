package co.edu.uniquindio.Storify.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * La clase Autor representa a un artista musical.
 * Implementa la interfaz Serializable para permitir la serialización de objetos.
 */
@Setter
@Getter
@Builder
public class Autor implements Serializable {
    /**
     * El código único del autor.
     */
    private String codigo;

    /**
     * El nombre del autor.
     */
    private String nombre;

    /**
     * La nacionalidad del autor.
     */
    private String nacionalidad;

    /**
     * Indica si el autor es un grupo musical.
     */
    private boolean esGrupo;

    /**
     * La lista de canciones asociadas al autor.
     */
    private ListaDoblementeEnlazada<Cancion> listaCanciones;

    /**
     * Constructor que crea un nuevo autor con los datos especificados.
     *
     * @param codigo         El código del autor.
     * @param nombre         El nombre del autor.
     * @param nacionalidad   La nacionalidad del autor.
     * @param esGrupo        Indica si el autor es un grupo musical.
     * @param listaCanciones La lista de canciones asociadas al autor.
     */
    @Builder
    public Autor(String codigo, String nombre, String nacionalidad, boolean esGrupo, ListaDoblementeEnlazada<Cancion> listaCanciones) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.esGrupo = esGrupo;
        this.listaCanciones = listaCanciones;
    }

    /**
     * Constructor por defecto.
     */
    public Autor() {
    }

    /**
     * Devuelve una representación en formato de cadena de texto del autor.
     *
     * @return Una cadena de texto con la información del autor.
     */
    @Override
    public String toString() {
        String sb = "Artista: " + nombre + "\n" +
                "Nacionalidad: " + nacionalidad + "\n" +
                "Es Grupo: " + (esGrupo ? "Si" : "No") + "\n" +
                "Canciones:\n";
        return sb;
    }

}
