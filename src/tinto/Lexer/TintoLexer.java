//------------------------------------------------------------------//
//                        COPYRIGHT NOTICE                          //
//------------------------------------------------------------------//
// Copyright (c) 2017, Francisco Jos� Moreno Velo                   //
// All rights reserved.                                             //
//                                                                  //
// Redistribution and use in source and binary forms, with or       //
// without modification, are permitted provided that the following  //
// conditions are met:                                              //
//                                                                  //
// * Redistributions of source code must retain the above copyright //
//   notice, this list of conditions and the following disclaimer.  // 
//                                                                  //
// * Redistributions in binary form must reproduce the above        // 
//   copyright notice, this list of conditions and the following    // 
//   disclaimer in the documentation and/or other materials         // 
//   provided with the distribution.                                //
//                                                                  //
// * Neither the name of the University of Huelva nor the names of  //
//   its contributors may be used to endorse or promote products    //
//   derived from this software without specific prior written      // 
//   permission.                                                    //
//                                                                  //
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND           // 
// CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,      // 
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF         // 
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE         // 
// DISCLAIMED. IN NO EVENT SHALL THE COPRIGHT OWNER OR CONTRIBUTORS //
// BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,         // 
// EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED  //
// TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,    //
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND   // 
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT          //
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING   //
// IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF   //
// THE POSSIBILITY OF SUCH DAMAGE.                                  //
//------------------------------------------------------------------//

//------------------------------------------------------------------//
//                      Universidad de Huelva                       //
//           Departamento de Tecnolog�as de la Informaci�n          //
//   �rea de Ciencias de la Computaci�n e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                     PROCESADORES DE LENGUAJE                     //
//------------------------------------------------------------------//
//                                                                  //
//                  Compilador del lenguaje Tinto                   //
//                                                                  //
//------------------------------------------------------------------//

package tinto.Lexer;

import java.io.*;

/**
 * Clase que desarrolla el analizador l�xico de Tinto
 * 
 * @author Francisco Jos� Moreno Velo
 *
 */
public class TintoLexer extends Lexer implements TokenConstants {

	/**
	 * Transiciones del aut�mata del analizador l�xico
	 * 
	 * @param state Estado inicial
	 * @param symbol S�mbolo del alfabeto
	 * @return Estado final
	 */
	protected int transition(int state, char symbol) 
	{
		switch(state) 
		{
			case 0: 
				if(symbol == '/') return 1;
				else if(symbol == ' ' || symbol == '\t'|| symbol == '\r' 
				|| symbol == '\n') return 7;
				else if(symbol == '_' || symbol >= 'a' && symbol <= 'z' 
				|| symbol >= 'A' && symbol <= 'Z') return 8;
				else if(symbol == '<') return 9;
				else if(symbol == ':') return 12;
				else if(symbol == '|') return 15;
				else if(symbol == ';') return 16;
				else return -1;
			case 1:
				if(symbol == '*') return 2;
				else if(symbol == '/') return 5;
				else return -1;
			case 2:
				if(symbol == '*') return 3;
				else return 2;
			case 3:
				if(symbol == '*') return 3;
				else if(symbol == '/') return 4;
				else return 2;
			case 5:
				if(symbol == '\n') return 6;
				else return 5;
			case 7:
				if(symbol == ' ' || symbol == '\t' || 
				symbol == '\r' || symbol == '\n') return 7;
				else return -1;
			case 8:
				if(symbol == '_' || symbol >= 'a' && symbol <= 'z' ||
				symbol >= 'A' && symbol <= 'Z' || 
				symbol >= '0' && symbol <= '9') return 8;
				else return -1;
			case 9:
				if(symbol == '_' || symbol >= 'a' && symbol <= 'z' ||
				symbol >= 'A' && symbol <= 'Z' ) return 10;
				else return -1;
			case 10:
				if(symbol == '_' || symbol >= 'a' && symbol <= 'z' ||
				symbol >= 'A' && symbol <= 'Z' || 
				symbol >= '0' && symbol <= '9') return 10;
				else if(symbol == '>') return 11;
				else return -1;
			case 12:
				if(symbol == ':' ) return 13;
				else return -1;
			case 13:
				if(symbol == '=') return 14;
				else return -1;
			default:
				return -1;
		}
	}
	
	/**
	 * Verifica si un estado es final
	 * 
	 * @param state Estado
	 * @return true, si el estado es final
	 */
	protected boolean isFinal(int state) 
	{
		if(state <=0 || state > 16) return false;
		switch(state) 
		{
			case 4:
			case 6:
			case 7:
			case 8:
			case 11:
			case 14:
			case 15:
			case 16:
				return true;
			default: 
				return false;
		}
	}
	
	/**
	 * Genera el componente l�xico correspondiente al estado final y
	 * al lexema encontrado. Devuelve null si la acci�n asociada al
	 * estado final es omitir (SKIP).
	 * 
	 * @param state Estado final alcanzado
	 * @param lexeme Lexema reconocido
	 * @param row Fila de comienzo del lexema
	 * @param column Columna de comienzo del lexema
	 * @return Componente l�xico correspondiente al estado final y al lexema
	 */
	protected Token getToken(int state, String lexeme, int row, int column) 
	{
		switch(state) 
		{
			case 8: return new Token(TokenConstants.NOTERMINAL,lexeme, row, column);
			case 11: return new Token(TokenConstants.TERMINAL,lexeme, row, column);
			case 14: return new Token(TokenConstants.EQ,lexeme, row, column);
			case 15: return new Token(TokenConstants.BAR,lexeme, row, column);
			case 16: return new Token(TokenConstants.SEMICOLON,lexeme, row, column);

			default: return null;
		}
	}
	
	
	/**
	 * Constructor
	 * @param filename Nombre del fichero fuente
	 * @throws IOException En caso de problemas con el flujo de entrada
	 */
	public TintoLexer(File file) throws IOException 
	{
		super(file);
	}
	
}
