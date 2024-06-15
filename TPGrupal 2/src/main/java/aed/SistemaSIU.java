package aed;

public class SistemaSIU {
    private Trie<Trie<Materia>> carreras;

    enum CargoDocente{
        AY2,
        AY1,
        JTP,
        PROF
    }

    @SuppressWarnings("unchecked") //se saca esto despues
    public SistemaSIU(InfoMateria[] infoMaterias, String[] libretasUniversitarias){
        Trie carreras = new Trie<Trie<Materia>>();
        Trie estudiantes = new Trie<Integer>();
        for (InfoMateria infoMateria : infoMaterias){
            ParCarreraMateria[] pares = infoMateria.getParesCarreraMateria();
            Materia mdata = new Materia();
            Trie trieMaterias = new Trie<Materia>();
            for (ParCarreraMateria par : pares){
                carreras.insertar(par.getCarrera(), trieMaterias); //guardar en un alista todos los tries de materias de carreras que comparte
                //no deberia ir primero el trieMaterias?
                trieMaterias.insertar(par.nombreMateria, mdata);

            }
        }
    }

    public void inscribir(String estudiante, String carrera, String materia){
        throw new UnsupportedOperationException("Método no implementado aún");
    }

    public void agregarDocente(CargoDocente cargo, String carrera, String materia){
        throw new UnsupportedOperationException("Método no implementado aún");	    
    }

    public int[] plantelDocente(String materia, String carrera){
        throw new UnsupportedOperationException("Método no implementado aún");	    
    }

    public void cerrarMateria(String materia, String carrera){
        throw new UnsupportedOperationException("Método no implementado aún");	    
    }

    public int inscriptos(String materia, String carrera){
        throw new UnsupportedOperationException("Método no implementado aún");	    
    }

    public boolean excedeCupo(String materia, String carrera){
        throw new UnsupportedOperationException("Método no implementado aún");	    
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
