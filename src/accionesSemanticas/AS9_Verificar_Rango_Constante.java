package accionesSemanticas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import compilador.Simbolo;

public class AS9_Verificar_Rango_Constante extends AccionSemantica{
	
	Hashtable<String,ArrayList<Simbolo>> tablaSimbolo;
	HashMap<String,Integer> tablaToken;  
	Simbolo s;

	// se definen rango mminimo y maximo conrrespondientes
	// al valor de las constantes
	static int minValorCte     = -32768;
	static int maxValorCte     =  32767;
	
	// Constructor
	public AS9_Verificar_Rango_Constante(Hashtable<String,ArrayList<Simbolo>> tablaSimbolo, HashMap<String,Integer> tablaToken){
		this.tablaToken = tablaToken;
		this.tablaSimbolo = tablaSimbolo;			
	}
	
	
	public int execute(StringBuffer buffer, char c) {
		if(buffer.toString().contains("_") && buffer.toString().contains("i")) {
			this.s = new Simbolo(buffer.toString().substring(0, buffer.toString().length()-2)); 
			s.setTipo("int");
			
			// Si la cte no esta la agrego y sino retorno  referencia
			if(!tablaSimbolo.containsKey(s.getValor()) ) {
				ArrayList<Simbolo> list =new ArrayList<Simbolo>();
				list.add(s);
				tablaSimbolo.put(s.getValor(),list);
			}else {
				tablaSimbolo.get(s.getValor()).add(s);
			}
			
			s.setUso("CTE");
			//Ambito Main
			String aux = s.getValor() + ":" + "Main";
			s.setAmbito(aux,true);
			return tablaToken.get("CTE");
		}
		else {
			System.out.println("Error en la linea "+compilador.Compilador.nroLinea+": CTE ENTERA mal escrita");
			buffer.delete(0, buffer.length());
			return 0;
		}
	}

	public static boolean estaEnRango(String cte) {
		int aux = Integer.parseInt(cte.toString().substring(0, cte.toString().length()-2));
		if((aux>=minValorCte) && (aux<=maxValorCte)) 	 
			return true;
		else
			return false;
	}
	
	// Al leer simbolo se puede leer uno que no corresponda a la 
	// constante, con lo cual hay que acomodar la lines, 
	// entonces retorna true
	public boolean acomodarLinea() {
		return true;
	}

}
