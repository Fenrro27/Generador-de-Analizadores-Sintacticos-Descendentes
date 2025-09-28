package tinto.ast;

import java.util.*;

public class NodoGramatica extends iNodo{
    
    public final List<NodoDefinicion> definitions;

    public NodoGramatica(List<NodoDefinicion> defs) {
        this.definitions = defs;
    }

    public void addFirst(NodoDefinicion n){
        definitions.addFirst(n);
    }

/**
 * 
 * busca una definicion
 * @param nonTerminal No terminal al que vamos a buscar una definicion
 * @return definicion
 */
    public NodoDefinicion getDefinition(String nonTerminal) {
    for (NodoDefinicion def : definitions) {
        if (def.nonTerminal.equals(nonTerminal)) {
            return def;
        }
    }
    return null; // si no existe
}

@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < definitions.size(); i++) {
            sb.append(definitions.get(i).toString());
            if (i < definitions.size() - 1) {
                sb.append(" \n");
            }
        }
        return sb.toString();
    }

}



