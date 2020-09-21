package compilador;
import java.util.HashMap;
import java.util.Hashtable;

import accionesSemanticas.*;

public class MatrizAccionSemantica {
	
	private static int nroLinea= 1;
	static StringBuffer buffer = new StringBuffer();
	static Hashtable<String,Simbolo> tablaSimbolo = new Hashtable<String,Simbolo>();
	private static HashMap<String, Integer> tablaToken = new HashMap<String,Integer>();
	
	//Acciones Semanticas
	static AccionSemantica as1_agregar_buffer = new AS1_Agregar_Buffer();
	static AccionSemantica as2_verificar_longitud_id = new AS2_Verificar_Longitud_Id(tablaSimbolo, tablaToken); 
	static AccionSemantica as3_devolver_pr = new AS3_Devolver_PR(tablaSimbolo, tablaToken);
	static AccionSemantica as4_end_comentario = new AS4_End_Comentario();
	static AccionSemantica as5_end_cadena = new AS5_End_Cadena();
	static AccionSemantica as6_end_simbolo = new AS6_End_Simbolo();
	static AccionSemantica as7_end_simbolo_simple = new AS7_End_Simbolo_Simple();
	static AccionSemantica as8_end_simbolo_complejo = new AS8_End_Simbolo_Complejo();
	static AccionSemantica as9_verificar_rango_number = new AS9_Verificar_Rango_Number();
	
										// Mapeado caracter_columna
										//  < > ! = . % " flmin lmay blanco nl tab c  d  +  -  _  *  /  {  }  (  )  i  ,  ;  eof
	private int[][] matrizAccionSemantica = {};
	
	public MatrizAccionSemantica() {
	}

	public int[][] getMatrizAccionSemantica() {
		return matrizAccionSemantica;
	}

	public void setMatrizAccionSemantica(int[][] matrizAccionSemantica) {
		this.matrizAccionSemantica = matrizAccionSemantica;
	}}
