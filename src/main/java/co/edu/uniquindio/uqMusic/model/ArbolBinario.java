package co.edu.uniquindio.uqMusic.model;

import java.io.Serializable;

public class ArbolBinario implements Serializable {

    NodoArbol inicio;

    public ArbolBinario (){
        this.inicio = null;
    }
    public void insertar(Autor autor){
        if(inicio == null){
            inicio.insertarAutor(autor);
        }else{
            this.inicio.insertarAutor(autor);
        }
    }
}
