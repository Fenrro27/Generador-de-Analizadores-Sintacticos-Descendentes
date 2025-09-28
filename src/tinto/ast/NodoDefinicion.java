package tinto.ast;

import java.util.*;

public class NodoDefinicion extends iNodo {

    public final String nonTerminal;
    public final List<NodoListaReglas> rulesList;

    public NodoDefinicion(String nt, List<NodoListaReglas> rulesList) {
        this.nonTerminal = nt;
        this.rulesList = rulesList;
    }

    public void addFirst(NodoListaReglas n) {
        rulesList.addFirst(n);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nonTerminal).append(" -> ");
        for (int i = 0; i < rulesList.size(); i++) {
            sb.append(rulesList.get(i).toString());
        }
        sb.append(";");

        return sb.toString();
    }
}
