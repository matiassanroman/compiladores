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
		       ArrayList<Simbolo> aux =  eliminarRepetidos(tablaSimbolo.get(str));
	    	   for(int i=0; i<aux.size(); i++) {
	    		   if (aux.get(i).getTipo().equals("Proc")) { 
	    			   salida.write("Clave: " + str + "\t Value: " + str + "\t Uso: " + aux.get(i).getUso() + "\t TipoDeUso: " + aux.get(i).getTipo() + "\t Ambito: " + aux.get(i).getAmbito() + "\t Declarada: " + aux.get(i).isDeclarada() + "\t CantParametros: " + aux.get(i).getCantParametros() + "\t NA: " + aux.get(i).getNa() + "\t NS: " + aux.get(i).getNs());
	    		   }
	    		   else if (aux.get(i).getTipo().equals("PARAM_PROC")) {
	    			   salida.write("Clave: " + str + "\t Value: " + str + "\t Uso: " + aux.get(i).getUso() + "\t TipoDeUso: " + aux.get(i).getTipo() + "\t Ambito: " + aux.get(i).getAmbito() + "\t Declarada: " + aux.get(i).isDeclarada() + "\t tipoParametro: " + aux.get(i).getTipoParametro() + "\t pasajeParametro: " + aux.get(i).getPasajeParametro() );
	    		   }
	    		   else {
	    			   if(aux.get(i).getUso().equals("ID"))
	    				   salida.write("Clave: " + aux.get(i).getValor() + "\t Value: " + aux.get(i).getValor() + "\t Uso: " + aux.get(i).getUso() + "\t TipoDeUso: " + aux.get(i).getTipo() +  "\t Ambito: " + aux.get(i).getAmbito() + "\t Declarada: " + aux.get(i).isDeclarada() + "\t tipoVar: " + aux.get(i).getTipoParametro() );
	    			   else if(aux.get(i).getUso().equals("CTE")) {
	    				   if(aux.get(i).getTipo().equals("int"))
	    					   salida.write("Clave: " + aux.get(i).getValor() + "\t Value: " + aux.get(i).getValor() + "\t Uso: " + aux.get(i).getUso() + "\t TipoDeUso: " + "CTE" + "\t Ambito: " + aux.get(i).getAmbito() + "\t Declarada: " + aux.get(i).isDeclarada() + "\t tipoCTE: " + aux.get(i).getTipoParametro() );
	    				   else if(aux.get(i).getTipo().equals("float"))
	    					   salida.write("Clave: " + aux.get(i).getValor() + "\t Value: " + aux.get(i).getValor() + "\t Uso: " + aux.get(i).getUso() + "\t TipoDeUso: " + "CTE" + "\t Ambito: " + aux.get(i).getAmbito() + "\t Declarada: " + aux.get(i).isDeclarada() + "\t tipoCTE: " + aux.get(i).getTipoParametro() );
	    				   else
	    					   salida.write("Clave: " + aux.get(i).getValor() + "\t Value: " + aux.get(i).getValor() + "\t Uso: " + aux.get(i).getUso() + "\t TipoDeUso: " + aux.get(i).getTipo() + "\t Ambito: " + aux.get(i).getAmbito() + "\t Declarada: " + aux.get(i).isDeclarada() + "\t tipoCTE: " + aux.get(i).getTipoParametro() );
	    			   }
	    			   else {
	    				   salida.write("Clave: " + aux.get(i).getValor() + "\t Value: " + aux.get(i).getValor() + "\t Uso: " + aux.get(i).getUso() + "\t TipoDeUso: " + aux.get(i).getTipo() +  "\t Ambito: " + aux.get(i).getAmbito() );
	    				   
	    			   }
	    				   
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
	
	public static ArrayList<Simbolo> eliminarRepetidos(ArrayList<Simbolo> l){
		ArrayList<Simbolo> aux = new ArrayList<Simbolo>();
	    boolean p = true;
	    
	    for(int i=0; i<l.size(); i++) {
	    	//Es una declaracion de ID
	    	if(l.get(i).isDeclarada()) 
	    		aux.add(l.get(i));	    
	    	//Es una CTE de NA o NS
	    	if(l.get(i).getUso().equals("CTE") && (l.get(i).getTipo().equals("NA_PROC")) || (l.get(i).getTipo().equals("NS_PROC")))
	    		aux.add(l.get(i));
	    }
	    
	    for(int i=0; i<l.size(); i++) {
	    	if(p) {
	    		if(l.get(i).getUso().equals("CTE") && !l.get(i).getTipo().equals("NA_PROC") && !l.get(i).getTipo().equals("NS_PROC")) {
	    			p = false;
	    			aux.add(l.get(i));
	    		}
	    		else if(l.get(i).getUso().equals("CADENA")){
	    			p = false;
	    			aux.add(l.get(i));
	    		}
	    	}
	    	else{
	    		boolean r = true;
	    		for(int j=0; j<aux.size(); j++) {
	    			if(aux.get(j).getUso().equals("CTE") && !aux.get(j).getTipo().equals("NA_PROC") && !aux.get(j).getTipo().equals("NS_PROC")) {
	    				if(aux.get(j).ambitoSinNombre().equals(l.get(i).ambitoSinNombre()))
	    					r = false;
	    			}
	    			else if(aux.get(j).getUso().equals("CADENA")){
	    				if(aux.get(j).ambitoSinNombre().equals(l.get(i).ambitoSinNombre()))
	    					r = false;
		    		}
	    		}
	    		if(r)
	    			aux.add(l.get(i));
	    	}		
	    }
	    return aux;
	}
	
}
