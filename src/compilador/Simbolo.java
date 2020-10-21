package compilador;

public class Simbolo {

	private String valor;       //lexema
	private String tipo = "";   // int - float - string - proc
	private String uso = "";    //CTE - CADENA - ID	
	private boolean declarada;
	private int contador;
	private String ambito;

	public Simbolo(String valor) {
	this.valor = valor;
	this.contador = 1;
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

	public boolean isDeclarada() {
		return declarada;
	}

	public void setDeclarada(boolean declarada) {
		this.declarada = declarada;
	}

	public void aumentarContador() {
		contador = contador + 1;
	}
	
	public void disminuirrContador() {
		contador = contador -1;
	}
	
	public int getContador() {
		return contador;
	}

	public void setContador(int contador) {
		this.contador = contador;
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
	
	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
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