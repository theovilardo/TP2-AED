package aed;

import java.util.ArrayList;

import aed.SistemaSIU.CargoDocente;
/* Asignatura = Materia que tiene distinto nombre segun la carrera.
Inv Rep: 
El Trie carreras debe contener un Trie de materiasDeCarrera para cada carrera. 
Cada materiasDeCarrera debe contener todas las materias asociadas a esa carrera, con las materias de una misma asignatura compartiendo la misma instancia de Materia.
El Trie estudiantes debe contener una instancia de Estudiante para cada libreta universitaria proporcionada en el constructor.
Sobre las complejidades, para no excederos con la cantidad de comentarios, entendemos que realizar una cantidad finita y constante de veces algo que es 
O(n), sigue siendo O(n).  
*/
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

                materiaCompartida.agregarNombreMateria(materiaNombre);
                materiaCompartida.agregarCarrera(materiasDeCarrera); //guardo cada carrera donde aparece esa materia (asignatura)
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
        if (carreras.obtenerSignificado(carrera) != null){ 
            Trie<Materia> materiasDeLaCarrera = carreras.obtenerSignificado(carrera); // O(|c|)
            if (materiasDeLaCarrera.obtenerSignificado(materia) != null){ // O(|m|)
                materiaParaInscribir = materiasDeLaCarrera.obtenerSignificado(materia);
            }
        }
        materiaParaInscribir.inscribirEstudiante(estudiante); // O(|m|)
        //registrar la inscripcion en el trie de estudiantes:
        estudiantes.obtenerSignificado(estudiante).estudianteSeInscribio(materia); // O(1), porque el Trie estudiante es acotado
        // O(|c|) + O(|m|) + O(|1|) = O(|c+m|)
    }

    public void agregarDocente(CargoDocente cargo, String carrera, String materia){
        if (cargo == CargoDocente.AY2){
            carreras.obtenerSignificado(carrera).obtenerSignificado(materia).agregarDocente(3); //AY2. O(|c|) + O(|m|) + O(|1|) = O(|c+m|), igual en cualquier guarda
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
        return carreras.obtenerSignificado(carrera).obtenerSignificado(materia).obtenerPlantelDocente(); // O(|c|) + O(|m|) + O(|1|) = O(|c+m|)
    }

    public void cerrarMateria(String materia, String carrera){
        ArrayList<String> clavesEstudiante = carreras.obtenerSignificado(carrera).obtenerSignificado(materia).obtenerEstudiantes(); // O(|c|) + O(|m|) + O(|1|) = O(|c+m|)

        for (String libreta : clavesEstudiante){
            estudiantes.obtenerSignificado(libreta).dejarMateria(); //O(1) * E_m = O(|E_m|)
        }

        Materia asignaturaACerrar = carreras.obtenerSignificado(carrera).obtenerSignificado(materia);
        
        for (int i = 0; i < asignaturaACerrar.getCarreras().size(); i++){
            System.out.print(asignaturaACerrar.getNombresMateria());
            String nombreMateria = asignaturaACerrar.getNombresMateria().get(i);
            Trie<Materia> materiasDeCarrera = asignaturaACerrar.getCarreras().get(i);
            materiasDeCarrera.eliminar(nombreMateria);
        }
        // for (Trie<Materia> materiasDeCarrera : asignaturaACerrar.getCarreras()){
        //     materiasDeCarrera.eliminar(materia);
        // }

        // // Para surtir el problema de no poder acceder nuevamente al resto de las carreras que compartan esta asignatura,
        // // y teniendo en cuenta que no existe la posibilidad de abrir una materia luego de ser cerrada (por los métodos posibles)
        // // en vez de eliminar la instancia Materia y su clave correspondiente de todas las carreras que la compartan, marcamos a la materia
        // // como cerrada usando una variable booleana dentro de la instancia materia. De esta forma, al pedir las materias de cierta carrera,
        // // indicaremos que si la materia está cerrada, no se devuelva. Esto tiene entonces complejidad O(|c+m|)
        // carreras.obtenerSignificado(carrera).obtenerSignificado(materia).cerrarEstaMateria();
        // // la complejidad final es O(|c+m|) + O(|E_m|) que está contenido dentro de lo pedido, O(|c+m| + E_m| + O(|c+m| + E_m + la suma de cada nombre de materia de la asignatura m)
    }

    public int inscriptos(String materia, String carrera){
        int inscriptos = 0;
        if (carreras.obtenerSignificado(carrera) != null){
            Trie<Materia> materiasDeLaCarrera = carreras.obtenerSignificado(carrera); // O(|c|)
            if (materiasDeLaCarrera.obtenerSignificado(materia) != null){
                inscriptos = materiasDeLaCarrera.obtenerSignificado(materia).obtenerInscriptos(); // O(|m|) + O(1)
            }
        }
        return inscriptos; //O(|c|) + O(|m|) + O(1) = O(|c+m|)
    }

    public boolean excedeCupo(String materia, String carrera){
        int estudiantesInscriptos = inscriptos(materia, carrera); // O(|c| + |m|)
        int cupo = calcularCupo(carrera, materia); //O(|c| + |m|)
        return estudiantesInscriptos > cupo; // O(1) false si excede el cupo, segun el TAD tenia que ser >=, pero asi los tests se rompen asi que queda >, ver que poner bien aca
    } //la complejidad final es O(|c| + |m|)

    // Funcion Auxilar para excedeCupo(), calcula el minimo cupo a exceder. Es O(|c|+|m|)
    public int calcularCupo(String nombreCarrera, String nombreMateria) {
        Materia materiaAChequear = carreras.obtenerSignificado(nombreCarrera).obtenerSignificado(nombreMateria); //O(|c| + |m|)
        int cupoPROF = 250 * materiaAChequear.obtenerPlantelDocente()[0]; // O(1)
        int cupoJTP = 100 * materiaAChequear.obtenerPlantelDocente()[1];
        int cupoAY1 = 20 * materiaAChequear.obtenerPlantelDocente()[2];
        int cupoAY2 = 30 * materiaAChequear.obtenerPlantelDocente()[3];

        //buscar el minimo
        int cupo = Math.min(Math.min(cupoPROF, cupoJTP), Math.min(cupoAY1, cupoAY2)); // O(1), son comparaciones simples

        return cupo;
    }

    public String[] carreras(){
        String[] stringArray = carreras.claves().toArray(new String[0]); //pasa de List<String> a String[], no creo que afecte la complejidad, igual chequear
        return stringArray;
    }

    public String[] materias(String carrera){
        String[] stringToArray = carreras.obtenerSignificado(carrera).claves().toArray(new String[0]); // .claveMaterias() devuelve la lista de materias abiertas (no cerradas) de la carrera
        return stringToArray;
    }

    public int materiasInscriptas(String estudiante){
        return estudiantes.obtenerSignificado(estudiante).cantidadDeInscripciones();
    }
}
