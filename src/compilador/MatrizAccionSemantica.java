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
	static AccionSemantica as1_inic_Buffer = new AS1_inic_Buffer();
	static AccionSemantica as2_add_Buffer = new AS2_add_Buffer();
	static AccionSemantica as3_check_Longitud = new AS3_check_Longitud(); 
	static AccionSemantica as4_return_Last_Caracter_PR = new AS4_return_Last_Caracter_PR();
	static AccionSemantica as5_count_Line = new AS5_count_Line();
	static AccionSemantica as6_inic_Cadena_Comentario = new AS6_inic_Cadena_Comentario();
	static AccionSemantica as7_end_Cadena = new AS7_end_Cadena();
	static AccionSemantica as8_delete_Caracter = new AS8_delete_Caracter();
	static AccionSemantica as9_count_NewLine_Comentario = new AS9_count_NewLine_Comentario();
	static AccionSemantica as10_inic_BufferSimple = new AS10_inic_BufferSimple();
	static AccionSemantica as11_skip_Caracter = new AS11_skip_Caracter();
	static AccionSemantica as12_not_Return_Last_Caracter = new AS12_not_Return_Last_Caracter();
	static AccionSemantica as13_return_Last_Caracter = new AS13_return_Last_Caracter();
	static AccionSemantica as14_range_Value = new AS14_range_Value();
	static AccionSemantica as15_count_New_Line = new AS15_count_New_Line();

	
	
	
	
	static AccionSemantica AgregarBuffer = new AS_AgregarBuffer();
	static AccionSemantica Fin_ID_PR = new AS_Fin_ID_PR(TablaSimbolo, TablaToken);
	static AccionSemantica FinCons = new AS_FinConst(TablaSimbolo, TablaToken);
	static AccionSemantica FinSimbolo = new AS_FinSimbolo();
	static AccionSemantica FinSimboloComp = new AS_FinSimboloCompuesto(TablaToken);
	static AccionSemantica FinSimboloSimple = new AS_FinSimboloSimple();
	static AccionSemantica NoAction = new AS_NoAction();
	static AccionSemantica FinCadena = new AS_FinCadena(TablaSimbolo, TablaToken);
	static AccionSemantica Error = new AS_Error();
	static AccionSemantica Error_AL = new AS_Error_AL();	
	static AccionSemantica FinComentario = new AS_FinComentario();
	
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
