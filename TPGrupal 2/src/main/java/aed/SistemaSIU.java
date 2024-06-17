package aed;

import aed.SistemaSIU.CargoDocente;

public class SistemaSIU {
    private Trie<Trie<Materia>> carreras;

    enum CargoDocente{
        AY2,
        AY1,
        JTP,
        PROF
    }

    @SuppressWarnings("unchecked")
    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias){
        Trie carrerasNew = new Trie<Trie<Materia>>();
        Trie trieMaterias = new Trie<Materia>(); // habia que inicializarlo antes del ciclo, no adentro
        //Trie estudiantes = new Trie<Integer>(); //empiezo a creer que esto no va aca
        for (InfoMateria infoMateria : infoMaterias){
            ParCarreraMateria[] paresCarreraMateria = infoMateria.getParesCarreraMateria();
            Materia mdata = new Materia();
            // Trie trieMaterias = new Trie<Materia>();
            for (ParCarreraMateria parCarreraMateria : paresCarreraMateria){
                trieMaterias.insertar(parCarreraMateria.nombreMateria, mdata);
                carrerasNew.insertar(parCarreraMateria.getCarrera(), trieMaterias); //guardar en una lista todos los tries de materias de carreras que comparte - coment para Teo
            }
        }
        carreras = carrerasNew;
    }

    public void inscribir(String estudiante, String carrera, String materia){
        Materia materiaParaInscribir = new Materia();
        //busco en el trie de la carrera el trie de la materia y pido su valor tipo materia para inscribir al estudiante
        if (carreras.obtenerSignificado(carrera) != null){
            Trie<Materia> materiasDeLaCarrera = carreras.obtenerSignificado(carrera);
            if (materiasDeLaCarrera.obtenerSignificado(materia) != null){
                materiaParaInscribir = materiasDeLaCarrera.obtenerSignificado(materia);
            }
        }
        materiaParaInscribir.inscribirEstudiante();
    }

    public void agregarDocente(CargoDocente cargo, String carrera, String materia){
        //carreras.obtenerSignificado(carrera).obtenerSignificado(materia).agregarDocente(cargo, 1);
        if (cargo == CargoDocente.AY2){
            carreras.obtenerSignificado(carrera).obtenerSignificado(materia).agregarDocente(3);
        }
        if (cargo == CargoDocente.AY1){
            carreras.obtenerSignificado(carrera).obtenerSignificado(materia).agregarDocente(2);
        }
        if (cargo == CargoDocente.JTP){
            carreras.obtenerSignificado(carrera).obtenerSignificado(materia).agregarDocente(1);
        }
        if (cargo == CargoDocente.PROF){
            carreras.obtenerSignificado(carrera).obtenerSignificado(materia).agregarDocente(0);
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
        int [] plantelDocente = carreras.obtenerSignificado(carrera).obtenerSignificado(materia).obtenerPlantelDocente();
        int estudiantesInscriptos = carreras.obtenerSignificado(carrera).obtenerSignificado(materia).obtenerInscriptos();
        // Capacidades máximas por cargo docente
        int capacidadPorProfesor = 250;
        int capacidadPorJTP = 100;
        int capacidadPorAy1 = 20;
        int capacidadPorAy2 = 30;
        // Calcular capacidad total permitida
        int capacidadTotal = (plantelDocente[0] * capacidadPorProfesor)
                + (plantelDocente[1] * capacidadPorJTP)
                + (plantelDocente[2] * capacidadPorAy1)
                + (plantelDocente[3] * capacidadPorAy2);
        
        // Comparar estudiantes inscritos con capacidad total permitida
        return estudiantesInscriptos > capacidadTotal;
    }

    public String[] carreras(){
        throw new UnsupportedOperationException("Método no implementado aún");	    
    }

    public String[] materias(String carrera){
        throw new UnsupportedOperationException("Método no implementado aún");	    
    }

    public int materiasInscriptas(String estudiante){
        throw new UnsupportedOperationException("Método no implementado aún");	    
    }
}
