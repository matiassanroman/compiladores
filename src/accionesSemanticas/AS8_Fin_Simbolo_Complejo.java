package accionesSemanticas;

import java.util.HashMap;

public class AS8_Fin_Simbolo_Complejo extends AccionSemantica{

	HashMap<String,Integer> tablaToken; 
	
	/*
	Identifica que el token es un simbolo compuesto devolviendolo. 
	Estos son los simbolos que involucran mas de un caracter como  ==, >=, !=, <=
	Entonces retornamos el ascii de la tabla token. 
	*/
	
	// Contrustor
	public AS8_Fin_Simbolo_Complejo(HashMap<String,Integer> tablaToken){
		this.tablaToken = tablaToken;			
	}
	
	// Agrega al buffer de lectura el caracte 'c', que es parte del 
	// simbolo compuesto que se quiere recononcer, luego retorna
	// el codigo asignado a ese simbolo compuestp
	public int execute(StringBuffer buffer, char c) {
		buffer.append(c);
		return tablaToken.get(buffer.toString());
	}

	// Como ya se leyó el siguiente simbolo que podria
	// pertencer al simbolo complejo, no es necesario
	// acomodar la linea, entonces retorne false
	public boolean acomodarLinea() {
		return false;
	}

}
