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

    /**
     * Realiza un recorrido en orden sobre un árbol binario de búsqueda, imprimiendo los autores en orden alfabético.
     * Comienza el recorrido desde el nodo dado y avanza recursivamente hacia los nodos izquierdo y derecho.
     * Imprime el nombre del autor en cada nodo visitado, indentando según el nivel en el árbol.
     *
     * @param nodo El nodo desde el cual comenzar el recorrido en orden.
     * @param nivel El nivel actual en el árbol, usado para determinar la cantidad de indentación.
     */
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

    /**
     * Busca un autor en el árbol binario de búsqueda utilizando su código.
     * Comienza la búsqueda desde la raíz del árbol.
     *
     * @param codigo El código del autor a buscar.
     * @return true si se encuentra el autor con el código dado, false de lo contrario.
     */
    public boolean buscarCodigo(String codigo) {
        return buscar(inicio, codigo);
    }

    /**
     * Realiza una búsqueda en el árbol binario de búsqueda para encontrar un autor con el código dado.
     * Comienza la búsqueda desde el nodo dado y avanza recursivamente hacia los nodos izquierdo y derecho.
     *
     * @param nodo El nodo desde el cual comenzar la búsqueda.
     * @param codigo El código del autor a buscar.
     * @return true si se encuentra el autor con el código dado, false de lo contrario.
     */
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

    /**
     * Agrega una canción al autor correspondiente en el árbol binario de búsqueda, utilizando el código del autor.
     * Comienza la búsqueda desde el nodo dado y avanza recursivamente hacia los nodos izquierdo y derecho.
     *
     * @param nodo El nodo desde el cual comenzar la búsqueda para agregar la canción.
     * @param codigo El código del autor al que se le va a agregar la canción.
     * @param cancion La canción que se va a agregar al autor.
     * @return true si se agrega la canción al autor correspondiente, false si el autor no se encuentra en el árbol.
     */
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

    /**
     * Para el arraylist de autores a una lista y la recorre en orden
     * @return lista
     */
    public ArrayList<Autor> toList() {
        ArrayList<Autor> lista = new ArrayList<>();
        recorridoEnOrden(inicio, lista);
        return lista;
    }

    /**
     * Realiza un recorrido en orden en el árbol binario de búsqueda y agrega los autores encontrados en una lista dada.
     * Comienza el recorrido desde el nodo dado y avanza recursivamente hacia los nodos izquierdo y derecho.
     *
     * @param nodo El nodo desde el cual comenzar el recorrido en orden.
     * @param lista La lista donde se van a agregar los autores en orden.
     */
    private void recorridoEnOrden(NodoArbol nodo, ArrayList<Autor> lista) {
        if (nodo != null) {
            recorridoEnOrden(nodo.getNodoIzquierda(), lista);
            lista.add(nodo.getAutor());
            recorridoEnOrden(nodo.getNodoDerecha(), lista);
        }
    }

    /**
     * Realiza un recorrido en orden en el árbol binario de búsqueda y agrega todas las canciones de los autores encontrados en una lista dada.
     * Comienza el recorrido desde el nodo dado y avanza recursivamente hacia los nodos izquierdo y derecho.
     *
     * @param nodo El nodo desde el cual comenzar el recorrido en orden.
     * @param lista La lista donde se van a agregar todas las canciones de los autores en orden.
     * @return Una lista que contiene todas las canciones de los autores encontrados en el árbol.
     */
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

