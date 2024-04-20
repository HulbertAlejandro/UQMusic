package co.edu.uniquindio.Storify.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CancionTest {

    @Test
    void coincideAtributo() {
        // Crear una canción para realizar las pruebas
        Cancion cancion = new Cancion("1", "Cancion1", "Album1",
                "caratula1", "Rock", "url1", "Artista1", 2022, 3.5);

        // Verificar si el nombre de la canción coincide con un atributo dado
        assertTrue(cancion.coincideAtributo("Cancion1"),
                "El nombre de la canción debería coincidir con el atributo");
        assertFalse(cancion.coincideAtributo("OtroNombre"),
                "El nombre de la canción no debería coincidir con el atributo");

        // Verificar si el nombre del álbum coincide con un atributo dado
        assertTrue(cancion.coincideAtributo("Album1"),
                "El nombre del álbum debería coincidir con el atributo");
        assertFalse(cancion.coincideAtributo("OtroAlbum"),
                "El nombre del álbum no debería coincidir con el atributo");

        // Verificar si el género coincide con un atributo dado
        assertTrue(cancion.coincideAtributo("Rock"),
                "El género debería coincidir con el atributo");
        assertFalse(cancion.coincideAtributo("OtroGenero"),
                "El género no debería coincidir con el atributo");

        // Verificar si el nombre de los artistas coincide con un atributo dado
        assertTrue(cancion.coincideAtributo("Artista1"),
                "El nombre de los artistas debería coincidir con el atributo");
        assertFalse(cancion.coincideAtributo("OtroArtista"),
                "El nombre de los artistas no debería coincidir con el atributo");
    }

}