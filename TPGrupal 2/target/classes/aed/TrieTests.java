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

    @BeforeEach
    void init() {
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
        
        Method buscarMethod = trieTest.getClass().getMethod("buscar", String.class);
        assertTrue((boolean) buscarMethod.invoke(trieTest, "amor"));
        assertFalse((boolean) buscarMethod.invoke(trieTest, "amora"));
        assertFalse((boolean) buscarMethod.invoke(trieTest, "amores"));
        assertFalse((boolean) buscarMethod.invoke(trieTest, "amoroso"));
        assertTrue((boolean) buscarMethod.invoke(trieTest, "amoblado"));
    }
}

