package accionesSemanticas;

import java.util.HashMap;
import java.util.Hashtable;

import compilador.*;

public class AS2_Verificar_Longitud_Id extends AccionSemantica{

	Hashtable<String,Simbolo> tablaSimbolo;
	HashMap<String,Integer> tablaToken; 
	Simbolo s;		
	
	public AS2_Verificar_Longitud_Id(Hashtable<String,Simbolo> tablaSimbolo, HashMap<String,Integer> tablaToken){
		this.tablaToken = tablaToken;
		this.tablaSimbolo = tablaSimbolo;			
	}	
	
	@Override
	public int execute(StringBuffer buffer, char c) {
		
		//Chequeo que sea menor a 20.
		//Si el String está en la TS, retornar identificador + referencia a la TS.
		//Si el String no está en la TS, se la dá de alta en la TS, retornar identificador 
		//+ referencia a la TS.
		if(buffer.length() < 20){
			s = new Simbolo(buffer.toString());			
			if(tablaSimbolo.contains(s))
			{
				return tablaToken.get("ID");
			}
			else
			{
				tablaSimbolo.put(s.getValor(),s);
				return tablaToken.get("ID");
			}
		}
		//Si longitud > 20, truncar(String), informar "String truncado a longitud 20"
		else {
			s = new Simbolo(buffer.substring(0,19));
			buffer.setLength(20);
			tablaSimbolo.put(s.getValor(),s);
			return -3; 														// ID FUERA DE RANGO
		}
	}

	public Simbolo getSimbolo() {
		return this.s;
	}

	@Override
	public boolean acomodarLinea() {
		return true;
	}

}
