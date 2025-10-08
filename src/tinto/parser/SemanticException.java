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
//          Departamento de Tecnolog�as de la Informaci�n           //
//   �rea de Ciencias de la Computaci�n e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                     PROCESADORES DE LENGUAJE                     //
//------------------------------------------------------------------//
//                                                                  //
//                  Compilador del lenguaje Tinto                   //
//                                                                  //
//------------------------------------------------------------------//

package tinto.parser;

import tinto.Lexer.Token;

/**
 * Clase que describe un excepci�n sint�ctica
 * 
 * @author Francisco Jos� Moreno Velo
 */
public class SemanticException extends Exception {

	/**
	 * N�mero de serie
	 */
	private static final long serialVersionUID = 8318848237596856683L;

	// --------------------------------------------------------------//
	// Constantes que describen a los errores sem�nticos //
	// --------------------------------------------------------------//
	
	/** Error: no terminal usado en la derecha no está definido en la izquierda */
	public static final int UNDEFINED_NONTERMINAL_EXCEPTION = 1;
    /** Error: conflicto SLR (shift/reduce o reduce/reduce) detectado */
    public static final int CONFLICT_SLR_EXCEPTION = 2;

	// ----------------------------------------------------------------//
	// Miembros privados //
	// ----------------------------------------------------------------//

	/**
	 * Mensaje de error
	 */
	private String msg;

	// ----------------------------------------------------------------//
	// Constructores //
	// ----------------------------------------------------------------//

	/**
	 * Constructor con un solo tipo esperado
	 * 
	 * @param token
	 * @param expected
	 */
	public SemanticException(int code, Token token) {
		this.msg = "Parse exception at row " + token.getRow();
		msg += ", column " + token.getColumn() + ".\n";
		msg += getExplanationForCode(code, token) + "\n";
	}


	
    /**
     * Constructor genérico sin token (para conflictos de tabla SLR)
     * 
     * @param code   tipo de error
     * @param detail descripción adicional del conflicto
     */
    public SemanticException(int code, String detail) {
        this.msg = "Semantic exception: " + getExplanationForCode(code, detail);
    }

	// ----------------------------------------------------------------//
	// M�todos p�blicos //
	// ----------------------------------------------------------------//

	/**
	 * Obtiene el mensaje de error
	 */
	public String toString() {
		return this.msg;
	}

	/**
	 * Obtiene la descripci�n del error
	 * 
	 * @param code
	 * @return
	 */
	private static String getExplanationForCode(int code, Token token) {
		switch (code) {
			 case UNDEFINED_NONTERMINAL_EXCEPTION:
                return "  Undefined non-terminal: '" + token.getLexeme() + "'";
		
			default:
				return "";
		}
	}

	/**
     * Obtiene la descripción del error en función del código.
     * 
     * @param code  tipo de error
     * @param token o texto adicional
     * @return descripción del error
     */
    private static String getExplanationForCode(int code, Object info) {
        switch (code) {
            case UNDEFINED_NONTERMINAL_EXCEPTION:
                Token t = (Token) info;
                return "  Undefined non-terminal: '" + t.getLexeme() + "'";

            case CONFLICT_SLR_EXCEPTION:
                return "  Conflict detected in SLR table: " + info;

            default:
                return "  Unknown semantic error.";
        }
    }
}
