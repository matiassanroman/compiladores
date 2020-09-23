package accionesSemanticas;

import java.util.HashMap;
import java.util.Hashtable;
import compilador.Simbolo;


public class AS5_Fin_Cadena extends AccionSemantica{
	
	Hashtable<String,Simbolo> tablaSimbolo;
	HashMap<String,Integer> tablaToken;  
	Simbolo s;
	
	// Constructor
	public AS5_Fin_Cadena(Hashtable<String, Simbolo> tablaSimbolo, HashMap<String, Integer> tablaToken) {
		this.tablaSimbolo = tablaSimbolo;
		this.tablaToken = tablaToken;
	}

	@Override
	public int execute(StringBuffer buffer, char c) {
		this.s = new Simbolo(buffer.toString());
		s.setTipo("Cadena");
		if(tablaSimbolo.contains(this.s) ){  			//SI ESTA EN LA TABLA
			return tablaToken.get("CADENA");
		}
		else{                                			// SI NO ESTA EN LA TABLA
			s.setUso("CADENA");
			tablaSimbolo.put(s.getValor(),s);
			return tablaToken.get("CADENA");
		}
	}

	public boolean acomodarLinea() {
		return false;
	}

}
