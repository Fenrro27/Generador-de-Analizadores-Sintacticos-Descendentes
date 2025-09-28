package tinto.ast;
import java.util.Objects;

public class NodoSimbolo extends iNodo {

    public final boolean isTerminal;
    public final String name;

    public NodoSimbolo(boolean isTerminal, String name) {
        this.isTerminal = isTerminal;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodoSimbolo)) return false;
        NodoSimbolo that = (NodoSimbolo) o;
        return isTerminal == that.isTerminal && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isTerminal, name);
    }
}
