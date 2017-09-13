package tp1.modelo.metodología;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.sym.Name;

import tp1.modelo.Empresa;
import tp1.modelo.indicador.Indicador;

@Entity
@DiscriminatorValue("CondiciónTaxativa")
public final class CondiciónTaxativa extends Condición {
	
	@JsonProperty
	private Double valorDeReferencia;

	public CondiciónTaxativa() {}

	@JsonCreator
	CondiciónTaxativa(
			@JsonProperty("nombre") String nombre,
			@JsonProperty("indicador") Indicador indicador, 
			@JsonProperty("númeroDePeríodos") int númeroDePeríodos,
			@JsonProperty("evaluacion") Evaluación evaluación, 
			@JsonProperty("orden") Orden orden,
			@JsonProperty("valorDeReferencia") Double valorDeReferencia) {
		super(nombre, indicador, númeroDePeríodos, evaluación, orden);
		this.valorDeReferencia = valorDeReferencia;
	}
	
	public Double obtenerValorDeReferencia() {
		return valorDeReferencia;
	}

	@Override
	public List<Empresa> aplicar(List<Empresa> empresas) {
		return empresas.stream().filter(this::esConveniente).collect(Collectors.toList());
	}
	
	private boolean esConveniente(Empresa empresa) {
		List<Double> valores = valoresAEvaluar(empresa);
		
		if(this.valorDeReferencia != null) {
			return orden.comparar(evaluación.evaluar(valores), valorDeReferencia);
		}
		
		// Si no hay valor de referencia, se evalúa la tendencia
		for(int i = 1, cantidad = valores.size(); i < cantidad; i++) {
			if(!orden.comparar(valores.get(i), valores.get(i-1))) {
				return false;
			}
		}
		return true;
	}
	
	public String getTipo()	{
		return "Taxativa";
	}
	
	public Condición getInstance()	{
		return this;
	}
}