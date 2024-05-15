package co.edu.uniquindio.Storify.model;

/**
 * Esta clase representa un nodo de una pila (stack) implementada utilizando nodos enlazados.
 */
public class NodoPila<T> {
    private T valor;
    private NodoPila<T> siguiente;

    /**
     * Constructor para crear un nuevo nodo con un valor dado.
     * @param valor El valor del nodo.
     */
    public NodoPila(T valor) {
        this.valor = valor;
        this.siguiente = null;
    }

    /**
     * Obtiene el valor almacenado en el nodo.
     * @return El valor almacenado en el nodo.
     */
    public T getValor() {
        return valor;
    }

    /**
     * Obtiene el siguiente nodo enlazado.
     * @return El siguiente nodo enlazado.
     */
    public NodoPila<T> getSiguiente() {
        return siguiente;
    }

    /**
     * Establece el siguiente nodo enlazado.
     * @param siguiente El siguiente nodo enlazado.
     */
    public void setSiguiente(NodoPila<T> siguiente) {
        this.siguiente = siguiente;
    }
}
