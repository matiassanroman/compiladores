package accionesSemanticas;

import java.util.HashMap;

public class AS8_Fin_Simbolo_Complejo extends AccionSemantica{

	HashMap<String,Integer> tablaToken; 
	
	/*
	Identifica que el token es un simbolo compuesto devolviendolo. 
	Estos son los simbolos que involucran mas de un caracter como : ==, >=, !=, <=.
	Entonces retornamos el ascii de la tabla token. 
	*/
	
	public AS8_Fin_Simbolo_Complejo(HashMap<String,Integer> tablaToken){
		this.tablaToken = tablaToken;			
	}
	
	@Override
	public int execute(StringBuffer buffer, char c) {
		buffer.append(c);
		return tablaToken.get(buffer.toString());
	}

	@Override
	public boolean acomodarLinea() {
		return false;
	}

}
