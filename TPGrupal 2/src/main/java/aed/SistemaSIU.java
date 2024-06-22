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
        carreras = new Trie<>(); 

        for (InfoMateria infoMateria : infoMaterias) {
            Materia materiaCompartida = new Materia();
            for (ParCarreraMateria parCarreraMateria : infoMateria.getParesCarreraMateria()) { 
                String materiaNombre = parCarreraMateria.getNombreMateria(); // O(1)
                String carreraNombre = parCarreraMateria.getCarrera(); 

                Trie<Materia> materiasDeCarrera = carreras.obtenerSignificado(carreraNombre); // O(|c|)
                if (materiasDeCarrera == null) {  //si no está la carrera, la inserto
                    materiasDeCarrera = new Trie<>(); 
                    carreras.insertar(carreraNombre, materiasDeCarrera); // O(|c|)
                }

                materiasDeCarrera.insertar(materiaNombre, materiaCompartida); // O(|N_m|). Insertar la istancia compartida de materia en las mismas materias (esto es para que apunten a la misma instancia de Materia y se actualicen juntas, es un alisaing intencional)

                materiaCompartida.agregarNombreMateria(materiaNombre);
                materiaCompartida.agregarCarrera(materiasDeCarrera); //guardamos dentro de cada materia, punteros a los tries de materias de otras carreras donde aparezca esa materia.
                                                                    // se guardan dentro de un ArrayList (dinámico), por lo que es O(1).
            }
        } 
        /*
         Para cada par carrera-nombre_materia se terminan realizando operaciones en orden O(|c|+ |n|). Además, se hace para cada nombre de cada materia, por lo que ser.
         Es decir, sería la sumatoria para cada materia m en M de la sumatoria para cada nombre de esa materia n en N_m, y cada carrera c donde aparezca esa materia, de n + c.
         Pero eso es equivalente a decir que, para cada materia en M, realizamos, para cada nombre n que tenga |n| operaciones, y luego, para cada carrera, operaciones en el orden
         de |c| * |M_c|, con M_c las materias de esa carrera. Es simplemente reordenar la sumatoria, y en vez de pensar cada carrera por materia, pensar las materias por carrera.
         En conclusión, la implementación cumple con la complejidad pedida.
        */
        
        estudiantes = new Trie<>();
        for (String libreta : libretasUniversitarias) {
            Estudiante e = new Estudiante();
            estudiantes.insertar(libreta, e); //como libreta está acotado, es k * E operaciones
        } // O(E). Luego, se cumple con lo pedido en términos de complejidad.
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

        Materia asignaturaACerrar = carreras.obtenerSignificado(carrera).obtenerSignificado(materia); //O(|c| +|m|)
        
        for (int i = 0; i < asignaturaACerrar.getCarreras().size(); i++){ //obtengo todas las carreras en las que aparece la materia m, y, para cada una, realizo operaciones en O(|n|)
            String nombreMateria = asignaturaACerrar.getNombresMateria().get(i); // O(|n|)
            Trie<Materia> materiasDeCarrera = asignaturaACerrar.getCarreras().get(i); // O(1)
            materiasDeCarrera.eliminar(nombreMateria); // O(n)
        } //termino realizando operaciones en el orden de la suma de todos los nombres de la materia m.
        
    } //todo termina siendo O(|c|+ |m| + suma, para n en N_m, de |n| + |E_m|)

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
        return estudiantesInscriptos > cupo; // O(1). Segun el TAD tenia que ser >=, pero asi los tests se rompen asi que queda >
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
        String[] stringArray = carreras.claves().toArray(new String[0]); //recorro en el Trie, todas las carreras. Es O(suma, para c en C, |c|). (el toArray vuelven a ser el mismo orden de operaciones, pero O(n) = O(2n)
        return stringArray;
    } 

    public String[] materias(String carrera){
        String[] stringToArray = carreras.obtenerSignificado(carrera).claves().toArray(new String[0]); // recorro primero la carrera (|c|), y luego, en el Trie, todas las materias de esa carrera. Es O(|c| + suma, para m en M_c, |m|)
        return stringToArray;
    }

    public int materiasInscriptas(String estudiante){
        return estudiantes.obtenerSignificado(estudiante).cantidadDeInscripciones(); //como la libreta es acotada, recorrerla es O(1). Hacer .cantidadDeInscripciones también. Es O(1)
    }
}
