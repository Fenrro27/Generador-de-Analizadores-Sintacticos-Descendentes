package tinto.SLR;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tinto.ast.*;

public class ConjuntoSiguientes {

    private final NodoGramatica arbol;
    private final Map<String, Set<String>> first; 
    private final Map<String, Set<String>> follow = new HashMap<>();

    public ConjuntoSiguientes(NodoGramatica arbol, Map<String, Set<String>> first) {
        this.arbol = arbol;
        this.first = first;
    }

    public Map<String, Set<String>> calcular() {
        // Inicializar FOLLOW(A) = {}
        for (NodoDefinicion def : arbol.definitions) {
            follow.put(def.nonTerminal, new HashSet<>());
        }

        // Regla 1: el inicial recibe $
        String inicial = arbol.definitions.get(0).nonTerminal;
        follow.get(inicial).add("EOF");

        boolean cambiado;
        do {
            cambiado = false;

            for (NodoDefinicion def : arbol.definitions) {
                String A = def.nonTerminal;

                for (NodoListaReglas lista : def.rulesList) {
                    for (NodoRegla regla : lista.rules) {
                        List<NodoSimbolo> simbolos = regla.getSimbolosLineales();

                        for (int i = 0; i < simbolos.size(); i++) {
                            NodoSimbolo X = simbolos.get(i);
                            if (!X.isTerminal) { // solo no terminales
                                Set<String> followX = follow.get(X.name);

                                if (i + 1 < simbolos.size()) {
                                    NodoSimbolo beta = simbolos.get(i + 1);

                                    if (beta.isTerminal) {
                                        if (followX.add(beta.name)) {
                                            cambiado = true;
                                        }
                                    } else {
                                        Set<String> firstBeta = first.get(beta.name);
                                        for (String f : firstBeta) {
                                            if (!f.equals("lambda") && followX.add(f)) {
                                                cambiado = true;
                                            }
                                        }
                                        if (firstBeta.contains("lambda")) {
                                            if (followX.addAll(follow.get(A))) {
                                                cambiado = true;
                                            }
                                        }
                                    }
                                } else {
                                    if (followX.addAll(follow.get(A))) {
                                        cambiado = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } while (cambiado);

        return follow;
    }

    @Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Conjuntos FOLLOW:\n");
    for (Map.Entry<String, Set<String>> entry : follow.entrySet()) {
        sb.append("FOLLOW(")
          .append(entry.getKey())
          .append(") = { ");

        int i = 0;
        for (String simbolo : entry.getValue()) {
            sb.append(simbolo);
            if (i < entry.getValue().size() - 1) {
                sb.append(", ");
            }
            i++;
        }
        sb.append(" }\n");
    }
    return sb.toString();
}

}
