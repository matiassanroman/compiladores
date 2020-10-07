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
		String numero = buffer.toString();
		double flotante;
		if (numero.contains("f"))
			flotante = Double.parseDouble(numero.replace('f', 'E'));
		else
			flotante = Double.parseDouble(numero);
		this.s = new Simbolo(numero);
		//flotante = Double.parseDouble(buffer.toString());
		// Si la 
		if ( ((flotante>=minimalValorFloat) && (flotante <= minValorFloat)) || (flotante==cero)  || ((flotante>=maxValorFloat) && (flotante <= maximalValorFloat)) ) {
			s.setTipo("float");
			// Si la cte ya está en la TS, retornar reference
			if(TablaSimbolo.contains(this.s) )  return TablaToken.get("FLOAT");
			else {
				s.setUso("CTE");
				TablaSimbolo.put(s.getValor(),s);
				return TablaToken.get("CTE");
			}
		}
		else {   // SI esta fuera de los rangos retornar error
			if ( (flotante<minimalValorFloat) ||  (flotante>minValorFloat && flotante<cero) || (flotante>cero && flotante<maxValorFloat) || (flotante>maximalValorFloat))
				return -1;	  // Retorna -1 codigo de error
			else 
				return 0; }
	}

	@Override
	public boolean acomodarLinea() {
		return true;
	}

}
