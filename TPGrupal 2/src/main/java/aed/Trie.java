package aed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trie<T>{
    private Nodo raiz;
    public static final int ALFABETO_LEXICOGRAFICO = 256; // alfabeto para el orden lexicografico segun el codigo ASCII

    // clase interna del nodo
    private class Nodo{
        private ArrayList<Nodo> hijos;
        private T valor;

        //uso: si el nodo es el nodo de significado su valor se llena con los datos, sino su valor queda vacio
        public Nodo(T valor) {
            this.hijos = new ArrayList<>(Collections.nCopies(ALFABETO_LEXICOGRAFICO, null));
            this.valor = valor;
        }

        public T obtenerValor() {
            return valor;
        }

        public void asignarValor(T valor) {
            this.valor = valor;
        }
    }

    public Trie() {
        this.raiz = new Nodo(null);
    }
    // ver como no pisar otros caracteres
    //Insertar
    // Dato: si se mete una clave ya existente reemplazara el valor actual por el dado
    public void insertar(String clave, T valor) {
        //clave = Normalizador.normalizar(clave); //normaliza la clave en caso de tener caracteres fuera dle codigo ASCII 128 como el "ó". Falta corroborar
        Nodo actual = raiz;
        for (char c : clave.toCharArray()) { // convierte a cadena de chars
            int index = c; // pasa el char a codiog ASCII
            if (actual.hijos.get(index) == null) {
                actual.hijos.set(index, new Nodo(null)); //los nodos que no son el de siginficaod tienen valor null por ahora
            }
            actual = actual.hijos.get(index);
        }
        actual.asignarValor(valor);
    }

    public T obtenerSignificado(String clave){
        //clave = Normalizador.normalizar(clave); //normaliza la clave
        Nodo actual = raiz;
        for (char c : clave.toCharArray()) {
            int index = c; // Convertimos el caracter en su valor ASCII
            if (actual.hijos.get(index) == null) {
                return null;
            }
            actual = actual.hijos.get(index);
        }
        if (actual.obtenerValor() == null){
            throw new UnsupportedOperationException("el valor de esta clave es nulo (no deberia pasar)");
        }
        return actual.obtenerValor();
    }

    public boolean buscar(String clave) {
        //clave = Normalizador.normalizar(clave); //normaliza la clave
        Nodo actual = raiz;
        for (char c : clave.toCharArray()) { // convierte a cadena de chars
            int index = c; // pasa el char a codiog ASCII
            if (actual.hijos.get(index) == null) {
                return false;
            }
            actual = actual.hijos.get(index);
        }
        return actual.valor != null; // si el nodo significado no es nulo encontraste la clave
    }

    // Método para eliminar una clave del Trie
    public void eliminar(String clave) {
        //clave = Normalizador.normalizar(clave); //normaliza la clave
        eliminarTrie(raiz, clave, 0); //inicializo el eliminador con el index en 0 (primera posicion de la clave)
    }

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
            actual.asignarValor(null); // Eliminar el valor del nodo final
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

    private boolean noTieneHijos(Nodo nodo) { //pred no tiene hijos
        for (Nodo hijo : nodo.hijos) {
            if (hijo != null) {
                return false;
            }
        }
        return true;
    }

    // obtener todas las claves del trie
    public List<String> claves() {
        List<String> claves = new ArrayList<>();
        obtenerClaves(raiz, new StringBuilder(), claves);
        return claves;
    }

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
}
