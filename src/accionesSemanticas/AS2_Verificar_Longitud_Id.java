package accionesSemanticas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import compilador.Simbolo;
import compilador.Compilador;;

public class AS2_Verificar_Longitud_Id extends AccionSemantica{

	Hashtable<String,ArrayList<Simbolo>> tablaSimbolo;
	HashMap<String,Integer> tablaToken; 
	Simbolo s;		
	
	public AS2_Verificar_Longitud_Id(Hashtable<String,ArrayList<Simbolo>> tablaSimbolo, HashMap<String,Integer> tablaToken){
		this.tablaToken = tablaToken;
		this.tablaSimbolo = tablaSimbolo;			
	}	
	
	//Controla que que la longitud de los identificadores(ID) sea menor o 
	// igual a 20 caracteres
	public int execute(StringBuffer buffer, char c) {
		// Bloques para controlar la longitud de la cadena
		if (buffer.length() <= 20) {
			s = new Simbolo(buffer.toString());
		}
		else {
			s = new Simbolo(buffer.substring(0,20));
			buffer.setLength(20);
			//tablaSimbolo.put(s.getValor(),s);
			System.out.println("Warning: Longitud de identificador excedido, truncado a 20");
		}
		
		if(!tablaSimbolo.containsKey(s.getValor()) ) {
			ArrayList<Simbolo> list =new ArrayList<Simbolo>();
			list.add(s);
			tablaSimbolo.put(s.getValor(),list);
		}else {
			tablaSimbolo.get(s.getValor()).add(s);	
		}
		
		s.setUso("ID");
		s.setTipo("Var");
		//Ambito Main
		String aux2 = s.getValor() + "@" + "Main";
		s.setAmbito(aux2,true);
		return tablaToken.get("ID");
		
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
