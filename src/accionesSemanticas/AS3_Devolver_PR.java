package accionesSemanticas;

import java.util.HashMap;
import java.util.Hashtable;

import compilador.Simbolo;

public class AS3_Devolver_PR extends AccionSemantica{

	/* Retornar último carácter a la entrada 
	Si el String está en la TPR
	Retornar identificador + referencia a la TPR
	Si el String no está en la TPR
	Se la dá de alta en la TPR
	Retornar identificador + referencia a la TPR */

	Hashtable<String,Simbolo> tablaSimbolo;
	HashMap<String,Integer> tablaToken; 
	Simbolo s;		
	
	// Constructor
	public AS3_Devolver_PR(Hashtable<String,Simbolo> tablaSimbolo, HashMap<String,Integer> tablaToken){
		this.tablaToken = tablaToken;
		this.tablaSimbolo = tablaSimbolo;			
	}	
	
	public Simbolo getSimbolo() { return this.s; }
	
	public int execute(StringBuffer buffer, char c) {
		//Retorna Palabra Reservada
		return tablaToken.get(buffer.toString());
	}

	// Como al querer reconocer palabras reservadas, se lee un caracter de más
	// con lo cual se necesita acomodar la linea, entonces retorna true
	public boolean acomodarLinea() {
		return true;
	}
}
