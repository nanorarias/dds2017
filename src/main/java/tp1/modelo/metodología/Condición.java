package tp1.modelo.metodología;

import java.util.List;
import java.util.stream.Collectors;

import org.uqbar.commons.utils.Observable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import tp1.modelo.Empresa;
import tp1.modelo.indicador.Indicador;

//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
//@JsonSubTypes({
//    @JsonSubTypes.Type(value = CondiciónTaxativa.class, name = "tax"),
//
//    @JsonSubTypes.Type(value = CondiciónComparativa.class, name = "comp") }
//)
@Observable
public abstract class Condición {
	
	@JsonProperty
	protected String nombre;
	
	@JsonProperty
	protected Indicador indicador;
	
	@JsonProperty
	protected int númeroDePeríodos;
	
	@JsonProperty
	protected Evaluación evaluación;
	
	@JsonProperty
	protected Orden orden;
	
	protected Condición() {}
	
	protected Condición(String nombre, Indicador indicador, int númeroDePeríodos, Evaluación evaluación, Orden orden) {
		this.nombre = nombre;
		this.indicador = indicador;
		this.númeroDePeríodos = númeroDePeríodos;
		this.evaluación = evaluación;
		this.orden = orden;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Indicador obtenerIndicador() {
		return indicador;
	}
	
	public int obtenerNúmeroDePeríodos() {
		return númeroDePeríodos;
	}
	
	public Evaluación obtenerEvaluación() {
		return evaluación;
	}
	
	public Orden obtenerOrden() {
		return orden;
	}

	public abstract List<Empresa> aplicar(List<Empresa> empresas);
	
	public boolean esAplicable(List<Empresa> empresas) {
		return empresas.stream().allMatch(empresa -> períodosAEvaluar(empresa).stream()
				.allMatch(período -> indicador.esVálidoParaContexto(empresa, período)));
	}
	
	protected List<Short> períodosAEvaluar(Empresa empresa) {
		List<Short> períodos = empresa.obtenerPeríodos();
		if(períodos.size() == 0) return períodos;
		short períodoFinal = períodos.get(períodos.size()-1);
		short períodoInicial = (short)(períodoFinal - númeroDePeríodos + 1);
		int índiceInicial = períodos.indexOf(períodoInicial);
		if(índiceInicial == -1) índiceInicial++;
		return períodos.subList(índiceInicial, períodos.size());
	}
	
	protected List<Double> valoresAEvaluar(Empresa empresa) {
		return períodosAEvaluar(empresa).stream().map(período -> indicador.obtenerValor(empresa, período))
				.collect(Collectors.toList());
	}
	
	public abstract String getTipo();
}