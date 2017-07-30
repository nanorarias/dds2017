package tp1.modelo.metodología;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import tp1.modelo.Empresa;
import tp1.modelo.indicador.Indicador;

public final class CondiciónComparativa extends Condición {
	
	@JsonProperty
	private Prioridad prioridad;

	@JsonCreator
	CondiciónComparativa(
			@JsonProperty("nombre") String nombre,
			@JsonProperty("indicador") Indicador indicador,
			@JsonProperty("númeroDePeríodos") int númeroDePeríodos,
			@JsonProperty("evaluacion") Evaluación evaluación,
			@JsonProperty("orden") Orden orden,
			@JsonProperty("valorDeReferencia") Prioridad prioridad) {
		super(nombre, indicador, númeroDePeríodos, evaluación, orden);
		this.prioridad = prioridad;
	}
	
	public Prioridad obtenerPrioridad() {
		return prioridad;
	}

	@Override
	public List<Empresa> aplicar(List<Empresa> empresas) {
		return empresas.stream().sorted(this::compararEmpresas).collect(Collectors.toList());
	}
	
	private int compararEmpresas(Empresa empresa1, Empresa empresa2) {
		double valor1 = evaluación.evaluar(valoresAEvaluar(empresa1));
		double valor2 = evaluación.evaluar(valoresAEvaluar(empresa2));
		return orden.comparar(valor1, valor2) ? 1 : -1;
	}
	
	public String getTipo()	{
		return "Comparativa";
	}

}