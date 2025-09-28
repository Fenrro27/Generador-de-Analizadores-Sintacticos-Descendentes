# Generador-de-Analizadores-Sint-cticos-Descendentes (SLR) en Java

Este repositorio contiene una implementación **manual** de un analizador sintáctico ascendente tipo **SLR (Simple LR)**, construido a partir de una gramática formal expresada en notación **BNF**. El proyecto está desarrollado en el contexto de la asignatura **Procesadores de Lenguajes** de la Universidad de Huelva, tomando como referencia el material y el código del profesor **Francisco José Moreno Velo**.

---

## ✨ Objetivo

El propósito del proyecto es desarrollar, sin apoyarse en generadores automáticos, una aplicación que:

1. Tome como entrada la descripción de una **gramática en BNF**.
2. Calcule la **tabla de desplazamiento/reducción** mediante el algoritmo **SLR**.
3. Genere automáticamente los siguientes ficheros Java:

   * `TokenConstants.java`
   * `SymbolConstants.java`
   * `Parser.java`

Estos ficheros implementan el **analizador sintáctico ascendente** para la gramática de entrada.

---

## ⚙️ Funcionamiento

1. El usuario introduce una gramática en **BNF**.
2. El programa procesa la gramática y construye los **conjuntos LR(0)**.
3. A partir de ellos se genera la **tabla SLR** con sus acciones de desplazamiento, reducción y aceptación.
4. Finalmente, se generan los ficheros Java que conforman el **parser** específico para dicha gramática.

---

## 📖 Ejemplo de Gramática en BNF

Archivo `ejemplo.bnf`:

```
<Expr>   ::= <Expr> "+" <Term> | <Term>
<Term>   ::= <Term> "*" <Factor> | <Factor>
<Factor> ::= "(" <Expr> ")" | id
```

---

## 🏛️ Créditos

* Basado en el material docente de la asignatura **Procesadores de Lenguajes**, Universidad de Huelva.
* Incluye referencias y código de **Francisco José Moreno Velo**.

---

## 📜 Licencia

Este proyecto incluye código y materiales basados en el trabajo de **Francisco José Moreno Velo** en el marco de la asignatura *Procesadores de Lenguajes* de la Universidad de Huelva.
El código original pertenece a su autor y está sujeto a sus condiciones de uso.

Las modificaciones, ampliaciones y documentación añadidas en este repositorio se distribuyen bajo licencia **MIT**, salvo indicación contraria en archivos específicos.
