package aed;

public class Estudiante {
    private int cantidadDeInscripciones;

    public Estudiante(){
        this.cantidadDeInscripciones = 0;
    }

    public void estudianteSeInscribio(String materia){
        cantidadDeInscripciones = cantidadDeInscripciones + 1;
    }

    public int cantidadDeInscripciones(){
        return cantidadDeInscripciones;
    }

    // tu materia ya no existe, chau
    public void dejarMateria(){
        cantidadDeInscripciones = cantidadDeInscripciones - 1;
    }
}
