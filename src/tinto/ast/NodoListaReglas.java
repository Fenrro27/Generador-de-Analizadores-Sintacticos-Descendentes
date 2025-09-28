
package tinto.ast;

import java.util.List;

public class NodoListaReglas extends iNodo {

    public final List<NodoRegla> rules;

    public NodoListaReglas(List<NodoRegla> rules) {
        this.rules = rules;
    }

    public void addFirst(NodoRegla n) {
        rules.addFirst(n);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rules.size(); i++) {
            sb.append(rules.get(i).toString()+" ");
            if (i < rules.size() - 1) {
                sb.append(" | ");
            }
        }
        return sb.toString();
    }

}