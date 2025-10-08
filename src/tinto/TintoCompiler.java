//------------------------------------------------------------------//
//                        COPYRIGHT NOTICE                          //
//------------------------------------------------------------------//
// Copyright (c) 2017, Francisco José Moreno Velo                   //
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
//          Departamento de Tecnologías de la Información           //
//   Área de Ciencias de la Computación e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                                                                  //
//                  Compilador del lenguaje Tinto                   //
//                                                                  //
//------------------------------------------------------------------//

package tinto;

import java.io.*;
import java.util.Map;
import java.util.Set;

import tinto.SLR.*;
import tinto.ast.*;
import tinto.parser.*;

/**
 * Clase que desarrolla el punto de entrada al compilador.
 * 
 * @author Francisco José Moreno Velo
 * 
 */
public class TintoCompiler {

	/**
	 * Punto de entrada de la aplicación
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// El directorio de trabajo es siempre donde se ejecuta el programa
		File workingdir = new File(System.getProperty("user.dir"));

		try {
			// Nombre del archivo de entrada (.tinto)
			String filename = (args.length > 0 ? args[0] : "Main.tinto");
			File mainfile = new File(workingdir, filename);

			TintoParser parser = new TintoParser();
			boolean correcto = parser.parse(mainfile); // Iniciamos el parser

			if (correcto) {

				NodoGramatica ng = parser.nodoRaiz;
				// System.err.println("NodoRaiz nulo: "+(ng==null?"si":"no"));
				System.out.println("Arbol:\n" + ng + "\n");

				// Calculamos los conjuntos primeros y siguientes
				ConjuntoPrimeros cp = new ConjuntoPrimeros(ng);
				Map<String, Set<String>> firsts = cp.calcular();
				System.out.println(cp);
				ConjuntoSiguientes cs = new ConjuntoSiguientes(ng, firsts);
				Map<String, Set<String>> seconds = cs.calcular();
				System.out.println(cs);

				// Construir el automata
				AutomataLR automata = new AutomataLR(ng);
				automata.construir();
				System.out.println(automata);

				TablaSLR slr = automata.construirTablaSLR(seconds); // construimos la tabla SLR
				System.out.println(slr);

				GeneradorClases generador = new GeneradorClases(ng, automata, slr);

				// Crear el directorio de salida "Output" en el directorio de ejecución
				File outputDir = new File(workingdir, "Output");
				if (!outputDir.exists())
					outputDir.mkdirs();
				generador.generarClases(outputDir.getPath());
				System.out.println("✅ Clases generadas en " + outputDir.getAbsolutePath());

				printOutput(workingdir, "Correcto");
			} else {
				printOutput(workingdir, "Incorrecto");
				printError(workingdir, parser.getError());

			}
		} catch (Error err) {
			printError(workingdir, err);
			printOutput(workingdir, "Incorrecto");

		} catch (Exception ex) {
			printError(workingdir, ex);
			printOutput(workingdir, "Incorrecto");
		}
	}

	/**
	 * Genera el fichero de error
	 * 
	 * @param workingdir Directorio de trabajo
	 * @param e          Error a mostrar
	 */
	private static void printError(File workingdir, Throwable e) {
		try {
			FileOutputStream errorfile = new FileOutputStream(new File(workingdir, "TintocErrors.txt"));
			PrintStream errorStream = new PrintStream(errorfile);
			errorStream.println("[File Main.tinto] 1 error found:");
			errorStream.println(e.toString());
			errorStream.close();
		} catch (Exception ex) {
		}
	}

	/**
	 * Genera el fichero de salida
	 * 
	 * @param workingdir Directorio de trabajo
	 * @param e          Error a mostrar
	 */
	private static void printOutput(File workingdir, String msg) {
		try {
			FileOutputStream outputfile = new FileOutputStream(new File(workingdir, "TintocOutput.txt"));
			PrintStream stream = new PrintStream(outputfile);
			stream.println(msg);
			stream.close();
		} catch (Exception ex) {
		}
	}
}
