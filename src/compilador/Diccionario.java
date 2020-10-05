package compilador;

import java.util.Hashtable;

public class Diccionario {

	static Hashtable<Integer, Integer> diccionario= new Hashtable<Integer, Integer>();
	
	public Diccionario() {
		
		diccionario.put(60, 20); 	// <
		diccionario.put(62, 21); 	// >
		diccionario.put(33, 22); 	// !
//		diccionario.put(102, 7); 	// f
//		diccionario.put(46, 4); 	// .
//		diccionario.put(9, 12); 	// tab
//		//diccionario.put(14, 13); 	// c
		//	diccionario.put(105, 24); 	// i
		//diccionario.put(3, 27); 	// eof
	
		diccionario.put(61, 2); 	// =
		diccionario.put(37, 6); 	// % 
		diccionario.put(34, 8); 	// "
		diccionario.put(0, 0); 		// lmin
		diccionario.put(1, 1); 		// lmay 
		diccionario.put(32, 12); 	// blanco
		diccionario.put(13, 7); 	// nl
		diccionario.put(10, 13); 	// nl
		diccionario.put(15, 4); 	// d
		diccionario.put(43, 14); 	// +
		diccionario.put(45, 9); 	// -	 
		diccionario.put(95, 5); 	// _
		diccionario.put(42, 15); 	// *
		diccionario.put(47, 16); 	// /
		diccionario.put(123, 18); 	// {
		diccionario.put(125, 19); 	// }
		diccionario.put(40, 10); 	// (
		diccionario.put(41, 11); 	// )
		diccionario.put(44, 17); 	// ,
		diccionario.put(59, 3); 	// ;
		
	}
	
	public int asciiToColumna(int ascii){
		//MAYUSCULA
		if(ascii >= 65 && ascii <= 90) { return diccionario.get(1); }
		//MINUSCULA
		else if (ascii>= 97 && ascii <= 122) { return diccionario.get(0); }
		//DECIMALES
		else if (ascii >= 48 && ascii <= 57)     { return diccionario.get(15); }
		else if (diccionario.containsKey(ascii)) { return diccionario.get(ascii); }
		else return 23;		
		}
	
	public static boolean contiene(int clave) { return diccionario.containsKey(clave); }
	
}
