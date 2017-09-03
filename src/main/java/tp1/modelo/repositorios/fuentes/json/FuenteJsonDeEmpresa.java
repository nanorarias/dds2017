package tp1.modelo.repositorios.fuentes.json;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tp1.modelo.CuentaDeEmpresa;
import tp1.modelo.Empresa;
import tp1.modelo.repositorios.fuentes.FuenteDeEmpresa;

public class FuenteJsonDeEmpresa implements FuenteDeEmpresa {
	
	private static final String ARCHIVO_DE_CUENTAS = "cuentas.json";
	
	public CodificadorJson coder;
	
	public FuenteJsonDeEmpresa(String nombreDeArchivo) {
		coder = new CodificadorJson(nombreDeArchivo, CuentaDeEmpresa.class);
	}
	
	public FuenteJsonDeEmpresa() {
		this(ARCHIVO_DE_CUENTAS);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Empresa> cargar() {
		List<CuentaDeEmpresa> metrics = (List<CuentaDeEmpresa>) coder.read();
		
		List<String> companyNames = metrics.stream().map(m -> m.getCompanyName()).distinct().collect(Collectors.toList());
		List<Empresa> companies = new ArrayList<>();
		
		companyNames.forEach(companyName -> {
			List<CuentaDeEmpresa> companyMetrics = metrics.stream().filter(m -> m.getCompanyName().equals(companyName))
					.collect(Collectors.toList());
			companies.add(new Empresa(companyName, companyMetrics));
		});
		return companies;
	}

}
