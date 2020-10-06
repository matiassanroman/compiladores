package accionesSemanticas;

import java.util.HashMap;
import java.util.Hashtable;

import compilador.Simbolo;

public class AS10_Verificar_Rango_Float extends AccionSemantica{

	Hashtable<String,Simbolo> TablaSimbolo;
	HashMap<String,Integer> TablaToken;  
	Simbolo s;

	// Se definen los rangos de las variables
	// tipo float
	double minimalValorFloat = -3.40282347E38;
	double minValorFloat     = -1.17549435E-38;
	double maxValorFloat     =  1.17549435E-38;
	double maximalValorFloat =  3.40282347E38;
	double cero = 0.0;
	
	// Contructor
	public AS10_Verificar_Rango_Float(Hashtable<String, Simbolo> tablaSimbolo, HashMap<String, Integer> tablaToken) {
		TablaSimbolo = tablaSimbolo;
		TablaToken = tablaToken;
	}

	
	public int execute(StringBuffer buffer, char c) {
		this.s = new Simbolo(buffer.toString());
		double flotante = Double.parseDouble(buffer.toString());
		System.out.println("END: " + flotante);
		// Si la 
		if ( ((flotante>=minimalValorFloat) && (flotante <= minValorFloat)) || (flotante==cero)  || ((flotante>=maxValorFloat) && (flotante <= maximalValorFloat)) ) {
			System.out.println("ENTRO 1");
			s.setTipo("float");
			// Si la cte ya est� en la TS, retornar referencia
			if(TablaSimbolo.contains(this.s) )  return TablaToken.get("FLOAT");
			else {
				s.setUso("CTE");
				TablaSimbolo.put(s.getValor(),s);
				return TablaToken.get("FLOAT");
			}
		}
		else {System.out.println("ENTRO 2");  // SI esta fuera de los rangos retornar error
			if ( (flotante<minimalValorFloat) ||  (flotante>minValorFloat && flotante<cero) || (flotante>cero && flotante<maxValorFloat) || (flotante>maximalValorFloat))
				return -4;	  // Retornar -4, para ERROR de fuera de rango
			else 
				return 0; }
	}

	@Override
	public boolean acomodarLinea() {
		return true;
	}

}
