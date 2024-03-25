package co.edu.uniquindio.uqMusic.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Autor {
    private String codigo, nombre,nacionalidad;
    private boolean esGrupo;
    private ListaDoblementeEnlazada<Cancion> listaCanciones = new ListaDoblementeEnlazada<Cancion>();

    public Autor(String codigo, String nombre, String nacionalidad, boolean esGrupo, ListaDoblementeEnlazada<Cancion> listaCanciones) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.esGrupo = esGrupo;
        this.listaCanciones = listaCanciones;
    }
}
