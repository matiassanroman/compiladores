package compilador;

public class Par {
	private Integer clave;
	private String valor;
	public static String valorIncompleto = "";
	
    public Par(String valor) {
    	this.valor = valor;
	}
    
//    public Par(Integer posicion, String valor) {
//    	this.clave = posicion;
//    	this.valor = valor;
//    }
    
    public Integer getClave() { return this.clave; }
    public String getValor() { return this.valor; }
    
    public void setClave(Integer clave) { this.clave = clave; }
    public void setValor(String valor) { this.valor = valor; }
    
    public boolean equals(Par otroPar) {
    	return this.getClave().equals(otroPar.getClave());
	}
    public boolean tieneMismoValor(String valor) {
    	return this.getValor().equals(valor);
    }
    public String toString() {
    	return this.clave + " " + this.valor;
    }
} 

