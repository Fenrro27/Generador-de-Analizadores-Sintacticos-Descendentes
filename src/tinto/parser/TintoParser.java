package tinto.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import tinto.Lexer.TintoLexer;
import tinto.Lexer.Token;
import tinto.Lexer.TokenConstants;
import tinto.ast.NodoDefinicion;
import tinto.ast.NodoGramatica;
import tinto.ast.NodoListaReglas;
import tinto.ast.NodoRegla;
import tinto.ast.NodoSimbolo;

public class TintoParser implements TokenConstants {

	private TintoLexer lexer;

	/**
	 * ultimo token analizado
	 */
	private Token prevToken;

	private Token nextToken;

	/**
	 * Contador de errores
	 */
	private int errorCount;

	/**
	 * Mensaje de errores
	 */
	private String errorMsg;

	public NodoGramatica nodoRaiz = null;

	//
	ArrayList<Token> parteDerRegla = new ArrayList<>();
	ArrayList<Token> parteIzqRegla = new ArrayList<>();

	public Exception getError() {
		return new Exception("NErrores: " + errorCount + ":\n" + errorMsg);
	}

	/**
	 * Almacena un error de an�lisis
	 * 
	 * @param ex
	 */
	private void catchError(Exception ex) {
		this.errorCount++;
		this.errorMsg += ex.toString();
	}

	/**
	 * Sincroniza la cadena de tokens
	 * 
	 * @param left
	 * @param right
	 */
	private void skipTo(int[] left, int[] right) {
		boolean flag = false;
		if (prevToken.getKind() == EOF || nextToken.getKind() == EOF)
			flag = true;
		for (int i = 0; i < left.length; i++)
			if (prevToken.getKind() == left[i])
				flag = true;
		for (int i = 0; i < right.length; i++)
			if (nextToken.getKind() == right[i])
				flag = true;

		while (!flag) {
			prevToken = nextToken;
			nextToken = lexer.getNextToken();
			if (prevToken.getKind() == EOF || nextToken.getKind() == EOF)
				flag = true;
			for (int i = 0; i < left.length; i++)
				if (prevToken.getKind() == left[i])
					flag = true;
			for (int i = 0; i < right.length; i++)
				if (nextToken.getKind() == right[i])
					flag = true;
		}
	}

	/**
	 * M�todo de an�lisis de un fichero
	 * 
	 * @param file Fichero a analizar
	 * @return Resultado del an�lisis sint�ctico
	 */
	public boolean parse(File file) throws IOException, SintaxException {
		this.lexer = new TintoLexer(file);
		this.prevToken = new Token(EOF, "", 0, 0); // Token ficticio para evitar null
		this.nextToken = lexer.getNextToken();
		nodoRaiz = tryGramatica();
		if (nextToken.getKind() == EOF)
			if(VerifyNoterminal()) return true;
			else return false;
		else
			return false;
	}


	/**
	 * 
	 * @return
	 */
	private boolean VerifyNoterminal() {
    // Set con los lexemas de los no terminales definidos
    Set<String> definidos = new HashSet<>();
    for (Token t : parteIzqRegla) {
        definidos.add(t.getLexeme());
    }

    boolean ok = true;

    // Revisar los no terminales usados en la derecha
    for (Token t : parteDerRegla) {
        if (!definidos.contains(t.getLexeme())) {
            int errorcode = SemanticException.UNDEFINED_NONTERMINAL_EXCEPTION;
            catchError(new SemanticException(errorcode, t));
            ok = false;
        }
    }

    return ok;
}



	/**
	 * M�todo que consume un token de la cadena de entrada
	 * 
	 * @param kind Tipo de token a consumir
	 * @throws SintaxException Si el tipo no coincide con el token
	 */
	private void match(int kind) throws SintaxException {
		if (nextToken.getKind() == kind)
			nextToken = lexer.getNextToken();
		else
			throw new SintaxException(nextToken, kind);
	}

	private NodoGramatica tryGramatica() {
		NodoGramatica nd = new NodoGramatica(new ArrayList<>());
		int[] lsync = {}; // Tokens q dejo a la izquierda , ;
		int[] rsync = { NOTERMINAL, EOF };// Me quedo en un punto en el que el siguiente token es un punto de este
											// conjunto, conjunto siguientes, |

		try {
			nd = parseGramatica();
		} catch (Exception ex) {
			catchError(ex);
			skipTo(lsync, rsync);
		}
		return nd;
	}

