package tinto.SLR;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tinto.ast.*;

public class ConjuntoPrimeros {

    private final NodoGramatica arbol;
    private final Map<String, Set<String>> primeros = new HashMap<>();

    public ConjuntoPrimeros(NodoGramatica arbol) {
        this.arbol = arbol;
    }

    public Map<String, Set<String>> calcular() {
        // Inicializar: cada no terminal tiene conjunto vacío
        for (NodoDefinicion def : arbol.definitions) {
            primeros.put(def.nonTerminal, new HashSet<>());
        }

        boolean cambiado;
        do {
            cambiado = false;

            for (NodoDefinicion def : arbol.definitions) {
                Set<String> firstA = primeros.get(def.nonTerminal);

                for (NodoListaReglas lista : def.rulesList) {
                    // cada lista representa una producción
                    for (NodoRegla regla : lista.rules) {
                        List<NodoSimbolo> produccion = regla.getSimbolosLineales();

                        boolean nullable = true;
                        for (NodoSimbolo simbolo : produccion) {
                            if (simbolo.isTerminal) {
                                // regla 1: terminal
                                if (firstA.add(simbolo.name)) {
                                    cambiado = true;
                                }
                                nullable = false;
                                break; 
                            } else {
                                // no terminal → añadir sus FIRST
                                Set<String> firstB = primeros.get(simbolo.name);
                                for (String f : firstB) {
                                    if (!f.equals("lambda")) {
                                        if (firstA.add(f)) {
                                            cambiado = true;
                                        }
                                    }
                                }
                                // si no contiene lambda, paramos
                                if (!firstB.contains("lambda")) {
                                    nullable = false;
                                    break;
                                }
                            }
                        }
                        if (nullable) {
                            if (firstA.add("lambda")) {
                                cambiado = true;
                            }
                        }
                    }
                }
            }

        } while (cambiado);

        return primeros;
    }


    @Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Conjuntos FIRST:\n");
    for (Map.Entry<String, Set<String>> entry : primeros.entrySet()) {
        sb.append("FIRST(")
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