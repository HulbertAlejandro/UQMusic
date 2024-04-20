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
        if (nodo == null){
            return false;
        }
        if (codigo.equals(nodo.getAutor().getCodigo())){
            return true;
        }
        else{
            return buscar(nodo.getNodoIzquierda(),codigo) || buscar(nodo.getNodoDerecha(),codigo);
        }
    }
    public boolean agregarAtributo(NodoArbol nodo, String codigo, Cancion cancion) {
        if (nodo == null) {
            return false;
        }
        // Comparar el código del autor del nodo actual con el código proporcionado
        int comparacion = codigo.compareTo(nodo.getAutor().getNombre());

        if (comparacion == 0) {
            System.out.println("===" + nodo.getAutor().getNombre());
            // Agregar la canción al autor encontrado
            nodo.getAutor().getListaCanciones().añadirFinal(cancion);
            return true;
        } else if (comparacion < 0) {
            System.out.println("<" + nodo.getAutor().getNombre());
            // Si el código proporcionado es menor, buscar en el subárbol izquierdo
            return agregarAtributo(nodo.getNodoIzquierda(), codigo, cancion);
        } else {
            System.out.println(">" + nodo.getAutor().getNombre());
            // Si el código proporcionado es mayor, buscar en el subárbol derecho
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

    public ArrayList<Cancion> recorridoCanciones(NodoArbol nodo, ArrayList<Cancion> lista) {
        if (nodo != null) {
            recorridoCanciones(nodo.getNodoIzquierda(), lista);
            lista.addAll(nodo.getAutor().getListaCanciones().toArrayList());
            recorridoCanciones(nodo.getNodoDerecha(), lista);
        }
        return lista;
    }
    /**
     * Busca un autor en el árbol binario por su nombre.
     *
     * @param nombre El nombre del autor a buscar.
     * @return Una lista de canciones del autor si se encuentra, o null si no se encuentra.
     */

    public ArrayList<Cancion> buscarAutor(String nombre) {
        return buscarAutor(inicio, nombre);
    }

    /**
     * Método auxiliar para buscar un autor en el árbol binario.
     *
     * @param nodo   El nodo actual a evaluar.
     * @param nombre El nombre del autor a buscar.
     * @return Una lista de canciones del autor si se encuentra, o null si no se encuentra.
     */
    private ArrayList<Cancion> buscarAutor(NodoArbol nodo, String nombre) {
        if (nodo == null)
            return null;

        int comparacion = nombre.compareTo(nodo.getAutor().getNombre());

        if (comparacion == 0)
            return nodo.getAutor().getListaCanciones().toArrayList(); // Devuelve la lista de canciones del autor encontrado
        else if (comparacion < 0)
            return buscarAutor(nodo.getNodoIzquierda(), nombre);
        else
            return buscarAutor(nodo.getNodoDerecha(), nombre);
    }
}

