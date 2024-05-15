package co.edu.uniquindio.Storify.model;

/**
 * Esta clase representa una pila (stack) implementada utilizando nodos enlazados.
 */
public class Pila<T> {

    private NodoPila<T> cabeza;
    private int tamaño;

    /**
     * Constructor para crear una pila vacía.
     */
    public Pila() {
        this.cabeza = null;
        this.tamaño = 0;
    }

    /**
     * Agrega un elemento a la pila.
     *
     * @param elemento El elemento a ser agregado.
     */
    public void push(T elemento) {
        NodoPila<T> nuevoNodo = new NodoPila<>(elemento);
        nuevoNodo.setSiguiente(cabeza);
        cabeza = nuevoNodo;
        tamaño++;
    }

    /**
     * Elimina y devuelve el elemento en la cima de la pila.
     *
     * @return El elemento en la cima de la pila.
     * @throws IllegalStateException Si la pila está vacía.
     */
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía");
        }
        T elemento = cabeza.getValor();
        cabeza = cabeza.getSiguiente();
        tamaño--;
        return elemento;
    }

    /**
     * Obtiene el elemento en la cima de la pila sin eliminarlo.
     *
     * @return El elemento en la cima de la pila.
     * @throws IllegalStateException Si la pila está vacía.
     */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía");
        }
        return cabeza.getValor();
    }

    /**
     * Verifica si la pila está vacía.
     *
     * @return true si la pila está vacía, false de lo contrario.
     */
    public boolean isEmpty() {
        return tamaño == 0;
    }
}
