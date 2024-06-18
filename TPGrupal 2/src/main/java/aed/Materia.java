package aed;

import java.util.ArrayList;

public class Materia{
    private boolean estaCerrada;
    private int estudiantesInscriptos;
    private int[] plantelDocente;
    private ArrayList<String> estudiantesDeLaMateria; //ArrayList<String> estudiantesDeLaMateria;
    //private Trie<Materia>[] carlosMenem; // un trie por cada carrera en la que se compaerte es materia, este podria ser el que apunta a las correlaciones entre carrera y materia

    public Materia(){
        this.estaCerrada = false;
        this.estudiantesInscriptos = 0;
        this.plantelDocente = new int[4];
        this.estudiantesDeLaMateria = new ArrayList<>();
        //this.carlosMenem = null;
    }

    public void inscribirEstudiante(String estudiante){
        estudiantesInscriptos = estudiantesInscriptos + 1;
        estudiantesDeLaMateria.add(estudiante);
    }

    public ArrayList<String> obtenerEstudiantes(){
        return estudiantesDeLaMateria;
    }

    public int obtenerInscriptos(){
        return estudiantesInscriptos;
    }

    public int[] obtenerPlantelDocente(){
        return plantelDocente;
    }

    public void agregarDocente(int cargo){
        //debe sumar la "cantidad" al valor de la posicion indicada por "cargo"
        plantelDocente[cargo] = plantelDocente[cargo] + 1;
    }

    public void cerrarEstaMateria(){
        estaCerrada = true;
    }

    public boolean estaCerrada(){
        return estaCerrada;
    }

}
