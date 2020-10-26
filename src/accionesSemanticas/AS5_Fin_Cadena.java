package accionesSemanticas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import compilador.Simbolo;


public class AS5_Fin_Cadena extends AccionSemantica{
	
	Hashtable<String,ArrayList<Simbolo>> tablaSimbolo;
	HashMap<String,Integer> tablaToken;  
	Simbolo s;
	
	// Constructor
	public AS5_Fin_Cadena(Hashtable<String, ArrayList<Simbolo>> tablaSimbolo, HashMap<String, Integer> tablaToken) {
		this.tablaSimbolo = tablaSimbolo;
		this.tablaToken = tablaToken;
	}

	// Al finalizar de reconocer una cadena, si esta el la tabla
	// se retorna sino se la agrega y luego se retorna
	public int execute(StringBuffer buffer, char c) {
		this.s = new Simbolo(buffer.toString());
		s.setTipo("Cadena");
		
		/*
		// Si est� en la tabla
		if(tablaSimbolo.contains(this.s) ){ 
			return tablaToken.get("CADENA"); 
		}
		// Si no est� en la tabla
		else{                                			
			s.setUso("CADENA");
			//tablaSimbolo.put(s.getValor(),s);
			return tablaToken.get("CADENA");
		}
		*/
		
		if(!tablaSimbolo.containsKey(s.getValor()) ) {
			ArrayList<Simbolo> list =new ArrayList<Simbolo>();
			list.add(s);
			tablaSimbolo.put(s.getValor(),list);
		}else {
			tablaSimbolo.get(s.getValor()).add(s);	
		}
		
		s.setUso("CADENA");
		//Ambito Main
		String aux2 = s.getValor() + ":" + "Main";
		s.setAmbito(aux2,true);
		return tablaToken.get("CADENA");
		
	}

	// Al ser una cadena de texto no se necesitar�
	// acomodar la linea con lo cual retorna false
	public boolean acomodarLinea() {
		return false;
	}

}
