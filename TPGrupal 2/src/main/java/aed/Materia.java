package aed;

public class Materia{
    private int estudiantesInscriptos;
    private int[] plantelDocente;
    private Trie<Materia>[] carlosMenem; // un trie por cada carrera en la que se compaerte es materia

    public Materia(){
        this.estudiantesInscriptos = 0;
        this.plantelDocente = new int[4];
        this.carlosMenem = null;
    }
}
