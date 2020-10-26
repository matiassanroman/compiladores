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
	int minValorCte     = -32768;
	int maxValorCte     =  32767;
	
	// Constructor
	public AS9_Verificar_Rango_Constante(Hashtable<String,ArrayList<Simbolo>> tablaSimbolo, HashMap<String,Integer> tablaToken){
		this.tablaToken = tablaToken;
		this.tablaSimbolo = tablaSimbolo;			
	}
	
	
	public int execute(StringBuffer buffer, char c) {
		this.s = new Simbolo(buffer.toString());
		int cte = Integer.parseInt(buffer.toString());
		//System.out.println(buffer.toString());
		// Verifica si la constante esta dentro del rango
		if((cte>=minValorCte) && (cte<=maxValorCte)){ 	   
			s.setTipo("int");
			
			/*
			// Si la cte ya está en la TS, retornar referencia
			if(TablaSimbolo.contains(this.s) )
				return TablaToken.get("CTE");
			// Si la cte no está en la TS, agregarla y retornarla
			else{                                			
				s.setUso("CTE");
				//TablaSimbolo.put(s.getValor(),s);
				//Ambito Main
				String aux = s.getValor() + ":" + "Main";
				s.setAmbito(aux,true);
				return TablaToken.get("CTE");
			}
			*/
			
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
		// Si la cte está fuera de los rangos
		else                                    	
			if((cte<minValorCte) || (cte>maxValorCte)) return -1;	   		
			else return -1;        // Retornar -1, para ERROR de fuera de rango
	}

	// Al leer simbolo se puede leer uno que no corresponda a la 
	// constante, con lo cual hay que acomodar la lines, 
	// entonces retorna true
	public boolean acomodarLinea() {
		return true;
	}

}
