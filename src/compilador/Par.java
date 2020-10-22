package compilador;

public class Par {
	private Integer clave;
	private String valor;
	public static String valorIncompleto = "chagar";
	
    public Par(String valor) {
    	this.valor = valor;
	}
    
    public Integer getClave() { return this.clave; }
    public String getValor() { return this.valor; }
    
    public void setClave(Integer clave) { this.clave = clave; }
    public void setValor(String valor) { this.valor = valor; }
    
    public boolean equals(Par otroPar) {
    	return this.getClave().equals(otroPar.getClave());
	}
    
    public String toString() {
    	return this.clave + " " + this.valor;
    }
} 

