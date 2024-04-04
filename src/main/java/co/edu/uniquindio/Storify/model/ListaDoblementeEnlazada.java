package co.edu.uniquindio.Storify.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * La clase ListaDoblementeEnlazada representa una lista doblemente enlazada genérica.
 *
 * @param <T> El tipo de datos de los elementos de la lista.
 */
@Setter
@Getter
public class ListaDoblementeEnlazada<T> implements Serializable {

    /**
     * El primer nodo de la lista.
     */
    private NodoDoble<T> nodoPrimero;

    /**
     * El último nodo de la lista.
     */
    private NodoDoble<T> nodoUltimo;

    /**
     * El tamaño actual de la lista.
     */
    private int tamano;

    /**
     * Constructor que crea una nueva lista doblemente enlazada vacía.
     */
    public ListaDoblementeEnlazada() {
        this.nodoPrimero = null;
        this.nodoUltimo = null;
        tamano = 0;
    }
    /**
     * Agrega un nuevo elemento al inicio de la lista.
     *
     * @param dato El dato que se va a agregar.
     */
    public void añadirInicio(T dato) {
        // Crea un nuevo nodo con el dato proporcionado
        NodoDoble<T> nodo = new NodoDoble<>(dato);

        if (estaVacia()) {
            // Si la lista está vacía, el nuevo nodo será tanto el primero como el último
            nodoPrimero = nodo;
            nodoUltimo = nodo;
        } else {
            // Si la lista no está vacía, el nuevo nodo se conecta al primero y se convierte en el primero
            nodo.setSiguiente(nodoPrimero);
            nodoPrimero.setAnterior(nodo);
            nodoPrimero = nodo;
        }
        tamano++;
    }

    /**
     * Agrega un nuevo elemento al final de la lista.
     *
     * @param dato El dato que se va a agregar.
     */
    public void añadirFinal(T dato) {
        NodoDoble<T> nodo = new NodoDoble<>(dato);
        if (estaVacia()) {
            nodoPrimero = nodo;
            nodoUltimo = nodo;
        } else {
            nodoUltimo.setSiguiente(nodo);
            nodo.setAnterior(nodoUltimo);
            nodoUltimo = nodo;
        }
        tamano++;
    }

    /**
     * Busca un valor en la lista.
     *
     * @param valor El valor que se desea buscar.
     * @return true si el valor se encuentra en la lista, false en caso contrario.
     */
    public boolean buscar(T valor) {
        if (nodoPrimero == null) {
            return false;
        }

        NodoDoble<T> actual = nodoPrimero;
        do {
            if (actual.getValor() == valor) {
                return true;
            }
            actual = actual.getSiguiente();
        } while (actual != nodoPrimero);

        return false;
    }

    /**
     * Imprime los elementos de la lista.
     */
    public void imprimirLista() {
        NodoDoble<T> actual = nodoPrimero;
        while (actual != null) {
            System.out.print(actual.getValor() + " ");
            actual = actual.getSiguiente();
        }
        System.out.println();
    }
    public ArrayList<T> toArrayList() {
        ArrayList<T> arrayList = new ArrayList<>();
        NodoDoble actual = nodoPrimero;
        while (actual != null) {
            arrayList.add((T) actual.getValor());
            actual = actual.getSiguiente();
        }
        return arrayList;
    }

    /**
     * Verifica si la lista está vacía.
     *
     * @return true si la lista está vacía, false en caso contrario.
     */
    public boolean estaVacia() {
        return nodoPrimero == null;
    }
}
