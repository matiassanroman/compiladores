package accionesSemanticas;

import java.util.HashMap;
import java.util.Hashtable;
import compilador.Simbolo;
import compilador.Compilador;;

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
		// Bloques para controlar la longitud de la cadena
		if (buffer.length() <= 20) {
			String a1 = buffer.toString(); 
			a1 = a1.concat("hola");
			s = new Simbolo(a1);
		}
		else {
			s = new Simbolo(buffer.substring(0,20));
			buffer.setLength(20);
			tablaSimbolo.put(s.getValor(),s);
			System.out.println("Warning: Longitud de identificador excedido, truncado a 20");
		}
		
		String ambitoGeneral = "Main" + Compilador.ambito;
		/*
		String ambitoId = tablaSimbolo.get(s.getValor()).getAmbito();
		
		String [] arreglo = ambitoId.split("\\:"); 
		String aux = ""; 
		for(int i=1; i<arreglo.length; i++){
			aux = aux + arreglo[i]; 
		} 
		ambitoId = aux; */
		
		System.out.println("holaaaa: " + s.getValor());
		
		if(!tablaSimbolo.contains(s) ) {
			tablaSimbolo.put(s.getValor(),s);
		} /* else {			
			System.out.println("1: " + ambitoGeneral);
			System.out.println("2: " + ambitoId);
			if(!ambitoId.equals(ambitoGeneral)) {
				System.out.println("No se puede asignar, diferentes ambitos");
			}
			else {
				System.out.println("Se puede asignar");
			}
		} */
		
		s.setUso("ID");
		//Ambito Main
		String aux2 = s.getValor() + ":" + "Main";
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
