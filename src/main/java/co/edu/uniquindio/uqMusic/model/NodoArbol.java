package co.edu.uniquindio.uqMusic.model;

import lombok.*;

import java.io.Serializable;
@Getter
@Setter
public class NodoArbol implements Serializable {
    private Autor autor ;
    private NodoArbol nodoIzquierda;
    private NodoArbol nodoDerecha;

    public NodoArbol(Autor autor){
        this.autor =  autor;
        this.nodoDerecha = null;
        this.nodoIzquierda = null;
    }

    public void insertarAutor(Autor autor){
        if(compararNombres(autor.getNombre(), this.getAutor().getNombre()) < 0){
            if(this.nodoIzquierda == null){
                this.nodoIzquierda = new NodoArbol(autor);
            }else{
                this.nodoIzquierda.insertarAutor(autor);
            }
        }if (compararNombres(autor.getNombre(), this.getAutor().getNombre()) > 0){
            if(this.nodoDerecha == null){
                this.nodoDerecha = new NodoArbol(autor);
            }else{
                this.nodoDerecha.insertarAutor(autor);
            }
        }
    }

    public static int compararNombres(String nombre1, String nombre2) {
        return nombre1.compareToIgnoreCase(nombre2);
    }
}
