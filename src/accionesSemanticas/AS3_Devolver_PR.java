package accionesSemanticas;

import java.util.HashMap;
import java.util.Hashtable;

import compilador.Simbolo;

public class AS3_Devolver_PR extends AccionSemantica{

	/*
	Retornar último carácter a la entrada
	Si el String está en la TPR
	Retornar identificador + referencia a la TPR
	Si el String no está en la TPR
	Se la dá de alta en la TPR
	Retornar identificador + referencia a la TPR*/

	Hashtable<String,Simbolo> tablaSimbolo;
	HashMap<String,Integer> tablaToken; 
	Simbolo s;		
	
	public AS3_Devolver_PR(Hashtable<String,Simbolo> tablaSimbolo, HashMap<String,Integer> tablaToken){
		this.tablaToken = tablaToken;
		this.tablaSimbolo = tablaSimbolo;			
	}	
	
	
	@Override
	public int execute(StringBuffer buffer, char c) {
		
		//Retorna Palabra Reservada
		return tablaToken.get(buffer.toString());
	}

	public Simbolo getSimbolo() {
		return this.s;
	}

	@Override
	public boolean acomodarLinea() {
		return true;
	}
}
