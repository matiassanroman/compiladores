package compilador;

public class Simbolo {

	public String valor;       //lexema
	public String tipo = "";   // int - float 
	//public int cant_ref; 
	private boolean declarado = false;
	private String estaDeclarado = "NO";

	public Simbolo(String valor) {
	this.valor = valor;
	//this.cant_ref = 1;
	}

	public Simbolo(String valor, String tipo) {
	this.valor = valor;
	this.tipo = tipo;
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

	public boolean isDeclarado() {
		return declarado;
	}

	public void setDeclarado(boolean declarado) {
		this.declarado = declarado;
	}

	public String getEstaDeclarado() {
		return estaDeclarado;
	}

	public void setEstaDeclarado(String estaDeclarado) {
		this.estaDeclarado = estaDeclarado;
	}
	
} 