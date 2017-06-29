package tp1;

import static org.junit.Assert.*;

import org.junit.Test;

public class TDD_Parser {

	public double evaluar(String formula){
		/*-- Este metodo evaula una formula sin errores, es decir que se debe chequear antes por errores --*/
		
		int i;
		String termino = "";
		String subtermino = ""; //un termino dentro de un parentesis
		for (i=0; i< formula.length(); i++){ //recorro el string para separar terminos por + y -
			char caracter = formula.charAt(i);
			switch (caracter) {
			case ' ':
				break; //ignora espacios
			case '+': 
				return evaluar(termino) + evaluar(formula.substring(i+1));
			case '-':
				return evaluar(termino) - evaluar(formula.substring(i+1));
			case '(':
				subtermino = saltearParentesis(formula.substring(i));
				termino += subtermino;
				i+= subtermino.length() -1;
				break;
				
			default: 
				termino += caracter; 
				break;
			}
	      }
		
		/*-- A este punto llega una formula solo si no tiene sumas ni restas --*/	
		
		termino = ""; //reseteo el "termino" para recorrer el string nuevamente

		for (i=0; i< formula.length(); i++){
			char caracter = formula.charAt(i);
			switch (caracter) {
			case ' ':
				break;
			case '/': 
				return evaluar(termino) / evaluar(formula.substring(i+1));
			case '*':
				return evaluar(termino) * evaluar(formula.substring(i+1));
			case '(':
				subtermino = saltearParentesis(formula.substring(i));
				termino += subtermino;
				i+= subtermino.length() -1;
				break;
			default: 
				termino += caracter; 
				break;
			} 
		}
		

		if (termino == "")return 0;
		/*--- A este punto llega una formula entre (), Ej: "(8+3*4)" --*/	
		if(formula.charAt(0) == '('){
			// entrada: "(8+3*4)" evaluo: "8+3*4" 
			return evaluar(formula.substring(1, formula.length()-1));  
		}
		

		return Double.parseDouble(termino); //para los casos donde solo hay un numero ej: formula = "7"
	}
	
	public String saltearParentesis(String subformula){
		//-- dado un string retorna la cadena hasta donde cierra ')'
		//-- Ej: (7*3-(11-1)/2)+44 --> (7*3-(11-1)/2)  
		int i =1;
		String termino = "(";
		String subtermino;
		for (i=1; i< subformula.length(); i++){
			char caracter = subformula.charAt(i);
			switch (caracter) {
			case '(':
				subtermino = saltearParentesis(subformula.substring(i));
				termino += subtermino;
				i+= subtermino.length()-1;
				break;
			case ')':
				termino += caracter;
				System.out.println(termino);
				return termino;
			default: 
				termino += caracter; 
				break;
			}
		}
		return "";
	}
	
	@Test
	public void sieteEsSiete() {
		String formula =  "7";
		Double resultado = evaluar(formula);
		assertEquals(7, resultado, 0.0);	
	}
	
	@Test
	public void menosSieteEsMenosSiete() {
		String formula =  "-7";
		Double resultado = evaluar(formula);
		assertEquals(-7, resultado, 0.0);	
	}
	
	@Test
	public void dosYDosSonCuatroTest() {
		String formula =  " 2 + 2 ";
		Double resultado = evaluar(formula);
		assertEquals(4, resultado, 0.0);
		
	}
	
	@Test
	public void ochoMenosUnoSiete() {
		String formula =  "8-1";
		Double resultado = evaluar(formula);
		assertEquals(7, resultado, 0.0);
	}
	
	@Test
	public void tresMasTresMasTres() {
		String formula =  "3+3+3";
		Double resultado = evaluar(formula);
		assertEquals(9, resultado, 0.0);
	}
	
	@Test
	public void cuatroMasDosMenosOnce() {
		String formula =  "4+2-11";
		Double resultado = evaluar(formula);
		assertEquals(-5, resultado, 0.0);
	}
	
	@Test
	public void tresDividoTresIgualUno() {
		String formula =  "3/3";
		Double resultado = evaluar(formula);
		assertEquals(1, resultado, 0.0);
	}
	
	@Test
	public void dosMasTresPorCuatroIgualCatorce() {
		String formula =  "2+3*4";
		Double resultado = evaluar(formula);
		assertEquals(14, resultado, 0.0);
	}
	
	@Test
	public void dosMaTresPorCuatroSobreDosMenosCincoIgualNueve() {
		String formula =  "8+3*4/2-5";
		Double resultado = evaluar(formula);
		assertEquals(9, resultado, 0.0);
	}
	
	@Test
	public void formulaConParentestisSimple() {
		String formula =  "(8+3*4)/2-5";
		Double resultado = evaluar(formula);
		assertEquals(5, resultado, 0.0);
	}
	
	/*-- Test que deberia pasar --*/
	@Test
	public void formulaConParentestisDoble() {
		String formula =  "((8+3)*4)/2-5";
		Double resultado = evaluar(formula);
		assertEquals(17, resultado, 0.0);
	}
	
	@Test
	public void formulaCompleja() {
		String formula =  "(((7*3)-(1+2))/(2 +11))*3 +7+4*1";
		Double resultado = evaluar(formula);
		assertEquals(15.154, resultado, 0.001);
	}

	@Test
	public void otraFormulaCompleja() {
		String formula =  "(((7*3)-(1+2))/(2 +11))*3 +7+4*1 + 0.07";
		Double resultado = evaluar(formula);
		assertEquals(15.224, resultado, 0.001);
	}
}

