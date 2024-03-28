package co.edu.uniquindio.Storify.exceptions;

/**
 * La clase CampoRepetido representa una excepción lanzada cuando se intenta agregar un campo repetido
 * en una estructura de datos que no permite duplicados.
 */
public class CampoRepetido extends Exception {

    /**
     * Constructor que crea una nueva instancia de CampoRepetido con el mensaje especificado.
     *
     * @param mensaje El mensaje que describe la causa de la excepción.
     */
    public CampoRepetido(String mensaje) {
        super(mensaje);
    }
}