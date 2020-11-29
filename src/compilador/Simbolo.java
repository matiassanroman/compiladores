package compilador;

public class Simbolo {

	private String valor;       //lexema
	private String tipo = "";   // VAR (int - float - string) - PROC - PARAM_PROC (int - float) - NA_PROC - NS_PROC
	private String uso = "";    //CTE - CADENA - ID
	private boolean declarada;
	//private int contador;
	private String ambito;
	private int cantParametros;
	private int na;
	private int ns;
	private String tipoParametro;
	private String pasajeParametro;

	public Simbolo(String valor) {
	this.valor = valor;
	}
	
	public String getAmbito() {
		return ambito;
	}
	
	public void setAmbito(String ambito, boolean primeroMain) {
		//Entro por primera vez con Main
		if(primeroMain) {
			this.ambito = ambito;
		//Agrega ambito de la variable ambito global
		}else {
			this.ambito = this.ambito + compilador.Compilador.ambito;
		}
		
		
	}
	
	public void setAmbito() {
			this.ambito = getValor() + "@Main" + compilador.Compilador.ambito;
	}
	
	public String ambitoSinNombre() {
		String [] arreglo = getAmbito().split("\\@");
		String auxSinNombre = "";
		boolean primero = true;
		for(int z=1; z<arreglo.length; z++) {
			if(primero) {
				primero = false;
				auxSinNombre = arreglo[z];
			}
			else
				auxSinNombre = auxSinNombre + "@" + arreglo[z];
		}
		return auxSinNombre;
	}
	
	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}

	public boolean isDeclarada() {
		return declarada;
	}

	public void setDeclarada(boolean declarada) {
		this.declarada = declarada;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public int getCantParametros() {
		return cantParametros;
	}

	public void setCantParametros(int cantParametros) {
		this.cantParametros = cantParametros;
	}

	public int getNa() {
		return na;
	}

	public void setNa(int na) {
		this.na = na;
	}

	public int getNs() {
		return ns;
	}

	public void setNs(int ns) {
		this.ns = ns;
	}
	
	public String getTipoParametro() {
		return tipoParametro;
	}

	public void setTipoParametro(String tipoParametro) {
		this.tipoParametro = tipoParametro;
	}

	public String getPasajeParametro() {
		return pasajeParametro;
	}

	public void setPasajeParametro(String pasajeParametro) {
		this.pasajeParametro = pasajeParametro;
	}

	@Override
	public boolean equals(Object o) {
	Simbolo s = (Simbolo) o;
	if (this.valor.equals(s.getValor())){  
	return true;
	}
	return false;
	}
	
} 