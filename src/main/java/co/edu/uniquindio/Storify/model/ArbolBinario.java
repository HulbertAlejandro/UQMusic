package co.edu.uniquindio.Storify.model;

import java.io.Serializable;

/**
 * La clase ArbolBinario representa un árbol binario de búsqueda que almacena autores.
 * Implementa la interfaz Serializable para permitir la serialización de objetos.
 */
public class ArbolBinario implements Serializable {

    /**
     * El nodo raíz del árbol binario.
     */
    private NodoArbol inicio;

    /**
     * Constructor que crea un nuevo árbol binario vacío.
     */
    public ArbolBinario() {
        this.inicio = null;
    }

    /**
     * Inserta un autor en el árbol binario.
     *
     * @param autor El autor que se va a insertar en el árbol.
     */
    public void insertar(Autor autor) {
        if (inicio == null) {
            inicio = new NodoArbol(autor);
        } else {
            inicio.insertarAutor(autor);
        }
    }
}

