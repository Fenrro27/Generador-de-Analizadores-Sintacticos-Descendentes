package tinto.SLR;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TablaSLR {
    private final Map<Integer, Map<String, String>> action = new LinkedHashMap<>();
    private final Map<Integer, Map<String, Integer>> goTo = new LinkedHashMap<>();

    public Map<Integer, Map<String, String>> getAction() {
        return action;
    }

    public Map<Integer, Map<String, Integer>> getGoTo() {
        return goTo;
    }

    public void addAction(int estado, String simbolo, String accion) {
        action.computeIfAbsent(estado, k -> new HashMap<>()).put(simbolo, accion);
    }

    public void addGoto(int estado, String noTerminal, int destino) {
        goTo.computeIfAbsent(estado, k -> new HashMap<>()).put(noTerminal, destino);
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
