package tinto.Output;

import auxiliares.ActionElement;
import auxiliares.SLRParser;

public class Parser extends SLRParser implements TokenConstants, SymbolConstants {

	public Parser() {
		initRules();
		initActionTable();
		initGotoTable();
	}

	private void initRules() {
		int[][] initRule = {
			{ Expr, 1 },
			{ Expr, 3 },
			{ Expr, 3 },
			{ Term, 1 },
			{ Term, 3 },
			{ Term, 3 },
			{ Factor, 1 },
			{ Factor, 3 },
			{ Factor, 4 },
			{ Args, 1 },
			{ Args, 0 },
			{ ArgumentList, 1 },
			{ ArgumentList, 3 },
		};
		this.rule = initRule;
	}

	private void initActionTable(){
		actionTable = new ActionElement[24][10];

		actionTable[0][NUM] = new ActionElement(ActionElement.SHIFT, 4);
		actionTable[0][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 5);
		actionTable[0][LPAREN] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[1][PLUS] = new ActionElement(ActionElement.SHIFT, 7);
		actionTable[1][EOF] = new ActionElement(ActionElement.ACCEPT, 0);
		actionTable[1][MINUS] = new ActionElement(ActionElement.SHIFT, 8);
		actionTable[2][COMMA] = new ActionElement(ActionElement.REDUCE, 1);
		actionTable[2][DIV] = new ActionElement(ActionElement.SHIFT, 10);
		actionTable[2][PLUS] = new ActionElement(ActionElement.REDUCE, 1);
		actionTable[2][RPAREN] = new ActionElement(ActionElement.REDUCE, 1);
		actionTable[2][PROD] = new ActionElement(ActionElement.SHIFT, 9);
		actionTable[2][EOF] = new ActionElement(ActionElement.REDUCE, 1);
		actionTable[2][MINUS] = new ActionElement(ActionElement.REDUCE, 1);
		actionTable[3][COMMA] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[3][DIV] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[3][PLUS] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[3][RPAREN] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[3][PROD] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[3][EOF] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[3][MINUS] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[4][COMMA] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[4][DIV] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[4][PLUS] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[4][RPAREN] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[4][PROD] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[4][EOF] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[4][MINUS] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[5][LPAREN] = new ActionElement(ActionElement.SHIFT, 11);
		actionTable[6][NUM] = new ActionElement(ActionElement.SHIFT, 4);
		actionTable[6][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 5);
		actionTable[6][LPAREN] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[7][NUM] = new ActionElement(ActionElement.SHIFT, 4);
		actionTable[7][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 5);
		actionTable[7][LPAREN] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[8][NUM] = new ActionElement(ActionElement.SHIFT, 4);
		actionTable[8][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 5);
		actionTable[8][LPAREN] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[9][NUM] = new ActionElement(ActionElement.SHIFT, 4);
		actionTable[9][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 5);
		actionTable[9][LPAREN] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[10][NUM] = new ActionElement(ActionElement.SHIFT, 4);
		actionTable[10][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 5);
		actionTable[10][LPAREN] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[11][NUM] = new ActionElement(ActionElement.SHIFT, 4);
		actionTable[11][RPAREN] = new ActionElement(ActionElement.REDUCE, 11);
		actionTable[11][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 5);
		actionTable[11][LPAREN] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[12][PLUS] = new ActionElement(ActionElement.SHIFT, 7);
		actionTable[12][RPAREN] = new ActionElement(ActionElement.SHIFT, 20);
		actionTable[12][MINUS] = new ActionElement(ActionElement.SHIFT, 8);
		actionTable[13][COMMA] = new ActionElement(ActionElement.REDUCE, 2);
		actionTable[13][DIV] = new ActionElement(ActionElement.SHIFT, 10);
		actionTable[13][PLUS] = new ActionElement(ActionElement.REDUCE, 2);
		actionTable[13][RPAREN] = new ActionElement(ActionElement.REDUCE, 2);
		actionTable[13][PROD] = new ActionElement(ActionElement.SHIFT, 9);
		actionTable[13][EOF] = new ActionElement(ActionElement.REDUCE, 2);
		actionTable[13][MINUS] = new ActionElement(ActionElement.REDUCE, 2);
		actionTable[14][COMMA] = new ActionElement(ActionElement.REDUCE, 3);
		actionTable[14][DIV] = new ActionElement(ActionElement.SHIFT, 10);
		actionTable[14][PLUS] = new ActionElement(ActionElement.REDUCE, 3);
		actionTable[14][RPAREN] = new ActionElement(ActionElement.REDUCE, 3);
		actionTable[14][PROD] = new ActionElement(ActionElement.SHIFT, 9);
		actionTable[14][EOF] = new ActionElement(ActionElement.REDUCE, 3);
		actionTable[14][MINUS] = new ActionElement(ActionElement.REDUCE, 3);
		actionTable[15][COMMA] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[15][DIV] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[15][PLUS] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[15][RPAREN] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[15][PROD] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[15][EOF] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[15][MINUS] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[16][COMMA] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[16][DIV] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[16][PLUS] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[16][RPAREN] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[16][PROD] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[16][EOF] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[16][MINUS] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[17][RPAREN] = new ActionElement(ActionElement.SHIFT, 21);
		actionTable[18][COMMA] = new ActionElement(ActionElement.SHIFT, 22);
		actionTable[18][RPAREN] = new ActionElement(ActionElement.REDUCE, 10);
		actionTable[19][COMMA] = new ActionElement(ActionElement.REDUCE, 12);
		actionTable[19][PLUS] = new ActionElement(ActionElement.SHIFT, 7);
		actionTable[19][RPAREN] = new ActionElement(ActionElement.REDUCE, 12);
		actionTable[19][MINUS] = new ActionElement(ActionElement.SHIFT, 8);
		actionTable[20][COMMA] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[20][DIV] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[20][PLUS] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[20][RPAREN] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[20][PROD] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[20][EOF] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[20][MINUS] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[21][COMMA] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[21][DIV] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[21][PLUS] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[21][RPAREN] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[21][PROD] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[21][EOF] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[21][MINUS] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[22][NUM] = new ActionElement(ActionElement.SHIFT, 4);
		actionTable[22][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 5);
		actionTable[22][LPAREN] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[23][COMMA] = new ActionElement(ActionElement.REDUCE, 13);
		actionTable[23][PLUS] = new ActionElement(ActionElement.SHIFT, 7);
		actionTable[23][RPAREN] = new ActionElement(ActionElement.REDUCE, 13);
		actionTable[23][MINUS] = new ActionElement(ActionElement.SHIFT, 8);
	}

	private void initGotoTable(){
		gotoTable = new int[24][5];

		gotoTable[0][Expr] = 1;
		gotoTable[0][Factor] = 3;
		gotoTable[0][Term] = 2;
		gotoTable[6][Expr] = 12;
		gotoTable[6][Factor] = 3;
		gotoTable[6][Term] = 2;
		gotoTable[7][Factor] = 3;
		gotoTable[7][Term] = 13;
		gotoTable[8][Factor] = 3;
		gotoTable[8][Term] = 14;
		gotoTable[9][Factor] = 15;
		gotoTable[10][Factor] = 16;
		gotoTable[11][Args] = 17;
		gotoTable[11][Expr] = 19;
		gotoTable[11][Factor] = 3;
		gotoTable[11][Term] = 2;
		gotoTable[11][ArgumentList] = 18;
		gotoTable[22][Expr] = 23;
		gotoTable[22][Factor] = 3;
		gotoTable[22][Term] = 2;
	}
}
