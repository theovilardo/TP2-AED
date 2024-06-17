package aed;

import aed.SistemaSIU.CargoDocente;

public class SistemaSIU {
    private Trie<Trie<Materia>> carreras;
    private Trie<Estudiante> estudiantes;

    enum CargoDocente{
        AY2,
        AY1,
        JTP,
        PROF
    }

    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias) {
        //Trie newCarreras = new Trie<Trie<Materia>>();
        carreras = new Trie<>(); //pruebo sin copiar todo a carreras post-ciclo

        for (InfoMateria infoMateria : infoMaterias) { // veo cada infoMateria, vendria a ser la "asignatura"
            Materia materiaCompartida = new Materia(); // esta instancia de materia se comparte dentro del infomateria (porquq todos los pares dentor de este infomateria especifico correspondend a la misma asignatura)
            for (ParCarreraMateria parCarreraMateria : infoMateria.getParesCarreraMateria()) { // obtengo los pares carrera-materia
                String materiaNombre = parCarreraMateria.getNombreMateria(); // guarod la carrera
                String carreraNombre = parCarreraMateria.getCarrera(); // guardo la materia

                Trie<Materia> materiasDeCarrera = carreras.obtenerSignificado(carreraNombre); //trie pra todas las materias de la carrera
                if (materiasDeCarrera == null) { //si no existe la carrera (PARA DEBUG).
                    materiasDeCarrera = new Trie<>(); // nuevo trie
                    carreras.insertar(carreraNombre, materiasDeCarrera); //insertar materias de carreara en la carrera
                }
                materiasDeCarrera.insertar(materiaNombre, materiaCompartida); // insertar la istancia d emateria en las mismas materias
            }
        }
        //carreras = newCarreras;
        // falta un bloque donde meta a los estudiantes de libretasUniversitarias
        estudiantes = new Trie<>();
        for (String libreta : libretasUniversitarias){ //debe estar mal la complejidad, asi que a chequear a la chequeria
            Estudiante e = new Estudiante();
            estudiantes.insertar(libreta, e);
            Estudiante anotarInscripcion = estudiantes.obtenerSignificado(libreta);
            anotarInscripcion.estudianteSeInscribio();
        }
    }

    public void inscribir(String estudiante, String carrera, String materia){
        Materia materiaParaInscribir = new Materia();
        //busco en el trie de la carrera el trie de la materia y pido su valor tipo materia para inscribir al estudiante
        if (carreras.obtenerSignificado(carrera) != null){  // no deberian aparece nulos, se tiene que encargar el constructor (si jode la complejidad se lo saca)
            Trie<Materia> materiasDeLaCarrera = carreras.obtenerSignificado(carrera);
            if (materiasDeLaCarrera.obtenerSignificado(materia) != null){
                materiaParaInscribir = materiasDeLaCarrera.obtenerSignificado(materia);
            }
        }
        materiaParaInscribir.inscribirEstudiante();
        //registrar en el trie de estudiantes: (puede que esto haya que tratarlo como las instancias de materias para no alterar la complejiad)
        //estudiantes.insertar(estudiante, ); // complejidad: O(1) \\mejor armar otra clase para estudiante con un metodo para aumentar al estudiante dado.
    }

    public void agregarDocente(CargoDocente cargo, String carrera, String materia){
        //carreras.obtenerSignificado(carrera).obtenerSignificado(materia).agregarDocente(cargo, 1);
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
        throw new UnsupportedOperationException("Método no implementado aún");
    }

    public int inscriptos(String materia, String carrera){
        int inscriptos = 0;
        if (carreras.obtenerSignificado(carrera) != null){
            Trie<Materia> materiasDeLaCarrera = carreras.obtenerSignificado(carrera);
            // esta siendo null? :
            //inscriptos = materiasDeLaCarrera.obtenerSignificado(materia).obtenerInscriptos();
            if (materiasDeLaCarrera.obtenerSignificado(materia) != null){ // esto esta null y no deberia
                inscriptos = materiasDeLaCarrera.obtenerSignificado(materia).obtenerInscriptos();
            }
        }
        return inscriptos;
    }

    public boolean excedeCupo(String materia, String carrera){
        int estudiantesInscriptos = inscriptos(materia, carrera);
        int cupo = calcularCupo(carrera, materia);
        //int cupo = minimo();
        // capacidad total, seria el cupo maximo
        //int capacidadTotal = (plantelDocente[0] * capacidadPorProfesor) + (plantelDocente[1] * capacidadPorJTP) + (plantelDocente[2] * capacidadPorAy1) + (plantelDocente[3] * capacidadPorAy2);
        return estudiantesInscriptos > cupo; // false si excede el cupo
    }

    // calcula el minimo cupo a exceder
    public int calcularCupo(String nombreCarrera, String nombreMateria) {
        Materia materiaAChequear = carreras.obtenerSignificado(nombreCarrera).obtenerSignificado(nombreMateria);
        int cupoPROF = 250 * materiaAChequear.obtenerPlantelDocente()[0]; //250 * docentes(CargoDocente.PROF, carrera, nombreMateria);
        int cupoJTP = 100 * materiaAChequear.obtenerPlantelDocente()[1];
        int cupoAY1 = 20 * materiaAChequear.obtenerPlantelDocente()[2];
        int cupoAY2 = 30 * materiaAChequear.obtenerPlantelDocente()[3];

        //buscar el minimo
        int cupo = Math.min(Math.min(cupoPROF, cupoJTP), Math.min(cupoAY1, cupoAY2)); // ver si afecta la complejidad ya que es una comparacion simple (creo)

        return cupo;
    }

    //auxiliar para devolver el minimo
    private int minimo(int[] cantidades){
        int min = cantidades[0];
        for (int i = 1; i < cantidades.length; i++) {
            if (cantidades[i] < min) {
                min = cantidades[i];
            }
        }
        return min;
    }

    public String[] carreras(){
        String[] stringArray = carreras.claves().toArray(new String[0]); //pasa de List<String> a String[], no creo que afecte la complejidad
        return stringArray;
    }

    public String[] materias(String carrera){
        String[] stringToArray = carreras.obtenerSignificado(carrera).claves().toArray(new String[0]);
        return stringToArray;
    }

    public int materiasInscriptas(String estudiante){
        return estudiantes.obtenerSignificado(estudiante).cantidadDeInscripciones();
    }
}
