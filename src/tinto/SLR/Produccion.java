package tinto.SLR;

import java.util.List;
import tinto.ast.NodoSimbolo;

public class Produccion {
    private final int id;
    private final String lhs;
    private final List<NodoSimbolo> rhs;

    public Produccion(int id, String lhs, List<NodoSimbolo> rhs) {
        this.id = id;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public int getId() { return id; }
    public String getLhs() { return lhs; }
    public List<NodoSimbolo> getRhs() { return rhs; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(": ").append(lhs).append(" -> ");
        for (NodoSimbolo s : rhs) {
            sb.append(s.name).append(" ");
        }
        return sb.toString().trim();
    }
}
