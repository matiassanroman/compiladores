package accionesSemanticas;

import java.util.HashMap;
import java.util.Hashtable;

import compilador.*;

public class AS3_check_Longitud extends AccionSemantica{

	Hashtable<String,Simbolo> TablaSimbolo;
	HashMap<String,Integer> TablaToken; 
	Simbolo s;		
	
	public AS3_check_Longitud(Hashtable<String,Simbolo> TablaSimbolo, HashMap<String,Integer> TablaToken){
		this.TablaToken = TablaToken;
		this.TablaSimbolo = TablaSimbolo;			
	}	
	
	@Override
	public int execute(StringBuffer buffer, char c) {
		
		//Chequeo que sea menor a 20
		//Si el String está en la TS, retornar identificador + referencia a la TS
		//Si el String no está en la TS, se la dá de alta en la TS, retornar identificador + referencia a la TS
		if(buffer.length() < 20){
			 s = new Simbolo(buffer.toString());			
			if(TablaSimbolo.contains(s))
			{
				return TablaToken.get("ID");
			}
			else
			{
				TablaSimbolo.put(s.getValor(),s);
				return TablaToken.get("ID");
			}
		}
		//Si longitud > 20, truncar(String), informar "String truncado a longitud 20"
		else {
			s = new Simbolo(buffer.substring(0,24));
			buffer.setLength(25);
			TablaSimbolo.put(s.getValor(),s);
			return -3; 														// ID FUERA DE RANGO
		}
	}

	@Override
	public boolean acomodarLinea() {
		// TODO Auto-generated method stub
		return false;
	}

}
