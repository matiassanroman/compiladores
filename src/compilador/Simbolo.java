package compilador;

import java.util.ArrayList;

public class Simbolo {

	public String valor;       //lexema
	public String tipo = "";   // int - float 
	//public int cant_ref; 
	private boolean declarado = false;
	private String estaDeclarado = "NO";

	public Simbolo(String valor) {
	this.valor = valor;
	this.cant_ref = 1;
	}

	public Simbolo(String valor, String tipo) {
	this.valor = valor;
	this.tipo = tipo;
	}
	
}
