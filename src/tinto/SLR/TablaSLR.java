package tinto.SLR;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import tinto.parser.SemanticException;

public class TablaSLR {
    private final Map<Integer, Map<String, String>> action = new LinkedHashMap<>();
    private final Map<Integer, Map<String, Integer>> goTo = new LinkedHashMap<>();

    public Map<Integer, Map<String, String>> getAction() {
        return action;
    }

    public Map<Integer, Map<String, Integer>> getGoTo() {
        return goTo;
    }

    public void addAction(int estado, String simbolo, String accion) throws SemanticException {
        Map<String, String> fila = action.computeIfAbsent(estado, k -> new HashMap<>());
        String existente = fila.get(simbolo);

        if (existente != null && !existente.equals(accion)) {
            // conflicto detectado
            throw new SemanticException(
                    SemanticException.CONFLICT_SLR_EXCEPTION,
                    String.format("state %d, symbol '%s': '%s' vs '%s'",
                            estado, simbolo, existente, accion));
        }

        fila.put(simbolo, accion);
    }

    public void addGoto(int estado, String noTerminal, int destino) throws SemanticException {
        Map<String, Integer> fila = goTo.computeIfAbsent(estado, k -> new HashMap<>());
        Integer existente = fila.get(noTerminal);

        if (existente != null && !existente.equals(destino)) {
            throw new SemanticException(
                    SemanticException.CONFLICT_SLR_EXCEPTION,
                    String.format(
                            "Conflict in GOTO table at state %d, non-terminal '%s': existing %d vs new %d",
                            estado, noTerminal, existente, destino));
        }

        fila.put(noTerminal, destino);
    }

    public String getAction(int estado, String simbolo) {
        return action.getOrDefault(estado, Map.of()).get(simbolo);
    }

    public Integer getGoto(int estado, String noTerminal) {
        return goTo.getOrDefault(estado, Map.of()).get(noTerminal);
    }

    public Set<Map.Entry<Integer, Map<String, String>>> getActionEntries() {
        return action.entrySet();
    }

    public Set<Map.Entry<Integer, Map<String, Integer>>> getGotoEntries() {
        return goTo.entrySet();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== TABLA SLR =====\n");
        for (var e : action.entrySet()) {
            int estado = e.getKey();
            for (var a : e.getValue().entrySet()) {
                sb.append("action[").append(estado).append(", ")
                        .append(a.getKey()).append("] = ").append(a.getValue()).append("\n");
            }
        }
        for (var e : goTo.entrySet()) {
            int estado = e.getKey();
            for (var g : e.getValue().entrySet()) {
                sb.append("goto[").append(estado).append(", ")
                        .append(g.getKey()).append("] = ").append(g.getValue()).append("\n");
            }
        }
        return sb.toString();
    }
}
