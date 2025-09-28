package tinto.ast;

import java.util.*;

public class NodoRegla extends iNodo {

    public final List<NodoSimbolo> simbolos; 

    public NodoRegla(List<NodoSimbolo> syms) {
        this.simbolos = syms;
    }

    public void addFirst(NodoSimbolo s) {
        simbolos.addFirst(s);
    }

    public List<NodoSimbolo> getSimbolosLineales() {
        return simbolos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < simbolos.size(); i++) {
            sb.append(simbolos.get(i).toString() + " ");
        }
        return sb.toString();
    }
}