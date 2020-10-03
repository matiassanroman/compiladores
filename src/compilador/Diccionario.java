package compilador;

import java.util.Hashtable;

public class Diccionario {

	static Hashtable<Integer, Integer> diccionario= new Hashtable<Integer, Integer>();
	
	public Diccionario() {
		
//		diccionario.put(60, 0); 	// <
	//	diccionario.put(62, 1); 	// >
	//	diccionario.put(33, 2); 	// !
		diccionario.put(61, 1); 	// =
//			diccionario.put(46, 4); 	// .
//		diccionario.put(37, 5); 	// % 
//		/diccionario.put(34, 6); 	// "
//	diccionario.put(102, 7); 	// f
	diccionario.put(0, 0); 		// lmin
//	diccionario.put(1, 9); 		// lmay 
///		diccionario.put(32, 10); 	// blanco
//		diccionario.put(13, 2); 	// nl
//		diccionario.put(9, 12); 	// tab
//		//diccionario.put(14, 13); 	// c
		diccionario.put(15, 3); 	// d
//		diccionario.put(43, 0); 	// +
//		diccionario.put(45, 1); 	// -	 
		diccionario.put(95, 4); 	// _
//		diccionario.put(42, 18); 	// *
//		diccionario.put(47, 2); 	// /
//		diccionario.put(123, 20); 	// {
//		diccionario.put(125, 21); 	// }
	//	diccionario.put(40, 22); 	// (
	//	diccionario.put(41, 23); 	// )
	//	diccionario.put(105, 24); 	// i
	//	diccionario.put(44, 25); 	// ,
		diccionario.put(59, 2); 	// ;
		//diccionario.put(3, 27); 	// eof
		
	}
	
	public int asciiToColumna(int ascii){
		//MAYUSCULA
		if(ascii >= 65 && ascii <= 90) { return diccionario.get(1); }
		//MINUSCULA
		else if (ascii>= 97 && ascii <= 122) { return diccionario.get(0); }
		//DECIMALES
		else if (ascii >= 48 && ascii <= 57)     { return diccionario.get(15); }
		else if (diccionario.containsKey(ascii)) { return diccionario.get(ascii); }
		else return 5;		
		}
	
	public static boolean contiene(int clave) { return diccionario.containsKey(clave); }
	
}
