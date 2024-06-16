package aed;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrieTests {
    Trie<Integer> trieTest;
    // Dato: defini el trie como tipo Integer porque java xigue un tipo asi, perop en realidad es tipo T 
    // y cada nodo puede tener cualquier tipo de valor, no se si es bug o feature pero por ahora es asi

    @BeforeEach
    void init() {
        // Este método reinicia los valores de las variables antes de cada test
        trieTest = new Trie<>();
    }

    @Test
    void testInsertar() throws Exception {
        Method insertarMethod = trieTest.getClass().getMethod("insertar", String.class, Object.class);
        insertarMethod.invoke(trieTest, "amor", 1);
        
        Method buscarMethod = trieTest.getClass().getMethod("buscar", String.class);
        boolean value = (boolean) buscarMethod.invoke(trieTest, "amor");
        
        assertTrue(value);
    }

    @Test
    void testBuscar() throws Exception {
        Method insertarMethod = trieTest.getClass().getMethod("insertar", String.class, Object.class);
        insertarMethod.invoke(trieTest, "amor", 1);
        insertarMethod.invoke(trieTest, "amoblado", 5);
        //pruebo variaciones de las claves para ver que no identifique valores de otras claves como propios y de valores erroneos
        insertarMethod.invoke(trieTest, "ghora", 1);
        insertarMethod.invoke(trieTest, "hora", 10);
        insertarMethod.invoke(trieTest, "horario", 100);


        Method buscarMethod = trieTest.getClass().getMethod("buscar", String.class);
        assertTrue((boolean) buscarMethod.invoke(trieTest, "amor"));
        assertFalse((boolean) buscarMethod.invoke(trieTest, "amo"));
        assertFalse((boolean) buscarMethod.invoke(trieTest, "amora"));
        assertFalse((boolean) buscarMethod.invoke(trieTest, "amorr"));
        assertFalse((boolean) buscarMethod.invoke(trieTest, "amores"));
        assertFalse((boolean) buscarMethod.invoke(trieTest, "amoroso"));
        assertTrue((boolean) buscarMethod.invoke(trieTest, "amoblado"));
        assertFalse((boolean) buscarMethod.invoke(trieTest, "amoblad"));
        assertFalse((boolean) buscarMethod.invoke(trieTest, "amobla"));
        //test contenidos:
        assertTrue((boolean) buscarMethod.invoke(trieTest, "ghora"));
        assertTrue((boolean) buscarMethod.invoke(trieTest, "hora"));
        assertTrue((boolean) buscarMethod.invoke(trieTest, "horario"));
    }

    @Test
    void testObtenerSignificado() throws Exception {
        Method insertarMethod = trieTest.getClass().getMethod("insertar", String.class, Object.class);
        insertarMethod.invoke(trieTest, "amor", 1);
        insertarMethod.invoke(trieTest, "amoblado", 5);
        insertarMethod.invoke(trieTest, "hola", "palos"); //banca cualquiero tipo
        
        Method ObtenerSignificadoMethod = trieTest.getClass().getMethod("obtenerSignificado", String.class);
        assertEquals(1, ObtenerSignificadoMethod.invoke(trieTest, "amor"));
        assertEquals(5, ObtenerSignificadoMethod.invoke(trieTest, "amoblado"));
        assertEquals("palos", ObtenerSignificadoMethod.invoke(trieTest, "hola")); //banca cualquiero tipo
    }

    @Test
    void testEliminar() throws Exception {
        Method insertarMethod = trieTest.getClass().getMethod("insertar", String.class, Object.class);
        insertarMethod.invoke(trieTest, "amor", 1);
        insertarMethod.invoke(trieTest, "orga", 5);
        //pruebo que las claves contenidas no se pisen
        insertarMethod.invoke(trieTest, "hora", 100);
        insertarMethod.invoke(trieTest, "horario", 100);
        
        Method buscarMethod = trieTest.getClass().getMethod("buscar", String.class);
        assertTrue((boolean) buscarMethod.invoke(trieTest, "amor"));
        assertTrue((boolean) buscarMethod.invoke(trieTest, "orga"));
        assertTrue((boolean) buscarMethod.invoke(trieTest, "hora"));
        assertTrue((boolean) buscarMethod.invoke(trieTest, "horario"));
        
        Method eliminarMethod = trieTest.getClass().getMethod("eliminar", String.class);
        eliminarMethod.invoke(trieTest, "amor");
        eliminarMethod.invoke(trieTest, "orga");
        //elimino la clave que "contiene" a la otra horario -> hora || tambien lo probe al reves y funciona
        eliminarMethod.invoke(trieTest, "horario");
        
        assertFalse((boolean) buscarMethod.invoke(trieTest, "amor"));
        assertFalse((boolean) buscarMethod.invoke(trieTest, "orga"));
        assertFalse((boolean) buscarMethod.invoke(trieTest, "horario")); // este deberia ser falso porque fue eliminada
        assertTrue((boolean) buscarMethod.invoke(trieTest, "hora")); // esta es la clave "contenida" que deberia tirar true porque no fue eliminada
    }

    @Test
    void testObtenerClaves() throws Exception {
        Method insertarMethod = trieTest.getClass().getMethod("insertar", String.class, Object.class);
        insertarMethod.invoke(trieTest, "amor", 1); //1
        insertarMethod.invoke(trieTest, "amar", 2); //2
        insertarMethod.invoke(trieTest, "amigo", 3); //3
        insertarMethod.invoke(trieTest, "orga", 10); //4
        insertarMethod.invoke(trieTest, "amiga", 4); //5
        insertarMethod.invoke(trieTest, "amo", 5); //6
        insertarMethod.invoke(trieTest, "am", 6); ///7
        insertarMethod.invoke(trieTest, "horario", 6); //8 claves

        Method clavesMethod = trieTest.getClass().getMethod("claves"); //pido el metodo claves para obtener todas las claves
        @SuppressWarnings("unchecked") //java jode con el tipo generico
        List<String> claves = (List<String>) clavesMethod.invoke(trieTest); //lista con un dump de todas las claves en el trie

        assertNotNull(claves);
        assertEquals(8, claves.size()); //chequea a cantidad del claves insertadas
        //veo que claves esten en la lista
        assertTrue(claves.contains("amor"));
        assertTrue(claves.contains("amar"));
        assertTrue(claves.contains("amigo"));
        assertTrue(claves.contains("amiga"));
        assertTrue(claves.contains("horario"));
        assertTrue(claves.contains("orga"));
        assertTrue(claves.contains("amo"));
        assertTrue(claves.contains("am"));
        assertFalse(claves.contains("hora")); //la clave "hora" no existe en el trie
    }
}