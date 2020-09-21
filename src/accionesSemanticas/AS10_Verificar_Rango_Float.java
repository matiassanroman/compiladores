package accionesSemanticas;

import java.util.HashMap;
import java.util.Hashtable;

import compilador.Simbolo;

public class AS10_Verificar_Rango_Float extends AccionSemantica{

	Hashtable<String,Simbolo> TablaSimbolo;
	HashMap<String,Integer> TablaToken;  
	Simbolo s;

	double minimalValorFloat = -3.40282347E38;
	double minValorFloat     = -1.17549435E-38;
	double maxValorFloat     =  1.17549435E-38;
	double maximalValorFloat =  3.40282347E38;
	double cero = 0.0;
	
	public AS10_Verificar_Rango_Float(Hashtable<String, Simbolo> tablaSimbolo, HashMap<String, Integer> tablaToken) {
		TablaSimbolo = tablaSimbolo;
		TablaToken = tablaToken;
	}

	@Override
	public int execute(StringBuffer buffer, char c) {
		this.s = new Simbolo(buffer.toString());
		//this.s.setUso("CTE");
		double flotante = Double.parseDouble(buffer.toString());		
		
		if ( ((flotante>=minimalValorFloat) && (flotante <= minValorFloat)) || (flotante==cero)  || ((flotante>=maxValorFloat) && (flotante <= maximalValorFloat)) ) {
			s.setTipo("float");
			if(TablaSimbolo.contains(this.s) )  			// Si la cte ya está en la TS, aumenta la cantidad de referencias, preguntar
				//TablaSimbolo.get(s.getValor()).increase_Ref();
				return TablaToken.get("FLOAT");
			else {
				TablaSimbolo.put(s.getValor(),s);
				return TablaToken.get("FLOAT");
			}
		}
		else
			if ( (flotante<minimalValorFloat) ||  (flotante>minValorFloat && flotante<cero) || (flotante>cero && flotante<maxValorFloat) || (flotante>maximalValorFloat))
				return -3;	  // Retornar -4, para Warning de fuera de rango
			else 
				return 0;
	}

	@Override
	public boolean acomodarLinea() {
		return true;
	}

}
