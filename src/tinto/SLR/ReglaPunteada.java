package tinto.SLR;

import java.util.List;
import java.util.Objects;

import tinto.ast.NodoSimbolo;

public class ReglaPunteada {

    public final String left; // No terminal de la producción
    public final List<NodoSimbolo> right; // Parte derecha de la producción
    public final int position; // Posición del punto (•)

    public ReglaPunteada(String left, List<NodoSimbolo> right, int position) {
        this.left = left;
        this.right = right;
        this.position = position;
    }

    public boolean isComplete() {
        return position >= right.size();
    }

    public NodoSimbolo getNextSymbol() {
        return isComplete() ? null : right.get(position);
    }

    public ReglaPunteada advance() {
        return new ReglaPunteada(left, right, position + 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(left).append(" -> ");
        for (int i = 0; i < right.size(); i++) {
            if (i == position)
                sb.append(". ");
            sb.append(right.get(i).name).append(" ");
        }
        if (position == right.size())
            sb.append(".");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ReglaPunteada))
            return false;
        ReglaPunteada other = (ReglaPunteada) o;
        return position == other.position &&
                left.equals(other.left) &&
                right.equals(other.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, position);
    }

}
