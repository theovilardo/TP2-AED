package aed;

public class Estudiante {
    private int cantidadDeInscripciones; // Cantidad de materias a las que esta inscripto el estudiante

    // COnstructor de Estudiante
    public Estudiante(){
        this.cantidadDeInscripciones = 0;
    }

    // Inscribir al estudiante en una materia (aumenta en 1 la cantidad de inscripciones)
    public void estudianteSeInscribio(String materia){
        cantidadDeInscripciones = cantidadDeInscripciones + 1;
    }

    // Devuelve la cantidad de materias a las que esta inscripto el estudiante
    public int cantidadDeInscripciones(){
        return cantidadDeInscripciones;
    }

    // Resta una inscripcion al estudiante en caso de que deje la materia (se usa en el caso de que la materia cierre)
    public void dejarMateria(){
        cantidadDeInscripciones = cantidadDeInscripciones - 1;
    }
}
