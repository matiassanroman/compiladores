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
			
			
			Hashtable<String,Simbolo> tablaSimbolo = c.getTablaSimbolo();
			salida.write("/*  Tabla de Simbolos  */");
			salida.newLine();
			salida.newLine();
		
			/*
			Simbolo s1 = new Simbolo("ariel".toString());
			s1.setUso("ID");
			Simbolo s2 = new Simbolo("matias".toString());	
			s2.setUso("CADENA");
			
			tablaSimbolo.put(s1.getValor(),s1);
			tablaSimbolo.put(s2.getValor(),s2);
			*/
			
			Set<String> keys = tablaSimbolo.keySet();
		    Iterator<String> itr = keys.iterator();
		    String str;
		    
		    while (itr.hasNext()) { 
		       str = itr.next();
		       salida.write("Clave: " + str + "\t Value: " + str + "\t Uso: " + tablaSimbolo.get(str).getUso());
		       salida.newLine();
		    }
		    
			salida.close();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
}
