package accionesSemanticas;

import java.util.HashMap;
import java.util.Hashtable;
import compilador.Simbolo;

public class AS2_Verificar_Longitud_Id extends AccionSemantica{

	Hashtable<String,Simbolo> tablaSimbolo;
	HashMap<String,Integer> tablaToken; 
	Simbolo s;		
	
	public AS2_Verificar_Longitud_Id(Hashtable<String,Simbolo> tablaSimbolo, HashMap<String,Integer> tablaToken){
		this.tablaToken = tablaToken;
		this.tablaSimbolo = tablaSimbolo;			
	}	
	
	//Controla que que la longitud de los identificadores(ID) sea menor o 
	// igual a 20 caracteres
	public int execute(StringBuffer buffer, char c) {
		
		
		//+ referencia a la TS.
		if(buffer.length() < 20){
			s = new Simbolo(buffer.toString());
			//Si el String está en la TS, retornar identificador + referencia a la TS.
			if(tablaSimbolo.contains(s)) { return tablaToken.get("ID"); }
			//Si el String no está en la TS, se la dá de alta en la TS, retornar identificador
			else {
				s.setUso("ID");
				tablaSimbolo.put(s.getValor(),s);
				return tablaToken.get("ID");
			}
		}
		//Si longitud > 20, truncar(String), informar "String truncado a longitud 20"
		else {
			s = new Simbolo(buffer.substring(0,19));
			buffer.setLength(20);
			tablaSimbolo.put(s.getValor(),s);
			// ID fuera de rango, (codigo de error -3), para informar luego
			return 0; 														
		}
	}

	public Simbolo getSimbolo() {
		return this.s;
	}

	// Al tener que leer un caracter mas de la entrada, se necesita
	// acomodar la linea, entonces retorna true
	public boolean acomodarLinea() {
		return true;
	}

}
