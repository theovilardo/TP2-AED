package aed;

import java.util.*;

public class ListaEnlazada<T> implements Secuencia<T> {
    private Nodo primero;
    private Nodo ultimo;

    private class Nodo {
        private Nodo anterior;
        private Nodo siguiente;
        private T elem;

        Nodo(T e) {
            elem = e;
        }

    }

    public ListaEnlazada() {

        primero = null;
        ultimo = null;
    }

    public int longitud() {
        int count = 0;
        Nodo actual = this.primero;
        while (actual != null) {
            count++;
            actual = actual.siguiente;
        }
        return count;
    }

    public void agregarAdelante(T elem) {
        Nodo nuevo = new Nodo(elem);
        if (this.longitud() == 0) {
            this.primero = nuevo;
            this.ultimo = nuevo;
        } else {
            nuevo.siguiente = this.primero;
            this.primero.anterior = nuevo;
            this.primero = nuevo;
        }
    }

    public void agregarAtras(T elem) {
        Nodo nuevo = new Nodo(elem);
        if (this.longitud() == 0) {
            this.primero = nuevo;
            this.ultimo = primero;
        } else {
            nuevo.anterior = this.ultimo;
            this.ultimo.siguiente = nuevo;
            this.ultimo = nuevo;
        }
    }

    public T obtener(int i) {
        int count = 0;
        Nodo actual = this.primero;
        while (count != i) {
            count++;
            actual = actual.siguiente;
        }

        return actual.elem;
    }

    public void eliminar(int i) {
        if (this.longitud() == 1 && i == 0) {
            this.primero = null;
            this.ultimo = null;
        } else {
            if (i == 0) {
                this.primero = this.primero.siguiente;
                this.primero.anterior = null;
            } else if (i == this.longitud() - 1) {
                this.ultimo = this.ultimo.anterior;
                this.ultimo.siguiente = null;
            } else {
                int count = 0;
                Nodo actual = this.primero;
                while (count != i) {
                    actual = actual.siguiente;
                    count++;
                }
                actual.anterior.siguiente = actual.siguiente;
                actual.siguiente.anterior = actual.anterior;
            }
        }
    }

    public void modificarPosicion(int indice, T elem) {
        Nodo nuevo = new Nodo(elem);
        if (this.longitud() == 1 || indice == 0) {
            this.eliminar(0);
            this.agregarAdelante(elem);
        } else if (indice == this.longitud() - 1) {
            this.eliminar(indice);
            this.agregarAtras(elem);
        } else {
            int count = 0;
            Nodo actual = this.primero;
            while (count != indice) {
                actual = actual.siguiente;
                count++;
            }
            nuevo.anterior = actual.anterior;
            nuevo.siguiente = actual.siguiente;
            nuevo.anterior.siguiente = nuevo;
            nuevo.siguiente.anterior = nuevo;
        }
    }

    public ListaEnlazada<T> copiar() {
        ListaEnlazada<T> nuevaLista = new ListaEnlazada<>();

        Nodo actual = this.primero;
        while (actual != null) {

            nuevaLista.agregarAtras(actual.elem);
            actual = actual.siguiente;
        }
        return nuevaLista;

    }

    public ListaEnlazada(ListaEnlazada<T> lista) {
        primero = lista.primero;
        ultimo = lista.ultimo;
    }

    @Override
    public String toString() {
        String res = "[";
        int count = 0;
        while (count != this.longitud()) {
            res += String.valueOf(this.obtener(count));
            if (count != this.longitud() - 1) {
                res += ", ";
            }
            count++;
        }
        res += "]";
        return res;
    }

    private class ListaIterador implements Iterador<T> {
        private Nodo puntero;

        public ListaIterador() {
            puntero = primero;
        }

        public boolean haySiguiente() {
            if (puntero == null) {
                return false;
            }
            return puntero != null;
        }

        public boolean hayAnterior() {
            if (ultimo != null && puntero == ultimo.siguiente) {
                return true;
            } else if (puntero == null) {
                return false;
            } else {
                return puntero.anterior != null;
            }
        }

        public T siguiente() {
            T elem = puntero.elem;
            puntero = puntero.siguiente;
            return elem;
        }

        public T anterior() {
            if (puntero == null) {
                puntero = ultimo;
                return puntero.elem;
            }
            puntero = puntero.anterior;
            T elem = puntero.elem;
            return elem;
        }
    }

    public Iterador<T> iterador() {
        Iterador puntero = new ListaIterador();
        return puntero;
    }

}
