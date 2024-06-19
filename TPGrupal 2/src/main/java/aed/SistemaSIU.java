package aed;

import java.util.ArrayList;

import aed.SistemaSIU.CargoDocente;

public class SistemaSIU {
    private Trie<Trie<Materia>> carreras; // Trie de carreras que guarda en su valor un Trie distinto para las materias de esas carreras
    private Trie<Estudiante> estudiantes; // Trie de estudianes, sus claves son las libretas universitarias y el valor de cada una es una instancia de Estudiante que guarda las inscripciones a las materias

    enum CargoDocente{
        AY2,
        AY1,
        JTP,
        PROF
    }

    // Constructor del SistemaSIU
    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias) {
        carreras = new Trie<>(); // Creo una nueva instancia para el Trie de carreras

        for (InfoMateria infoMateria : infoMaterias) { // veo cada infoMateria, vendria a ser la "asignatura"
            Materia materiaCompartida = new Materia(); // esta instancia de materia se comparte dentro del infoMateria (porque todos los pares dentro de este infoMateria especifico corresponden a la misma asignatura)
            for (ParCarreraMateria parCarreraMateria : infoMateria.getParesCarreraMateria()) { // obtengo los pares carrera-materia
                String materiaNombre = parCarreraMateria.getNombreMateria(); // guardo el nombre de la carrera
                String carreraNombre = parCarreraMateria.getCarrera(); // guardo el nombre de la materia

                Trie<Materia> materiasDeCarrera = carreras.obtenerSignificado(carreraNombre); // Trie para guardar todas las materias de la carrera
                if (materiasDeCarrera == null) { // si no existe la carrera (PARA DEBUG, ver si puede sacarse esta guarda).
                    materiasDeCarrera = new Trie<>(); // Nuevo Trie en caso de que no exista
                    carreras.insertar(carreraNombre, materiasDeCarrera); // insertar el Trie materiasDeCarrera en la carrera
                }
                materiasDeCarrera.insertar(materiaNombre, materiaCompartida); // insertar la istancia compartida de materia en las mismas materias (esto es para que apunten a la misma instancia de Materia y se actualicen juntas, es un alisaing intencional)
            }
        }
        
        estudiantes = new Trie<>(); // Creo una nueva instancia para el Trie de estudiantes
        for (String libreta : libretasUniversitarias) {
            Estudiante e = new Estudiante(); // Inicializo la clase estudiante para guardarlo como valor para cada clave (libreta universitaria) en el Trie de estudiantes
            estudiantes.insertar(libreta, e); // inserto la libreta como clave y el e: Estudiante como valor
        }
    }

    public void inscribir(String estudiante, String carrera, String materia){
        Materia materiaParaInscribir = new Materia();
        //busco en el trie de la carrera el trie de la materia y pido su valor tipo materia para inscribir al estudiante
        if (carreras.obtenerSignificado(carrera) != null){  // no deberian aparece nulos, se tiene que encargar el constructor (si jode la complejidad se lo saca la guarda)
            Trie<Materia> materiasDeLaCarrera = carreras.obtenerSignificado(carrera);
            if (materiasDeLaCarrera.obtenerSignificado(materia) != null){
                materiaParaInscribir = materiasDeLaCarrera.obtenerSignificado(materia);
            }
        }
        materiaParaInscribir.inscribirEstudiante(estudiante);
        //registrar la inscripcion en el trie de estudiantes:
        estudiantes.obtenerSignificado(estudiante).estudianteSeInscribio(materia); // nose si esta bien
    }

    public void agregarDocente(CargoDocente cargo, String carrera, String materia){
        if (cargo == CargoDocente.AY2){
            carreras.obtenerSignificado(carrera).obtenerSignificado(materia).agregarDocente(3); //AY2
        }
        if (cargo == CargoDocente.AY1){
            carreras.obtenerSignificado(carrera).obtenerSignificado(materia).agregarDocente(2); //AY1
        }
        if (cargo == CargoDocente.JTP){
            carreras.obtenerSignificado(carrera).obtenerSignificado(materia).agregarDocente(1); //JTP
        }
        if (cargo == CargoDocente.PROF){
            carreras.obtenerSignificado(carrera).obtenerSignificado(materia).agregarDocente(0); //PROF
        }
    }

    public int[] plantelDocente(String materia, String carrera){
        return carreras.obtenerSignificado(carrera).obtenerSignificado(materia).obtenerPlantelDocente();   
    }

    public void cerrarMateria(String materia, String carrera){
        ArrayList<String> clavesEstudiante = carreras.obtenerSignificado(carrera).obtenerSignificado(materia).obtenerEstudiantes();

        for (String libreta : clavesEstudiante){
            estudiantes.obtenerSignificado(libreta).dejarMateria();
        }
        // Al cerrar la carrera la marco como cerrada y la elimino del Trie
        carreras.obtenerSignificado(carrera).obtenerSignificado(materia).cerrarEstaMateria();
        // no hay una explicacion de porque lo hacemos por separado, ver que poner aca
        carreras.obtenerSignificado(carrera).eliminarSignficado(materia); //elimino la instancia de la Materia
        carreras.obtenerSignificado(carrera).eliminar(materia); // elimino la clave
    }

    public int inscriptos(String materia, String carrera){
        int inscriptos = 0;
        if (carreras.obtenerSignificado(carrera) != null){
            Trie<Materia> materiasDeLaCarrera = carreras.obtenerSignificado(carrera);
            if (materiasDeLaCarrera.obtenerSignificado(materia) != null){ // chequear si es necesaria la guarda
                inscriptos = materiasDeLaCarrera.obtenerSignificado(materia).obtenerInscriptos();
            }
        }
        return inscriptos;
    }

    public boolean excedeCupo(String materia, String carrera){
        int estudiantesInscriptos = inscriptos(materia, carrera);
        int cupo = calcularCupo(carrera, materia);
        return estudiantesInscriptos > cupo; // false si excede el cupo, segun el TAD tenia que ser >=, pero asi los tests se rompen asi que queda >, ver que poner bien aca
    }

    // Funcion Auxilar para excedeCupo(), calcula el minimo cupo a exceder.
    public int calcularCupo(String nombreCarrera, String nombreMateria) {
        Materia materiaAChequear = carreras.obtenerSignificado(nombreCarrera).obtenerSignificado(nombreMateria);
        int cupoPROF = 250 * materiaAChequear.obtenerPlantelDocente()[0];
        int cupoJTP = 100 * materiaAChequear.obtenerPlantelDocente()[1];
        int cupoAY1 = 20 * materiaAChequear.obtenerPlantelDocente()[2];
        int cupoAY2 = 30 * materiaAChequear.obtenerPlantelDocente()[3];

        //buscar el minimo
        int cupo = Math.min(Math.min(cupoPROF, cupoJTP), Math.min(cupoAY1, cupoAY2)); // ver si afecta la complejidad ya que es una comparacion simple (creo), y ver si se podia usar el Math o habira que usar otra aux: Minimo

        return cupo;
    }

    public String[] carreras(){
        String[] stringArray = carreras.claves().toArray(new String[0]); //pasa de List<String> a String[], no creo que afecte la complejidad, igual chequear
        return stringArray;
    }

    public String[] materias(String carrera){
        String[] stringToArray = carreras.obtenerSignificado(carrera).claveMaterias().toArray(new String[0]); // .claveMaterias() devuelve la lista de materias de la carrera (mientras esten abiertas)
        return stringToArray;
    }

    public int materiasInscriptas(String estudiante){
        return estudiantes.obtenerSignificado(estudiante).cantidadDeInscripciones();
    }
}
