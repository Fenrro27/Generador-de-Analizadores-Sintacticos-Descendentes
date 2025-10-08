package tinto.SLR;

import java.util.*;
import tinto.ast.*;
import tinto.parser.SemanticException;

public class AutomataLR {

    private final NodoGramatica arbol;
    private final List<EstadoAutomata> estados = new ArrayList<>();
    private final List<Produccion> producciones = new ArrayList<>();

    private final Map<Set<ReglaPunteada>, Integer> stateIndexMap = new HashMap<>();

    public AutomataLR(NodoGramatica arbol) {
        this.arbol = arbol;
        construirProducciones();
    }

    private void construirProducciones() {
        int id = 0;
        for (NodoDefinicion def : arbol.definitions) {
            for (NodoListaReglas lista : def.rulesList) {
                for (NodoRegla regla : lista.rules) {
                    producciones.add(new Produccion(id++, def.nonTerminal, regla.getSimbolosLineales()));
                }
            }
        }
    }

    public List<Produccion> getProducciones() {
        return producciones;
    }

    public Set<String> getNoTerminales() {
        Set<String> nt = new LinkedHashSet<>();
        for (NodoDefinicion def : arbol.definitions) {
            nt.add(def.nonTerminal);
        }
        return nt;
    }

    public Set<String> getTerminales() {
        Set<String> t = new LinkedHashSet<>();
        for (Produccion p : producciones) {
            for (NodoSimbolo s : p.getRhs()) {
                if (s.isTerminal) {
                    t.add(s.name);
                }
            }
        }
        return t;
    }

    private Set<ReglaPunteada> closure(Set<ReglaPunteada> I) {
        Set<ReglaPunteada> C = new LinkedHashSet<>(I);
        boolean cambiado;
        do {
            cambiado = false;
            Set<ReglaPunteada> nuevos = new HashSet<>();
            for (ReglaPunteada it : new HashSet<>(C)) {
                NodoSimbolo X = it.getNextSymbol();
                if (X != null && !X.isTerminal) {
                    NodoDefinicion def = arbol.getDefinition(X.name);
                    if (def != null) {
                        for (NodoListaReglas lista : def.rulesList) {
                            for (NodoRegla regla : lista.rules) {
                                ReglaPunteada nuevo = new ReglaPunteada(def.nonTerminal,
                                        regla.getSimbolosLineales(), 0);
                                if (!C.contains(nuevo)) {
                                    nuevos.add(nuevo);
                                }
                            }
                        }
                    }
                }
            }
            if (!nuevos.isEmpty()) {
                C.addAll(nuevos);
                cambiado = true;
            }
        } while (cambiado);
        return C;
    }

    private Set<ReglaPunteada> goTo(Set<ReglaPunteada> I, NodoSimbolo X) {
        Set<ReglaPunteada> movidos = new HashSet<>();
        for (ReglaPunteada it : I) {
            if (X.equals(it.getNextSymbol())) {
                movidos.add(it.advance());
            }
        }
        return closure(movidos);
    }

    public void construir() {
        // Producción inicial aumentada: X →  (primer nonTerminal de arbol)
        NodoDefinicion inicial = arbol.definitions.get(0);
        ReglaPunteada itemInicial = new ReglaPunteada("X",
                List.of(new NodoSimbolo(false, inicial.nonTerminal)), 0);

        Set<ReglaPunteada> I0items = closure(Set.of(itemInicial));
        EstadoAutomata I0 = new EstadoAutomata(I0items);
        estados.add(I0);
        stateIndexMap.put(new HashSet<>(I0items), 0);

        // Cola para expandir estados y asignar índices de forma estable
        Queue<Integer> q = new ArrayDeque<>();
        q.add(0);

        while (!q.isEmpty()) {
            int idx = q.poll();
            EstadoAutomata E = estados.get(idx);

            // recoger simbolos que aparecen despues del punto en los items
            Map<NodoSimbolo, Set<ReglaPunteada>> moverPorSimbolo = new LinkedHashMap<>();
            for (ReglaPunteada it : E.getReglaPunteadas()) {
                NodoSimbolo X = it.getNextSymbol();
                if (X != null) {
                    moverPorSimbolo.computeIfAbsent(X, k -> new HashSet<>());
                }
            }

            for (NodoSimbolo X : moverPorSimbolo.keySet()) {
                Set<ReglaPunteada> Ir = goTo(E.getReglaPunteadas(), X);
                if (Ir.isEmpty()) continue;

                // usar copia para la llave
                Set<ReglaPunteada> key = new HashSet<>(Ir);
                Integer existente = stateIndexMap.get(key);
                if (existente == null) {
                    int newIndex = estados.size();
                    EstadoAutomata nuevoEstado = new EstadoAutomata(Ir);
                    estados.add(nuevoEstado);
                    stateIndexMap.put(key, newIndex);
                    q.add(newIndex);
                }
            }
        }
    }

