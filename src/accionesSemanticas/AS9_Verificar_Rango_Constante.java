package accionesSemanticas;

import java.util.HashMap;
import java.util.Hashtable;

import compilador.Simbolo;

public class AS9_Verificar_Rango_Constante extends AccionSemantica{
	
	Hashtable<String,Simbolo> TablaSimbolo;
	HashMap<String,Integer> TablaToken;  
	Simbolo s;

	short minValorCte     = -32768;
	short maxValorCte     =  32767;
	
	public AS9_Verificar_Rango_Constante(Hashtable<String,Simbolo> TablaSimbolo, HashMap<String,Integer> TablaToken){
		this.TablaToken = TablaToken;
		this.TablaSimbolo = TablaSimbolo;			
	}
	
	@Override
	public int execute(StringBuffer buffer, char c) {
		this.s = new Simbolo(buffer.toString());
		//this.s.setUso("CTE");
		int cte = Integer. parseInt(buffer.toString());
		if((cte>=minValorCte) && (cte<=maxValorCte)){ 	    // Verifica si la constante esta dentro del rango
			s.setTipo("int");
			if(TablaSimbolo.contains(this.s) ){  			// Si la cte ya está en la TS, aumenta la cantidad de referencias, preguntar
				//TablaSimbolo.get(s.getValor()).increase_Ref();
				return TablaToken.get("CTE");
			}
			else{                                			// Si la cte no está en la TS
				s.setUso("CTE");
				TablaSimbolo.put(s.getValor(),s);
				return TablaToken.get("CTE");
			}
		}
		else                                    		 // Si la cte está fuera de los rangos
			if((cte<minValorCte) || (cte>maxValorCte))   // Si la cte es menor al rango minimo 
					return -4;					 		 // Restornar -4, para Warning de fuera de rango
			else 
				return 0;
	}

	@Override
	public boolean acomodarLinea() {
		return true;
	}

}
