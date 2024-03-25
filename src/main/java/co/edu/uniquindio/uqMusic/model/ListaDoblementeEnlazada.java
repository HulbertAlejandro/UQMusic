package co.edu.uniquindio.uqMusic.model;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ListaDoblementeEnlazada <T>{

    private NodoDoble<T> nodoPrimero;
    private NodoDoble<T> nodoUltimo;
    int tamano;

    public ListaDoblementeEnlazada() {
        this.nodoPrimero = null;
        this.nodoUltimo = null;
        tamano = 0;
    }
    public void añadirInicio(T dato) {
        NodoDoble<T> nodo = new NodoDoble<>(dato);
        if (estaVacia()) {
            nodoPrimero = nodo;
            nodoUltimo = nodo;
        } else {
            nodo.setSiguiente(nodoPrimero);
            nodoPrimero.setAnterior(nodo); // Establecer el anterior del primer nodo al nuevo nodo
            nodoPrimero = nodo;
        }
        tamano++;
    }

    public void añadirFinal(T dato) {
        NodoDoble<T> nodo = new NodoDoble<>(dato);
        if (estaVacia()) {
            nodoPrimero = nodo;
            nodoUltimo = nodo;
        } else {
            nodoUltimo.setSiguiente(nodo);
            nodo.setAnterior(nodoUltimo); // Establecer el anterior del nuevo nodo al nodoUltimo
            nodoUltimo = nodo;
        }
        tamano++;
    }
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

    public void imprimirLista() {
        NodoDoble<T> actual = nodoPrimero;
        while (actual != null) {
            System.out.print(actual.getValor() + " ");
            actual = actual.getSiguiente();
            System.out.println();
        }
    }
    public boolean estaVacia(){
        return nodoPrimero == null;
    }

}
