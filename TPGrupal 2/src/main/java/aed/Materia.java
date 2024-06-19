package aed;

import java.util.ArrayList;
/*
Inv rep: 
estudiantesInscriptos debe ser un entero no negativo y debe coincidir con el tamaño de estudiantesDeLaMateria.
plantelDocente debe ser un arreglo de enteros con 4 elementos, todos no negativos, donde cada elemento representa la cantidad de docentes en un cargo específico.
estudiantesDeLaMateria debe ser una lista de cadenas (libretas universitarias) cuya longitud debe ser igual a estudiantesInscriptos.
*/

public class Materia{
    private boolean estaCerrada; // Estado de la Asignatura (Abierta o Cerrada)
    private int estudiantesInscriptos; // Cantidad de estudiantes inscriptos
    private int[] plantelDocente; // Cantidad de Docentes en cada posicion segun su cargo
    private ArrayList<String> estudiantesDeLaMateria; // Lista de libretas de los estudiantes inscriptos en la materia

    // Constructor de Materia
    public Materia(){
        this.estaCerrada = false;
        this.estudiantesInscriptos = 0;
        this.plantelDocente = new int[4];
        this.estudiantesDeLaMateria = new ArrayList<>();
    }

    // Inscribe al estudiante en la materia (aumenta la cantidad de inscriptos y guarda la libreta del estudiante)
    public void inscribirEstudiante(String estudiante){
        estudiantesInscriptos = estudiantesInscriptos + 1;
        estudiantesDeLaMateria.add(estudiante);
    }

    // Devuelve la lista de libretas de los estudiantes inscriptos
    public ArrayList<String> obtenerEstudiantes(){
        return estudiantesDeLaMateria;
    }

    // Devuelve la cantidad de estudiantes inscriptos
    public int obtenerInscriptos(){
        return estudiantesInscriptos;
    }

    // Devuelve la cantidad de docentes en cada posicion segun su cargo
    public int[] obtenerPlantelDocente(){
        return plantelDocente;
    }

    // Agrega un docente al cargo seleccionado (0 o 1 o 2 o 3)
    public void agregarDocente(int cargo){
        plantelDocente[cargo] = plantelDocente[cargo] + 1;
    }

    // Cierra la materia marcando el estaCerrada como true
    public void cerrarEstaMateria(){
        estaCerrada = true;
    }

    // Devuelve True si la materia esta cerrada
    public boolean estaCerrada(){
        return estaCerrada;
    }

}