    public List<EstadoAutomata> getEstados() {
        return estados;
    }

    private int getEstadoIndex(Set<ReglaPunteada> items) {
        Integer idx = stateIndexMap.get(new HashSet<>(items));
        return idx == null ? -1 : idx;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== AUTOMATA LR(0) =====\n");

        for (int i = 0; i < estados.size(); i++) {
            EstadoAutomata estado = estados.get(i);
            sb.append("Estado ").append(i).append(":\n");
            sb.append(String.format("  %-15s | %-40s | %-20s\n", "LHS", "Producción con punto", "Transiciones"));
            sb.append("  ").append("-".repeat(85)).append("\n");

            // calcular transiciones (símbolo -> estado)
            Map<String, Integer> transiciones = new LinkedHashMap<>();
            for (ReglaPunteada it : estado.getReglaPunteadas()) {
                NodoSimbolo X = it.getNextSymbol();
                if (X != null) {
                    Set<ReglaPunteada> dest = goTo(estado.getReglaPunteadas(), X);
                    int j = getEstadoIndex(dest);
                    if (j != -1) transiciones.put(X.name, j);
                }
            }

            boolean primera = true;
            for (ReglaPunteada it : estado.getReglaPunteadas()) {
                StringBuilder rhs = new StringBuilder();
                for (int j = 0; j < it.right.size(); j++) {
                    if (j == it.position) rhs.append(". ");
                    rhs.append(it.right.get(j).name).append(" ");
                }
                if (it.position == it.right.size()) rhs.append(".");

                String transStr = "";
                if (primera) {
                    StringBuilder sbt = new StringBuilder();
                    for (var e : transiciones.entrySet()) {
                        sbt.append(e.getKey()).append(" -> ").append(e.getValue()).append("  ");
                    }
                    transStr = sbt.toString().trim();
                    primera = false;
                }

                sb.append(String.format("  %-15s | %-40s | %-20s\n", it.left, rhs.toString().trim(), transStr));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public TablaSLR construirTablaSLR(Map<String, Set<String>> follow)  throws SemanticException {
        List<String> terminalesOrdenados = new ArrayList<>(getTerminales());
        Collections.sort(terminalesOrdenados);
        List<String> noTerminalesOrdenados = new ArrayList<>(getNoTerminales());
        Collections.sort(noTerminalesOrdenados);

        TablaSLR tabla = new TablaSLR();

        for (int i = 0; i < estados.size(); i++) {
            EstadoAutomata E = estados.get(i);

            for (ReglaPunteada it : E.getReglaPunteadas()) {
                NodoSimbolo X = it.getNextSymbol();

                if (X != null) {
                    int j = getEstadoIndex(goTo(E.getReglaPunteadas(), X));
                    if (X.isTerminal) {
                        tabla.addAction(i, X.name, "shift " + j);
                    } else {
                        tabla.addGoto(i, X.name, j);
                    }
                } else {
                    if (!it.left.equals("X")) {
                        Set<String> followA = follow.get(it.left);
                        if (followA != null) {
                            int prodId = buscarProduccion(it.left, it.right);
                            for (String a : followA) {
                                tabla.addAction(i, a, "reduce " + prodId);
                            }
                        }
                    } else {
                        tabla.addAction(i, "EOF", "accept");
                    }
                }
            }
        }
        return tabla;
    }

    private int buscarProduccion(String lhs, List<NodoSimbolo> rhs) {
        for (Produccion p : producciones) {
            if (p.getLhs().equals(lhs) && p.getRhs().equals(rhs)) {
                return p.getId()+1;// las reglas empiezan en 1
            }
        }
        return -1; // no encontrada
    }
}