	private NodoGramatica parseGramatica() throws SintaxException {
		NodoGramatica nd = null;
		int[] expected = { NOTERMINAL, EOF };
		switch (nextToken.getKind()) {
			case NOTERMINAL:
			case EOF:
				nd = new NodoGramatica(tryGramaticaPrima());
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
		return nd;
	}

	private ArrayList<NodoDefinicion> tryGramaticaPrima() {
		ArrayList<NodoDefinicion> ad = new ArrayList<>();
		int[] lsync = { NOTERMINAL, EOF };
		int[] rsync = { NOTERMINAL, EOF };

		try {
			ad = parseGramaticaPrima();
		} catch (Exception ex) {
			catchError(ex);
			skipTo(lsync, rsync);
		}
		return ad;
	}

	private ArrayList<NodoDefinicion> parseGramaticaPrima() throws SintaxException {
		ArrayList<NodoDefinicion> ad = new ArrayList<>();
		int[] expected = { NOTERMINAL, EOF };
		switch (nextToken.getKind()) {
			case NOTERMINAL:
				ad.add(new NodoDefinicion(nextToken.getLexeme(), tryDefinicion()));
				ad.addAll(tryGramaticaPrima());
				break;
			case EOF:
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
		return ad;
	}

	private ArrayList<NodoListaReglas> tryDefinicion() {
		ArrayList<NodoListaReglas> anlr = new ArrayList<>();
		int[] lsync = { NOTERMINAL, EOF };
		int[] rsync = { NOTERMINAL };

		try {
			anlr = parseDefinicion();
		} catch (Exception ex) {
			catchError(ex);
			skipTo(lsync, rsync);
		}
		return anlr;
	}

	private ArrayList<NodoListaReglas> parseDefinicion() throws SintaxException {
		ArrayList<NodoListaReglas> anlr = new ArrayList<>();
		int[] expected = { NOTERMINAL };
		switch (nextToken.getKind()) {
			case NOTERMINAL:
				parteIzqRegla.add(nextToken);
				match(NOTERMINAL);
				match(EQ);
				anlr.add(new NodoListaReglas(tryListaReglas()));
				match(SEMICOLON);
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
		return anlr;
	}

	private ArrayList<NodoRegla> tryListaReglas() {
		ArrayList<NodoRegla> anr = new ArrayList<>();

		int[] lsync = { SEMICOLON };
		int[] rsync = { TERMINAL, NOTERMINAL };

		try {
			anr = parseListaReglas();
		} catch (Exception ex) {
			catchError(ex);
			skipTo(lsync, rsync);
		}
		return anr;
	}

	private ArrayList<NodoRegla> parseListaReglas() throws SintaxException {
		ArrayList<NodoRegla> anr = new ArrayList<>();

		int[] expected = { TERMINAL, NOTERMINAL,BAR, SEMICOLON };
		switch (nextToken.getKind()) {
			case TERMINAL:
			case NOTERMINAL:
			case BAR:
			case SEMICOLON:
				anr.add(new NodoRegla(tryRegla()));
				anr.addAll(tryListaReglasPrima());
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
		return anr;
	}

	private ArrayList<NodoRegla> tryListaReglasPrima() {
		ArrayList<NodoRegla> anr = new ArrayList<>();
		int[] lsync = { SEMICOLON };
		int[] rsync = { BAR, SEMICOLON };

		try {
			anr = parseListaReglasPrima();
		} catch (Exception ex) {
			catchError(ex);
			skipTo(lsync, rsync);
		}
		return anr;
	}

	private ArrayList<NodoRegla> parseListaReglasPrima() throws SintaxException {
		ArrayList<NodoRegla> anr = new ArrayList<>();

		int[] expected = { BAR, SEMICOLON };
		switch (nextToken.getKind()) {
			case BAR:
				match(BAR);
				anr.add(new NodoRegla(tryRegla()));
				anr.addAll(tryListaReglasPrima());
				break;
			case SEMICOLON:
				break;
			default:
				throw new SintaxException(nextToken, expected);
		}
		return anr;
	}

	private ArrayList<NodoSimbolo> tryRegla() {
		ArrayList<NodoSimbolo> ans = new ArrayList<>();

		int[] lsync = { BAR, SEMICOLON };
		int[] rsync = { TERMINAL, NOTERMINAL, BAR, SEMICOLON };

		try {
			ans = parseRegla();
		} catch (Exception ex) {
			catchError(ex);
			skipTo(lsync, rsync);
		}
		return ans;
	}

	private ArrayList<NodoSimbolo> parseRegla() throws SintaxException {

		ArrayList<NodoSimbolo> ans = new ArrayList<>();
		int[] expected = { BAR, SEMICOLON, TERMINAL, NOTERMINAL };
		switch (nextToken.getKind()) {
			case BAR:
			case SEMICOLON:

				break;
			case TERMINAL:
			case NOTERMINAL:
				ans.add(trySimbolo());
				ans.addAll(tryRegla());
				break;

			default:
				throw new SintaxException(nextToken, expected);
		}
		return ans;
	}

	private NodoSimbolo trySimbolo() {
		NodoSimbolo n = new NodoSimbolo(true, "");// dummy

		int[] lsync = { TERMINAL, NOTERMINAL, BAR, SEMICOLON };
		int[] rsync = { TERMINAL, NOTERMINAL };

		try {
			n = parseSimbolo();
		} catch (Exception ex) {
			catchError(ex);
			skipTo(lsync, rsync);
		}
		return n;
	}

	private NodoSimbolo parseSimbolo() throws SintaxException {
		NodoSimbolo n;

		int[] expected = { TERMINAL, NOTERMINAL };
		switch (nextToken.getKind()) {
			case TERMINAL:
				n = new NodoSimbolo(true, nextToken.getLexeme());
				match(TERMINAL);
				break;
			case NOTERMINAL:
				parteDerRegla.add(nextToken);
				n = new NodoSimbolo(false, nextToken.getLexeme());
				match(NOTERMINAL);
				break;

			default:
				throw new SintaxException(nextToken, expected);
		}
		return n;
	}

}
