package tinto.SLR;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class EstadoAutomata {

    private final Set<ReglaPunteada> items = new LinkedHashSet<>();

    public EstadoAutomata(Set<ReglaPunteada> items) {
        this.items.addAll(items);
    }

    public Set<ReglaPunteada> getReglaPunteadas() {
        return items;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Estado:\n");
        for (ReglaPunteada it : items) {
            sb.append("  ").append(it).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof EstadoAutomata))
            return false;
        EstadoAutomata other = (EstadoAutomata) o;
        return items.equals(other.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

}
