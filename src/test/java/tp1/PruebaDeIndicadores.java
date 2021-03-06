package tp1;

import static org.junit.Assert.*;

import org.junit.Test;

import tp1.modelo.Empresa;
import tp1.modelo.indicador.Indicador;
import tp1.modelo.indicador.ConstructorDeIndicador;
import tp1.modelo.indicador.ConstructorDeIndicador.ExcepciónDeFórmulaInválida;

public class PruebaDeIndicadores {

	@Test
	public void probarIndicadorVálido() throws Exception {
		String nombre = "IV";
		String descripción = "El número áureo";
		String fórmula = "1,61803398875";

		ConstructorDeIndicador constructor = new ConstructorDeIndicador();
		constructor.establecerNombre(nombre);
		constructor.establecerDescripción(descripción);
		constructor.establecerFórmula(fórmula);

		Indicador indicador = constructor.construir();

		assertEquals(nombre, indicador.obtenerNombre());
		assertEquals(descripción, indicador.obtenerDescripción());
		assertEquals(fórmula, indicador.obtenerFórmula().comoCadenaDeCaracteres());
	}
	
	@Test(expected = ExcepciónDeFórmulaInválida.class)
	public void probarIndicadorInválido() throws Exception {
		String nombre = "II";
		String fórmula = "1:61803398875";

		ConstructorDeIndicador constructor = new ConstructorDeIndicador();
		constructor.establecerNombre(nombre);
		constructor.establecerFórmula(fórmula);
		constructor.construir();
	}
	
	@Test
	public void probarObtenerElValorDeUnIndicador() throws Exception {
		String nombre = "nombre";
		String descripción = "";
		String fórmula = "1+2";
		
		Empresa empresa = new Empresa("CompanyName");

		ConstructorDeIndicador constructor = new ConstructorDeIndicador();
		constructor.establecerNombre(nombre);
		constructor.establecerDescripción(descripción);
		constructor.establecerFórmula(fórmula);

		Indicador indicador = constructor.construir();
		
		double resultado = indicador.obtenerValor(empresa, (short) 1929);

		assertEquals(3.0, resultado, 0.0);
	}
	
	
}