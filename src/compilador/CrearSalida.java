package compilador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unused")
public class CrearSalida {

	public static void crearTxtSalida(Compilador c) {
		try {
			String ruta = "Tabla de Simbolos.txt";
			BufferedWriter salida = new BufferedWriter(new FileWriter(ruta));
			
			salida.write("/*  Resultados de la compilacion  */");
			salida.newLine();
			salida.newLine();
			
			
			Hashtable<String,ArrayList<Simbolo>> tablaSimbolo = c.getTablaSimbolo();
			salida.write("/*  Tabla de Simbolos  */");
			salida.newLine();
			salida.newLine();
			
			Set<String> keys = tablaSimbolo.keySet();
		    Iterator<String> itr = keys.iterator();
		    String str;
		    
		    while (itr.hasNext()) { 
		       str = itr.next();
	    	   for(int i=0; i<tablaSimbolo.get(str).size(); i++) {
	    		   if (tablaSimbolo.get(str).get(i).getTipo().equals("Proc")) { 
	    			   salida.write("Clave: " + str + "\t Value: " + str + "\t Uso: " + tablaSimbolo.get(str).get(i).getUso() + "\t Ambito: " + tablaSimbolo.get(str).get(i).getAmbito() + "\t Tipo: " + tablaSimbolo.get(str).get(i).getTipo() + "\t Declarada: " + tablaSimbolo.get(str).get(i).isDeclarada() + "\t CantParametros: " + tablaSimbolo.get(str).get(i).getCantParametros() + "\t NA: " + tablaSimbolo.get(str).get(i).getNa() + "\t NS: " + tablaSimbolo.get(str).get(i).getNs());
	    		   }
	    		   else if (tablaSimbolo.get(str).get(i).getTipo().equals("PARAM_PROC")) {
	    			   salida.write("Clave: " + str + "\t Value: " + str + "\t Uso: " + tablaSimbolo.get(str).get(i).getUso() + "\t Ambito: " + tablaSimbolo.get(str).get(i).getAmbito() + "\t Tipo: " + tablaSimbolo.get(str).get(i).getTipo() + "\t Declarada: " + tablaSimbolo.get(str).get(i).isDeclarada() + "\t tipoParametro: " + tablaSimbolo.get(str).get(i).getTipoParametro() + "\t pasajeParametro: " + tablaSimbolo.get(str).get(i).getPasajeParametro() );
	    		   }
	    		   else {
	    			   salida.write("Clave: " + str + "\t Value: " + str + "\t Uso: " + tablaSimbolo.get(str).get(i).getUso() + "\t Ambito: " + tablaSimbolo.get(str).get(i).getAmbito() + "\t Tipo: " + tablaSimbolo.get(str).get(i).getTipo() + "\t Declarada: " + tablaSimbolo.get(str).get(i).isDeclarada());
	    		   } 
	    		   salida.newLine();
		       }
		    }
		    
		    
		    
			salida.close();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
}
