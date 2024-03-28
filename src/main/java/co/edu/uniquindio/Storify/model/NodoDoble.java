package co.edu.uniquindio.Storify.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * La clase NodoDoble representa un nodo en una lista doblemente enlazada.
 *
 * @param <T> El tipo de datos almacenado en el nodo.
 */
@Setter
@Getter
public class NodoDoble<T> implements Serializable {

    /**
     * El valor almacenado en el nodo.
     */
    private T valor;

    /**
     * El siguiente nodo en la lista.
     */
    private NodoDoble<T> siguiente;

    /**
     * El nodo anterior en la lista.
     */
    private NodoDoble<T> anterior;

    /**
     * Constructor que crea un nuevo nodo con el valor especificado.
     *
     * @param valor El valor que se almacenará en el nodo.
     */
    public NodoDoble(T valor) {
        this.valor = valor;
        this.anterior = null;
        this.siguiente = null;
    }
}