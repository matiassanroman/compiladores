package compilador;

public class Simbolo {

	public String valor;       //lexema
	public String tipo = "";   // int - float - string
	public String uso = "";    //CTE - CADENA - ID	
	private boolean declarado = false;
	private String estaDeclarado = "NO";

	public Simbolo(String valor) {
	this.valor = valor;
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
	
	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}
	
	@Override
	public boolean equals(Object o) {
	Simbolo s = (Simbolo) o;
	if (this.valor.equals(s.getValor())  /*&& this.ambiente.equals(s.getAmbiente()) && this.tipo.equals(s.getTipo()) && this.uso.equals(s.getUso())*/) {  
	return true;
	}
	return false;
	}
	
	public String imprimir() {
		String format = "|%1$-15s|%2$-15s|%3$-25s|%4$-35s|%5$-15s|\n";
		   String output = String.format(format, "Valor: "+ this.valor, "Tipo: "+ this.tipo, "Declarado: " + this.estaDeclarado);
		  return output; //" Valor: "+ this.valor + " Tipo: "+ this.tipo;
	}
	
} 