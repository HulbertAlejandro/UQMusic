package co.edu.uniquindio.Storify.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * La clase ArbolBinario representa un árbol binario de búsqueda que almacena autores.
 * Implementa la interfaz Serializable para permitir la serialización de objetos.
 */
@Getter
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
    public void recorridoEnOrden(NodoArbol nodo, int nivel) {
        if (nodo != null) {
            recorridoEnOrden(nodo.getNodoDerecha(), nivel + 1);
            for (int i = 0; i < nivel; i++) {
                System.out.print("\t");
            }
            System.out.println(nodo.getAutor().getNombre());
            recorridoEnOrden(nodo.getNodoIzquierda(), nivel + 1);
        }
    }

    public boolean buscarCodigo(String codigo) {
        return buscar(inicio, codigo);
    }
    private boolean buscar(NodoArbol nodo, String codigo) {
        if (nodo == null)
            return false;
        int comparacion = codigo.compareTo(nodo.getAutor().getCodigo());
        if (comparacion == 0)
            return true;
        else if (comparacion < 0)
            return buscar(nodo.getNodoIzquierda(), codigo);
        else
            return buscar(nodo.getNodoDerecha(), codigo);
    }
    public boolean agregarAtributo(NodoArbol nodo, String codigo, Cancion cancion) {
        if (nodo == null) {
            return false;
        }
        int comparacion = codigo.compareTo(nodo.getAutor().getCodigo());
        if (comparacion == 0) {
            nodo.getAutor().getListaCanciones().añadirFinal(cancion);
            return true;
        } else if (comparacion < 0) {
            return agregarAtributo(nodo.getNodoIzquierda(), codigo, cancion);
        } else {
            return agregarAtributo(nodo.getNodoDerecha(), codigo, cancion);
        }
    }
    public ArrayList<Autor> toList() {
        ArrayList<Autor> lista = new ArrayList<>();
        recorridoEnOrden(inicio, lista);
        return lista;
    }

    private void recorridoEnOrden(NodoArbol nodo, ArrayList<Autor> lista) {
        if (nodo != null) {
            recorridoEnOrden(nodo.getNodoIzquierda(), lista);
            lista.add(nodo.getAutor());
            recorridoEnOrden(nodo.getNodoDerecha(), lista);
        }
    }
}

