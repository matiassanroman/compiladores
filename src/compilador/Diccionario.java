package compilador;

import java.util.Hashtable;

public class Diccionario {

	static Hashtable<Integer, Integer> diccionario= new Hashtable<Integer, Integer>();
	
	public Diccionario() {
		// REVISAR CADA SIMBOLO Y AGREGAR O SACAR LO QUE CORRESPONDA
		diccionario.put(1, 0);		//L
		diccionario.put(2, 1);		//D
		diccionario.put(10, 2);		// /n
		diccionario.put(13, 2);		// /n
		diccionario.put(32, 3);		// espacio
		diccionario.put(9, 3);		// tab
		diccionario.put(45, 4);		//-
		diccionario.put(43, 5);		// +
		diccionario.put(60, 6);		//<
		diccionario.put(62, 7);		//>
		diccionario.put(61, 8);		//=
		diccionario.put(42, 9);     //*
		diccionario.put(47, 10);	// / 
		diccionario.put(40, 11);	// (	
		diccionario.put(41, 12);	// )
		diccionario.put(58, 13);	// :
		diccionario.put(44, 14);	// ,
		diccionario.put(59, 15);	// ;
		diccionario.put(95, 16);	// _
		diccionario.put(37, 17);	// %
		diccionario.put(46, 18);	// .
	}
	
	public int asciiToColumna(int ascii){
		if((ascii >= 65 && ascii <= 90)||(ascii>= 97 && ascii <= 122)) { return diccionario.get(1); }	
		else if (ascii >= 48 && ascii <= 57)     { return diccionario.get(2); }
		else if (diccionario.containsKey(ascii)) { return diccionario.get(ascii); }
			else return 19;		
		}
	
	public static boolean contiene(int clave) { return diccionario.containsKey(clave); }
	
}
