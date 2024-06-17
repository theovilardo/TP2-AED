package aed;

public class Materia{
    private int estudiantesInscriptos;
    private int[] plantelDocente;
    private Trie<Materia>[] carlosMenem; // un trie por cada carrera en la que se compaerte es materia, este podria ser el que apunta a las correlaciones entre carrera y materia

    public Materia(){
        this.estudiantesInscriptos = 0;
        this.plantelDocente = new int[4];
        this.carlosMenem = null;
    }

    public void inscribirEstudiante(){
        estudiantesInscriptos = estudiantesInscriptos + 1;
    }

    public int obtenerInscriptos(){
        return estudiantesInscriptos;
    }

    public int[] obtenerPlantelDocente(){
        return plantelDocente;
    }

    public void agregarDocente(int cargo){
        //debe sumar la "cantidad" al valor de la posicion indicada por "cargo"
        plantelDocente[cargo] += 1;
    }
}
