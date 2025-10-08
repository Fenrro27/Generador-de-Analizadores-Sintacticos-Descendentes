package tinto.SLR;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import tinto.ast.NodoDefinicion;
import tinto.ast.NodoGramatica;
import tinto.ast.NodoListaReglas;
import tinto.ast.NodoRegla;
import tinto.ast.NodoSimbolo;

public class GeneradorClases {

    private final AutomataLR automata;
    private final TablaSLR tabla;
    private final NodoGramatica arbol;

    public GeneradorClases(NodoGramatica arbol, AutomataLR automata, TablaSLR tabla) {
        this.automata = automata;
        this.tabla = tabla;
        this.arbol = arbol;
    }

    public void generarClases(String rutaSalida) throws IOException {
        generarTokenConstants(rutaSalida);
        generarSymbolConstants(rutaSalida);
        generarParser(rutaSalida);
    }

    private void generarTokenConstants(String ruta) throws IOException {
        LinkedHashSet<String> terminales = new LinkedHashSet<>();

        Set<String> noterminales = new LinkedHashSet<>();
        for (NodoDefinicion def : arbol.definitions) {
            noterminales.add(def.nonTerminal);
        }

        for (NodoDefinicion def : arbol.definitions) {
            for (NodoListaReglas lista : def.rulesList) {
                for (NodoRegla regla : lista.rules) {
                    for (NodoSimbolo simbolo : regla.getSimbolosLineales()) {
                        if (simbolo.isTerminal && !noterminales.contains(simbolo.name)) {
                            terminales.add(simbolo.name);
                        }
                    }
                }
            }
        }

        // Generamos el fichero
        try (FileWriter fw = new FileWriter(ruta + "/TokenConstants.java")) {
            fw.write("package Output;\n\n");
            fw.write("public interface TokenConstants {\n\n");
            fw.write("\tpublic int EOF = 0;\n");
            int index = 1;
            for (String terminal : terminales) {
                String nombreLimpio = terminal.replaceAll("[<>]", "");
                fw.write("\tpublic int " + nombreLimpio + " = " + index + ";\n");
                index++;
            }
            fw.write("}\n");
        }
    }

    private void generarSymbolConstants(String ruta) throws IOException {
        try (FileWriter fw = new FileWriter(ruta + "/SymbolConstants.java")) {
            fw.write("package Output;\n\n");
            fw.write("public interface SymbolConstants {\n\n");

            int index = 0;
            for (NodoDefinicion def : arbol.definitions) {
                fw.write("\tpublic int " + def.nonTerminal + " = " + index + ";\n");
                index++;
            }

            fw.write("}\n");
        }
    }

    private void generarParser(String ruta) throws IOException {
        List<Produccion> producciones = automata.getProducciones();
        Set<String> terminales = automata.getTerminales();
        Set<String> noterminales = automata.getNoTerminales();

        try (FileWriter fw = new FileWriter(ruta + "/Parser.java")) {
            fw.write("package Output;\n\n");
            fw.write("// Los metodos del paquete auxiliares son los correspondientes a Francisco Jose Moreno Velo\n");
            fw.write("import auxiliares.ActionElement;\n");
            fw.write("import auxiliares.SLRParser;\n\n");

            fw.write("public class Parser extends SLRParser implements TokenConstants, SymbolConstants {\n\n");
            fw.write("\tpublic Parser() {\n");
            fw.write("\t\tinitRules();\n");
            fw.write("\t\tinitActionTable();\n");
            fw.write("\t\tinitGotoTable();\n");
            fw.write("\t}\n\n");

            fw.write("\tprivate void initRules() {\n");
            fw.write("\t\tint[][] initRule = {\n");

            for (Produccion p : producciones) {
                int rhsSize = p.getRhs().size();
                fw.write("\t\t\t{ " + p.getLhs() + ", " + rhsSize + " },\n");
            }

            fw.write("\t\t};\n");
            fw.write("\t\tthis.rule = initRule;\n");
            fw.write("\t}\n\n");

            // Tabla ACTION
            fw.write("\tprivate void initActionTable(){\n");
            fw.write("\t\tactionTable = new ActionElement[" + automata.getEstados().size() + "][" + (terminales.size()+1)
                    + "];\n\n");
            for (var entry : tabla.getActionEntries()) {
                int estado = entry.getKey();
                for (var act : entry.getValue().entrySet()) {
                    String simbolo = act.getKey().replaceAll("[<>]", "").toUpperCase();
                    String accion = act.getValue();

                    if (accion.startsWith("shift")) {
                        int destino = Integer.parseInt(accion.split(" ")[1]);
                        fw.write("\t\tactionTable[" + estado + "][" + simbolo
                                + "] = new ActionElement(ActionElement.SHIFT, " + destino + ");\n");
                    } else if (accion.startsWith("reduce")) {
                        int prodId = Integer.parseInt(accion.split(" ")[1]);
                        fw.write("\t\tactionTable[" + estado + "][" + simbolo
                                + "] = new ActionElement(ActionElement.REDUCE, " + prodId + ");\n");
                    } else if (accion.equals("accept")) {
                        fw.write("\t\tactionTable[" + estado + "][" + simbolo
                                + "] = new ActionElement(ActionElement.ACCEPT, 0);\n");
                    }
                }
            }
            fw.write("\t}\n\n");

            // Tabla GOTO
            fw.write("\tprivate void initGotoTable(){\n");
            fw.write("\t\tgotoTable = new int[" + automata.getEstados().size() + "][" + noterminales.size() + "];\n\n");
            for (var entry : tabla.getGotoEntries()) {
                int estado = entry.getKey();
                for (var g : entry.getValue().entrySet()) {
                    String nt = g.getKey();
                    int destino = g.getValue();
                    fw.write("\t\tgotoTable[" + estado + "][" + nt + "] = " + destino + ";\n");
                }
            }
            fw.write("\t}\n");
            fw.write("}\n");
        }
    }
}
