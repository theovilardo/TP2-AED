package aed;

import java.util.ArrayList;
import java.util.Collections;

public class Trie<T>{
    private Nodo raiz;
    public static final int ALFABETO_LEXICOGRAFICO = 128; // alfabeto para el orden lexicografico segun el codigo ASCII

    // clase interna del nodo
    private class Nodo{
        private ArrayList<Nodo> hijos;
        private boolean[] indices; //booleanos por orden lexicografico, puede que al pedo
        private boolean finalDeClave;
        private T valor;

        //uso: si el nodo es el nodo de significado su valor se llena con los datos, sino su valor queda vacio
        public Nodo(T valor) {
            this.hijos = new ArrayList<>(Collections.nCopies(ALFABETO_LEXICOGRAFICO, null));
            this.finalDeClave = false;
            this.valor = valor;
        }

        public boolean esfinalDeClave() {
            return finalDeClave;
        }

        public void seteaFinalDeClave(boolean finalDeClave) {
            this.finalDeClave = finalDeClave;
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

    //Insertar
    // public void insertar(String clave, T valor) {
    //     Nodo actual = raiz;
    //     for (char c : clave.toCharArray()) { // convierte a cadena de chars
    //         int index = c;
    //         if (!actual.indices[index]) {
    //             actual.hijos[index] = new Nodo();
    //             actual.indices[index] = true;
    //         }
    //         actual = actual.hijos[index];
    //     }
    //     actual.seteaFinalDeClave(true);
    //     actual.asignarValor(valor);
    // }

    // ver como no pisar otros caracteres

    //Insertar
    // Idea: en vez de booleanos se chequea por posicion si es nula y en base a eso se ve el mismo nodo "siguiente"
    //To-Do: Soportar claves multiples y cruzadas ( que comparten nodos e incluso significados? ) 
    public void insertar(String clave, T valor) {
        Nodo actual = raiz;
        for (char c : clave.toCharArray()) { // convierte a cadena de chars
            int index = c; // pasa el char a codiog ASCII
            if (actual.hijos.get(index) == null) {
                actual.hijos.set(index, new Nodo(null)); //los nodos que no son el de siginficaod tienen valor null por ahora
            }
            actual = actual.hijos.get(index);
        }
        // el ultimo nodo es el de significado:
        actual.seteaFinalDeClave(true);
        actual.asignarValor(valor); // hace falta asignar?
    }
}
