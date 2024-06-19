package aed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trie<T>{
    private Nodo raiz; // Nodo raiz del Trie
    public static final int ALFABETO_LEXICOGRAFICO = 256; // alfabeto para el orden lexicografico segun el codigo ASCII

    // Clase interna del Nodo
    private class Nodo{
        private ArrayList<Nodo> hijos;
        private T valor;

        // NOTA: Los nodos cuyo valor es vacion son parte de la clave y cobran sentido al llegar la nodo significado que es el que contiene el valor de la clave
        public Nodo(T valor) {
            this.hijos = new ArrayList<>(Collections.nCopies(ALFABETO_LEXICOGRAFICO, null));
            this.valor = valor;
        }

        // Devuelve el valor del Nodo
        public T obtenerValor() {
            return valor;
        }

        // Asigna un valor al Nodo (le da signfiicado al Nodo y por lo tanto a la clave misma)
        public void asignarValor(T valor) {
            this.valor = valor;
        }
    }

    // Constructor del Trie
    public Trie() {
        this.raiz = new Nodo(null);
    }

    // Insertar una clave con su respectivo valor en el Trie
    // NOTA: si se mete una clave ya existente reemplazara el valor actual por el dado, es decir se sobreescribe.
    public void insertar(String clave, T valor) {
        Nodo actual = raiz;
        for (char c : clave.toCharArray()) { // convierte a cadena de chars
            int index = c; // pasa el char a codiog ASCII
            if (actual.hijos.get(index) == null) {
                actual.hijos.set(index, new Nodo(null)); //los nodos que no son el de siginficaod se les asigna el valor null por ahora
            }
            actual = actual.hijos.get(index);
        }
        actual.asignarValor(valor);
    }

    // Dada una clave (ya insertada en el Trie) devuelve su valor
    public T obtenerSignificado(String clave){
        Nodo actual = raiz;
        for (char c : clave.toCharArray()) {
            int index = c;
            if (actual.hijos.get(index) == null) {
                return null;
            }
            actual = actual.hijos.get(index);
        }
        // El proposito de esta guarda es identificar el caso donde el valor de la clave es nulo
        if (actual.obtenerValor() == null){
            throw new UnsupportedOperationException("el valor de esta clave es nulo (no deberia pasar)");
        }
        return actual.obtenerValor();
    }

    // Devuelve True si la clave pertence al Trie (creo que al final ni se uso asi que se lo puede sacar)
    public boolean buscar(String clave) {
        Nodo actual = raiz;
        for (char c : clave.toCharArray()) {
            int index = c;
            if (actual.hijos.get(index) == null) {
                return false;
            }
            actual = actual.hijos.get(index);
        }
        return actual.valor != null; // Si el nodo significado no es nulo, la clave esta en el Trie
    }

    // Método para eliminar una clave del Trie
    public void eliminar(String clave) {
        eliminarTrie(raiz, clave, 0); //inicializo el eliminador con el index en 0 (primera posicion de la clave)
    }

    // Funcion Auxiliar de eliminar()
    private boolean eliminarTrie(Nodo actual, String clave, int indice){
        //caso base:
        if (actual == null) {
            return false; // La clave no existe en el trie desde la raiz
        }
        //caso "borde":
        if (indice == clave.length()) { // chequea si estas en el nodo significado (si alcanzaste el largo de la clave)
            if (actual.obtenerValor() == null) {
                return false; // La clave no existe en el trie
            }
            actual.asignarValor(null); // eliminar el valor del nodo final
            return noTieneHijos(actual);
        }
        //recursion
        char ch = clave.charAt(indice); // pido el char en la posicion que marca el indice
        int index = ch; // convierte a ascii
        Nodo nodo = actual.hijos.get(index);
        // pregunto por el nodo a chequear:
        if (nodo == null) {
            return false; // La clave no existe en el trie
        }
        //si el nodo esta -> recursion
        boolean borrarNodo = eliminarTrie(nodo, clave, indice + 1);
        if (borrarNodo){
            actual.hijos.set(index, null); // nulleo la posicion del nodo y lo elimino
            return noTieneHijos(actual) && actual.obtenerValor() == null;
        }
        return false;
    }

    // Funciona como predicado para saber si el Nodo tiene hijos o no || Complejidad: 
    private boolean noTieneHijos(Nodo nodo) {
        for (Nodo hijo : nodo.hijos) {
            if (hijo != null) {
                return false;
            }
        }
        return true;
    }

    // Obtiene todas las claves del Trie y devuelve una lista con cada clave en formato String
    public List<String> claves() {
        List<String> claves = new ArrayList<>();
        obtenerClaves(raiz, new StringBuilder(), claves);
        return claves;
    }

    // Funcion Auxiliar de claves()
    private void obtenerClaves(Nodo actual, StringBuilder claveActual, List<String> claves) {
        if (actual == null) {
            return;
        }
        if (actual.obtenerValor() != null) {
            claves.add(claveActual.toString());
        }
        for (int i = 0; i < ALFABETO_LEXICOGRAFICO; i++) {
            Nodo hijo = actual.hijos.get(i);
            if (hijo != null) {
                claveActual.append((char) i);
                obtenerClaves(hijo, claveActual, claves);
                claveActual.deleteCharAt(claveActual.length() - 1);
            }
        }
    }

    // Elimina el significado de una clave (haciendo que la clave ya no sea valida)
    public void eliminarSignficado(String clave){
        Nodo actual = raiz;
        for (char c : clave.toCharArray()) {
            int index = c;
            actual = actual.hijos.get(index);
        }
        actual.asignarValor(null);
    }

    // Obtiene todas las claves del Trie, pero devuelve una lista con solo las lcaves validas (segunn al estado de la Materia)
    public List<String> claveMaterias() {
        List<String> claves = new ArrayList<>();
        obtenerClavesMaterias(raiz, new StringBuilder(), claves);
        return claves;
    }

    // Funcion Auxiliar para claveMaterias()
    private void obtenerClavesMaterias(Nodo actual, StringBuilder claveActual, List<String> claves) {
        if (actual == null) {
            return;
        }
        if (actual.obtenerValor() != null) {
            Materia materia = (Materia) actual.obtenerValor();
            if (!materia.estaCerrada()) {
                claves.add(claveActual.toString());
            }
        }
        for (int i = 0; i < ALFABETO_LEXICOGRAFICO; i++) {
            Nodo hijo = actual.hijos.get(i);
            if (hijo != null) {
                claveActual.append((char) i);
                obtenerClavesMaterias(hijo, claveActual, claves);
                claveActual.deleteCharAt(claveActual.length() - 1);
            }
        }
    }
}
