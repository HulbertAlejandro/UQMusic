package co.edu.uniquindio.Storify.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ListaCircular<T> implements Serializable {
    private Nodo<T> inicio;
    private Nodo<T> fin;
    private int tamaño;

    // Constructor
    public ListaCircular() {
        inicio = null;
        fin = null;
        tamaño = 0;
    }

    // Método para agregar un elemento al final de la lista circular
    public void agregar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        if (inicio == null) {
            inicio = nuevoNodo;
            fin = nuevoNodo;
            inicio.setSiguiente(fin); // Establecer el siguiente del inicio como el fin
        } else {
            fin.setSiguiente(nuevoNodo); // Establecer el siguiente del nodo fin como el nuevo nodo
            nuevoNodo.setSiguiente(inicio); // Establecer el siguiente del nuevo nodo como el inicio
            fin = nuevoNodo; // Actualizar el nodo fin
        }
        tamaño++;
    }

    // Método para convertir la lista circular en un ArrayList
    public ArrayList<T> toArrayList() {
        ArrayList<T> arrayList = new ArrayList<>();
        if (inicio != null) {
            Nodo<T> actual = inicio;
            do {
                arrayList.add(actual.getElemento());
                actual = actual.getSiguiente();
            } while (actual != inicio);
        }
        return arrayList;
    }

    public void eliminar(T selectedItem) {
        if (inicio != null) {
            Nodo<T> nodoActual = inicio;
            Nodo<T> nodoAnterior = fin; // Nodo anterior al actual, inicialmente se establece en el último nodo

            // Si el nodo a eliminar es el nodo inicio
            if (inicio.getElemento().equals(selectedItem)) {
                if (inicio == fin) {
                    inicio = null;
                    fin = null;
                } else {
                    inicio = inicio.getSiguiente(); // Mover el inicio al siguiente nodo
                    fin.setSiguiente(inicio); // Actualizar el enlace del último nodo al nuevo inicio
                }
                tamaño--; // Reducir el tamaño de la lista
                return; // Salir del método después de eliminar el nodo
            }

            // Buscar el nodo que contiene el elemento a eliminar
            do {
                if (nodoActual.getElemento().equals(selectedItem)) {
                    // Si el nodo a eliminar es el nodo fin
                    if (nodoActual == fin) {
                        nodoAnterior.setSiguiente(inicio); // Actualizar el enlace del nodo anterior para apuntar al inicio
                        fin = nodoAnterior; // Actualizar el nodo fin al nodo anterior
                    } else {
                        nodoAnterior.setSiguiente(nodoActual.getSiguiente());
                    }
                    tamaño--; // Reducir el tamaño de la lista
                    return; // Salir del método después de eliminar el nodo
                }
                // Mover al siguiente nodo
                nodoAnterior = nodoActual;
                nodoActual = nodoActual.getSiguiente();
            } while (nodoActual != inicio); // Continuar hasta que volvamos al inicio de la lista
        }
    }
}