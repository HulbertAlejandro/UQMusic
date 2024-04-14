package co.edu.uniquindio.Storify.model;

import javafx.fxml.FXML;
import lombok.AllArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
public class Nodo<T> implements Serializable {
    private T elemento;
    private Nodo<T> siguiente;

    public Nodo(T elemento) {
        this.elemento = elemento;
        siguiente = null;
    }

    public T getElemento() {
        return elemento;
    }

    public void setElemento(T elemento) {
        this.elemento = elemento;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}