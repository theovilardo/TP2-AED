package aed;

import java.util.ArrayList;
/*
Inv rep: 
estudiantesInscriptos debe ser un entero no negativo y debe coincidir con el tamaño de estudiantesDeLaMateria.

plantelDocente debe ser un arreglo de enteros con 4 elementos, todos no negativos, donde cada elemento representa la cantidad de docentes en un cargo específico.

estudiantesDeLaMateria debe ser una lista de cadenas (libretas universitarias) cuya longitud debe ser igual a estudiantesInscriptos.

carreras es una array list con los punteros a los tries de materias de las carreras donde esté la instancia correspondiente de Materia.
Debe tener el mismo tamaño que nombresDeLaMateria, y estar en el mismo orden, es decir, el nombre de materia en la posición i de nombresDeLaMateria
debe estar en la posición i de carreras. 

nombresDeLaMateria contiene todos los nombres con los que se identifica la instancia. tiene la misma cantidad de elementos que carreras, y está ordenado de la misma manera,
es decir, el nombre de materia en la posición i de nombresDeLaMateria
debe estar en la posición i de carreras. 
*/

public class Materia{
    private boolean estaCerrada; // Estado de la Asignatura (Abierta o Cerrada)
    private int estudiantesInscriptos; // Cantidad de estudiantes inscriptos
    private int[] plantelDocente; // Cantidad de Docentes en cada posicion segun su cargo
    private ArrayList<String> estudiantesDeLaMateria; // Lista de libretas de los estudiantes inscriptos en la materia
    private ArrayList<Trie<Materia>> carreras;
    private ArrayList<String> nombresDeLaMateria;

    // Constructor de Materia
    public Materia(){
        this.estaCerrada = false;
        this.estudiantesInscriptos = 0;
        this.plantelDocente = new int[4];
        this.estudiantesDeLaMateria = new ArrayList<>();
        this.carreras = new ArrayList<>();
        this.nombresDeLaMateria = new ArrayList<>();
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

    public void agregarCarrera(Trie<Materia> carrera){
        this.carreras.add(carrera);
    }

    public ArrayList<Trie<Materia>> getCarreras(){
        return this.carreras;
    }

    public void agregarNombreMateria(String nombreMateria){
        this.nombresDeLaMateria.add(nombreMateria);
    }

    public ArrayList<String> getNombresMateria(){
        return this.nombresDeLaMateria;
    }
}
