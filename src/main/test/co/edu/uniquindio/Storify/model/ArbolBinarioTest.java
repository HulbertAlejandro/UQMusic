package co.edu.uniquindio.Storify.model;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ArbolBinarioTest {

    @Test
    void insertar() {
        // Crear el árbol binario
        ArbolBinario arbol = new ArbolBinario();

        //Crear lista enlazada vacía
        ListaDoblementeEnlazada<Cancion> listaCanciones = new ListaDoblementeEnlazada<>();
        // Crear algunos autores para usar en las pruebas
        Autor autor1 = new Autor("1","A","Colombia",false,listaCanciones);
        Autor autor2 = new Autor("2","B","Colombia",false,listaCanciones);
        Autor autor3 = new Autor("3","C","Colombia",false,listaCanciones);

        // Insertar un autor en un árbol vacío
        arbol.insertar(autor1);
        assertEquals(autor1,
                arbol.getInicio().getAutor(), "El autor no se insertó correctamente en un árbol vacío");

        // Insertar otro autor en el mismo árbol y devolver su lista de canciones (en este caso null)

        arbol.insertar(autor2);
        assertEquals(autor2.getListaCanciones().toArrayList(),
                arbol.buscarAutor(autor2.getNombre()), "El autor no se insertó correctamente");
    }

    @Test
    void recorridoEnOrden() {
        // Crear el árbol binario
        ArbolBinario arbol = new ArbolBinario();

        // Crear algunos autores para usar en las pruebas
        ListaDoblementeEnlazada<Cancion> listaCanciones = new ListaDoblementeEnlazada<>();
        Autor autor1 = new Autor("1", "A", "Colombia", false, listaCanciones);
        Autor autor2 = new Autor("2", "B", "Argentina", true, listaCanciones);
        Autor autor3 = new Autor("3", "C", "Brasil", false, listaCanciones);
        Autor autor4 = new Autor("4", "D", "Chile", true, listaCanciones);

        // Insertar autores en el árbol
        arbol.insertar(autor2);
        arbol.insertar(autor1);
        arbol.insertar(autor3);
        arbol.insertar(autor4);

        // Redirigir la salida estándar a un ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Llamar al método recorridoEnOrden
        arbol.recorridoEnOrden(arbol.getInicio(), 0);

        // Restaurar la salida estándar
        System.setOut(System.out);

        // Obtener la salida como una cadena
        String output = outputStream.toString().trim();

        // Verificar que la salida contiene los autores en el orden correcto
        assertTrue(output.contains(autor1.getNombre()), "El primer autor no está en el orden correcto");
        assertTrue(output.contains(autor2.getNombre()), "El segundo autor no está en el orden correcto");
        assertTrue(output.contains(autor3.getNombre()), "El tercer autor no está en el orden correcto");
        assertTrue(output.contains(autor4.getNombre()), "El cuarto autor no está en el orden correcto");
    }

    @Test
    void buscarCodigo() {
        // Crear el árbol binario
        ArbolBinario arbol = new ArbolBinario();

        // Crear algunos autores para usar en las pruebas
        Autor autor1 = new Autor("1", "A", "Colombia", false, null);
        Autor autor2 = new Autor("2", "B", "Argentina", true, null);
        Autor autor3 = new Autor("3", "C", "Brasil", false, null);
        Autor autor4 = new Autor("4", "D", "Chile", true, null);

        // Insertar autores en el árbol
        arbol.insertar(autor2);
        arbol.insertar(autor1);
        arbol.insertar(autor3);
        arbol.insertar(autor4);

        // Buscar un código que existe en el árbol
        assertTrue(arbol.buscarCodigo("1"), "El método no encontró un código existente");

        // Buscar un código que no existe en el árbol
        assertFalse(arbol.buscarCodigo("5"), "El método encontró un código inexistente");
    }

    @Test
    void agregarAtributo() {
        // Crear el árbol binario
        ArbolBinario arbol = new ArbolBinario();

        // Crear algunos autores para usar en las pruebas
        Autor autor1 = new Autor("1", "A", "Colombia",
                false, new ListaDoblementeEnlazada<>()); // Inicializar lista de canciones
        Autor autor2 = new Autor("2", "B", "Argentina",
                true, new ListaDoblementeEnlazada<>());
        Autor autor3 = new Autor("3", "C", "Brasil",
                false, new ListaDoblementeEnlazada<>());
        Autor autor4 = new Autor("4", "D", "Chile",
                true, new ListaDoblementeEnlazada<>());

        // Insertar autores en el árbol
        arbol.insertar(autor2);
        arbol.insertar(autor1);
        arbol.insertar(autor3);
        arbol.insertar(autor4);

        // Crear una canción para agregar
        Cancion cancion = new Cancion("1", "Cancion1",
                "Album1", "url1", "Rock", "url2", null, 2022, 3.5);

        // Agregar la canción al autor existente en el árbol
        assertTrue(arbol.agregarAtributo(arbol.getInicio(), "A",
                cancion), "No se pudo agregar la canción a un autor existente");
    }

    @Test
    void recorridoCanciones() {
        // Crear el árbol binario
        ArbolBinario arbol = new ArbolBinario();

        // Crear un autor con una lista de canciones
        Autor autor1 = new Autor("1", "A", "Colombia", false, null);        ListaDoblementeEnlazada<Cancion> listaCancionesAutor = new ListaDoblementeEnlazada<>();
        Cancion cancion1 = new Cancion("1", "Cancion1", "Album1",
                "url1", "Rock", "url2", null, 2022, 3.5);
        listaCancionesAutor.añadirFinal(cancion1);
        autor1.setListaCanciones(listaCancionesAutor);

        // Insertar el autor en el árbol
        arbol.insertar(autor1);

        // Crear una lista para almacenar las canciones recorridas
        ArrayList<Cancion> cancionesRecorridas = new ArrayList<>();

        // Realizar el recorrido de canciones en el árbol
        cancionesRecorridas = arbol.recorridoCanciones(arbol.getInicio(), cancionesRecorridas);

        // Verificar que la lista de canciones recorridas tenga el tamaño correcto
        assertEquals(1, cancionesRecorridas.size(),
                "El número de canciones recorridas no es el esperado");

        // Verificar el orden de las canciones recorridas
        assertEquals("Cancion1", cancionesRecorridas.get(0).getNombreCancion(),
                "El orden de las canciones recorridas es incorrecto");
    }

    @Test
    void buscarAutor() {
        // Crear el árbol binario
        ArbolBinario arbol = new ArbolBinario();

        // Crear un autor con una lista de canciones
        ListaDoblementeEnlazada<Cancion> listaCancionesAutor = new ListaDoblementeEnlazada<>();
        Autor autor1 = new Autor("1", "A", "Colombia", false, listaCancionesAutor);

        // Insertar el autor en el árbol
        arbol.insertar(autor1);

        // Buscar un autor existente
        String nombreAutorExistente = "A";
        ArrayList<Cancion> cancionesAutorExistente = arbol.buscarAutor(nombreAutorExistente);

        // Verificar que se encuentre el autor y que la lista de canciones sea null
        assertNotNull(cancionesAutorExistente, "El autor debería existir en el árbol");
        assertTrue(cancionesAutorExistente.isEmpty(), "La lista de canciones del autor debería estar vacía");
    }

}