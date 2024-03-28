package co.edu.uniquindio.Storify.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * La clase NodoArbol representa un nodo en un árbol binario de búsqueda de autores.
 * Cada nodo contiene un autor y referencias a los nodos hijo izquierdo y derecho.
 */
@Getter
@Setter
public class NodoArbol implements Serializable {
    /**
     * El autor almacenado en este nodo.
     */
    private Autor autor;

    /**
     * El nodo hijo izquierdo.
     */
    private NodoArbol nodoIzquierda;

    /**
     * El nodo hijo derecho.
     */
    private NodoArbol nodoDerecha;

    /**
     * Constructor que crea un nuevo nodo con el autor especificado.
     *
     * @param autor El autor almacenado en este nodo.
     */
    public NodoArbol(Autor autor) {
        this.autor = autor;
        this.nodoDerecha = null;
        this.nodoIzquierda = null;
    }

    /**
     * Inserta un nuevo autor en el árbol binario de búsqueda.
     *
     * @param autor El autor que se va a insertar.
     */
    public void insertarAutor(Autor autor) {
        if (compararNombres(autor.getNombre(), this.getAutor().getNombre()) < 0) {
            if (this.nodoIzquierda == null) {
                this.nodoIzquierda = new NodoArbol(autor);
            } else {
                this.nodoIzquierda.insertarAutor(autor);
            }
        }
        if (compararNombres(autor.getNombre(), this.getAutor().getNombre()) > 0) {
            if (this.nodoDerecha == null) {
                this.nodoDerecha = new NodoArbol(autor);
            } else {
                this.nodoDerecha.insertarAutor(autor);
            }
        }
    }

    /**
     * Compara dos nombres de autor y devuelve un valor que indica su orden lexicográfico.
     *
     * @param nombre1 El primer nombre a comparar.
     * @param nombre2 El segundo nombre a comparar.
     * @return Un entero negativo si nombre1 es menor que nombre2, cero si son iguales,
     * o un entero positivo si nombre1 es mayor que nombre2.
     */
    public static int compararNombres(String nombre1, String nombre2) {
        return nombre1.compareToIgnoreCase(nombre2);
    }
}