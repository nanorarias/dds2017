package tp1.modelo.repositorios;

import java.util.List;
import java.util.stream.Collectors;

import tp1.modelo.Empresa;
import tp1.modelo.repositorios.fuentes.FuenteDeEmpresa;

public class RepositorioDeEmpresas {

	FuenteDeEmpresa source;
	
	List<Empresa> companies;
	
	public RepositorioDeEmpresas(FuenteDeEmpresa source){
		
		this.source = source;
		companies = this.source.cargar();
	}
	
	public List<Empresa> todos() {
		return companies;
	}
	
	public List<String> obtenerNombres(){
		return companies.stream().map(e -> e.getNombre()).collect(Collectors.toList());
	}
	
	public Empresa encontrar(String name) {
		return companies.stream().filter(c -> c.getNombre().equals(name)).findFirst().orElse(null);
	}
	
}
