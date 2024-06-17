package aed;

public class Estudiante {
    private int cantidadDeInscripciones;

    public Estudiante(){
        this.cantidadDeInscripciones = 1;
    }

    public void estudianteSeInscribio(){
        cantidadDeInscripciones = cantidadDeInscripciones + 1;
    }

    public int cantidadDeInscripciones(){
        return cantidadDeInscripciones;
    }
}
