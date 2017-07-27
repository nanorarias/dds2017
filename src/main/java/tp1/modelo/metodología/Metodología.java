package tp1.modelo.metodología;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.uqbar.commons.utils.Observable;

import tp1.modelo.Empresa;

public class Metodología {
	
	private String nombre;
	private List<CondiciónTaxativa> condicionesTaxativas;
	private List<CondiciónComparativa> condicionesComparativas;
	
	Metodología(String nombre, List<CondiciónTaxativa> condicionesTaxativas,
			List<CondiciónComparativa> condicionesComparativas) {
		this.nombre = nombre;
		this.condicionesTaxativas = condicionesTaxativas;
		this.condicionesComparativas = condicionesComparativas;
	}

	public String obtenerNombre() {
		return nombre;
	}
	
	public List<String> getNombreCondiciones(){
		List<String> listaNombres = new ArrayList<>();
		for(Condición condicion: condicionesTaxativas){
			listaNombres.add(condicion.getNombre());
		}
		for(Condición condicion: condicionesComparativas){
			listaNombres.add(condicion.getNombre());
		}
			
		return listaNombres;
	}
	
	public boolean esAplicable(List<Empresa> empresas) {
		return Stream.concat(condicionesTaxativas.stream(), condicionesComparativas.stream())
				.allMatch(condición -> condición.esAplicable(empresas));
	}

	public List<Empresa> aplicar(List<Empresa> empresas) {
		return aplicarCondicionesComparativas(aplicarCondicionesTaxativas(empresas));
	}
	
	private List<Empresa> aplicarCondicionesTaxativas(List<Empresa> empresas) {
		for(Condición condiciónTaxativa : condicionesTaxativas) {
			empresas = condiciónTaxativa.aplicar(empresas);
		}
		return empresas;
	}
	
	private List<Empresa> aplicarCondicionesComparativas(List<Empresa> empresas) {
		Map<String, Integer> pesos = new HashMap<>();
		
		for(CondiciónComparativa condiciónComparativa : condicionesComparativas) {
			actualizarPesos(pesos, condiciónComparativa.obtenerPrioridad(), condiciónComparativa.aplicar(empresas));
		}
		
		return obtenerEmpresasOrdenadasPorPeso(pesos, empresas);
	}

	private void actualizarPesos(Map<String, Integer> pesos, Prioridad prioridad, List<Empresa> empresas) {
		// Agrego puntos segun posición
		int i = empresas.size();
		for(String empresa : empresas.stream().map(e -> e.getNombre()).collect(Collectors.toList())) {
			Integer peso = pesos.get(empresa);
			if(peso == null) peso = 0;
			
			peso += prioridad.obtenerValor() * i--;
			pesos.put(empresa, peso); // no reemplaza; si existe lo actualiza			
		}		
	}
	
	public void eliminarCondicion(Condición condicion) {
		this.condicionesComparativas.remove(condicion);
		this.condicionesTaxativas.remove(condicion);
	}
	
	private List<Empresa> obtenerEmpresasOrdenadasPorPeso(Map<String, Integer> pesos, List<Empresa> empresas) {
		return pesos.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.map(e -> obtenerEmpresaDeNombre(e.getKey(), empresas)).collect(Collectors.toList());
	}
	
	private Empresa obtenerEmpresaDeNombre(String nombre, List<Empresa> empresas) {
		return empresas.stream().filter(empresa -> empresa.getNombre().equals(nombre)).findFirst().get();
	}
	
}
