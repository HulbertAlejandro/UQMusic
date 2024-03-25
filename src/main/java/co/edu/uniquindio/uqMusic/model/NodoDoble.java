package co.edu.uniquindio.uqMusic.model;

import lombok.Setter;
import lombok.Getter;
@Setter @Getter
public class NodoDoble <T>{

    private T valor;
    private NodoDoble<T> siguiente;
    private NodoDoble<T> anterior;

    public NodoDoble(T valor) {
        this.valor = valor;
        this.anterior = null;
        this.siguiente = null;
    }

}
