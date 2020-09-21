
// Clases central desde donde se invoca todo

package compilador;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import accionesSemanticas.*;

public class Compilador {
	
	
	static StringBuffer buffer = new StringBuffer();
	public static void limpiarBuffer() { buffer.delete(0, buffer.length()); }
	@SuppressWarnings("unused")
	private static int nroLinea= 1;
	static         Hashtable<String,Simbolo> tablaSimbolo = new Hashtable<String,Simbolo>();
	private static HashMap<String, Integer>  tablaToken   = new HashMap<String,Integer>();
	
	//Acciones Semanticas
	static AccionSemantica as1_agregar_buffer = new AS1_Agregar_Buffer();
	static AccionSemantica as2_verificar_longitud_id = new AS2_Verificar_Longitud_Id(tablaSimbolo, tablaToken); 
	static AccionSemantica as3_devolver_pr = new AS3_Devolver_PR(tablaSimbolo, tablaToken);
	static AccionSemantica as4_end_comentario = new AS4_Fin_Comentario();
	static AccionSemantica as5_end_cadena = new AS5_Fin_Cadena(tablaSimbolo, tablaToken);
	static AccionSemantica as6_end_simbolo = new AS6_Fin_Simbolo();
	static AccionSemantica as7_end_simbolo_simple = new AS7_Fin_Simbolo_Simple();
	static AccionSemantica as8_end_simbolo_complejo = new AS8_Fin_Simbolo_Complejo();
	static AccionSemantica as9_verificar_rango_cte = new AS9_Verificar_Rango_Constante(tablaSimbolo, tablaToken);
	static AccionSemantica as10_verificar_float = new AS10_Verificar_Rango_Float(tablaSimbolo, tablaToken);
	
	int[][] matrizTEstados = 
// Mapeado caracter-columna
//            <   >   !   =   .   %   "   f lmin lmay blanco nl  tab c   d   +   -   _   *   /   {   }   (   )   i   ,   ;   eof
//            0   1   2   3   4   5   6   7   8    9    10   11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27
/*0*/		{{1 , 2 , 3 , 4 , 17, 5 , 9 , 19, 7 ,  8 ,  0 ,  0 , 0 , 19, 12, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*1*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*2*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*3*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*4*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*5*/		 {19, 19, 19, 19, 19, 6,  19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*6*/		 {19, 19, 19, 19, 19, 6,  19, 19, 19,  19,  19,  0 , 19, 6 , 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*7*/		 {19, 19, 19, 19, 19, 19, 19, 19, 7 ,  19,  19,  19, 19, 19, 7 , 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*8*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  8 ,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*9*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 9 , 19, 19, 10, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*10*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  9 , 19, 11, 19, 19, 10, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*11*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 11, 19, 19, 10, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*12*/		 {19, 19, 19, 19, 13, 19, 19, 19, 19,  19,  19,  19, 19, 19, 12, 19, 19, 16, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*13*/		 {19, 19, 19, 19, 19, 19, 19, 14, 19,  19,  19,  19, 19, 19, 13, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*14*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 15, 15, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*15*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 15, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*16*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*17*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*18*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*19*/		 {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19}, };
	
	
//	AccionSemantica[][] matrizASemanticas =
	// Mapeado caracter-columna
//  <   >   !   =   .   %   "   f lmin lmay blanco nl  tab c   d   +   -   _   *   /   {   }   (   )   i   ,   ;   eof
//  0   1   2   3   4   5   6   7   8    9    10   11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27
/*0*/	//	{{as1_inic_Buffer , as1_inic_Buffer , as1_inic_Buffer , as1_inic_Buffer , as1_inic_Buffer, as6_inic_Cadena_Comentario , as16_No_action , as16_No_action, as1_inic_Buffer ,  as1_inic_Buffer ,  as15_count_New_Line , as15_count_New_Line , as15_count_New_Line , as16_No_action, as1_inic_Buffer, as10_inic_BufferSimple, as10_inic_BufferSimple, as16_No_action, as10_inic_BufferSimple,as10_inic_BufferSimple, as10_inic_BufferSimple, as10_inic_BufferSimple, as10_inic_BufferSimple, as10_inic_BufferSimple, as16_No_action, as10_inic_BufferSimple, as10_inic_BufferSimple, as11_skip_Caracter},
/*1*/	//	 {as16_No_action, as16_No_action, as16_No_action, 19,             as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*2*/	//	 {as16_No_action, as16_No_action, as16_No_action, 19,             as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*3*/	//	 {as16_No_action, as16_No_action, as16_No_action, 19,             as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*4*/	//	 {as16_No_action, as16_No_action, as16_No_action, 19,             as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*5*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 6,  19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*6*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 6,  19, 19, 19,  19,  19,  0 , 19, 6 , 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*7*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 7 ,  19,  19,  19, 19, 19, 7 , 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*8*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  8 ,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*9*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 9 , 19, 19, 10, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*10*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  9 , 19, 11, 19, 19, 10, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*11*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 11, 19, 19, 10, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*12*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, 13, 19, 19, 19, 19,  19,  19,  19, 19, 19, 12, 19, 19, 16, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*13*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 14, 19,  19,  19,  19, 19, 19, 13, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*14*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 15, 15, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*15*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 15, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*16*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*17*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*18*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*19*/	//	 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19}, };
	public static void main(String[] args) throws IOException {
		
		// Obtengo la ruta del archivo de los argumentos de programa
		String ruta = args[0];
		Archivo archivo = new Archivo();
		// Cargo el archivo para poder usarlo
		archivo.cargarArchivo(ruta);

	}

}
