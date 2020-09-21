
// Clases central desde donde se invoca todo

package compilador;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import accionesSemanticas.*;

public class Compilador {
	static StringBuffer buffer = new StringBuffer();
	public static void limpiarBuffer() { buffer.delete(0, buffer.length()); }
	private static int nroLinea= 1;
	static Hashtable<String,Simbolo> tablaSimbolo = new Hashtable<String,Simbolo>();
	private static HashMap<String, Integer> tablaToken = new HashMap<String,Integer>();
	
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
	
	//Acciones Semanticas
	static AccionSemantica as1_inic_Buffer = new AS1_inic_Buffer();
	static AccionSemantica as2_add_Buffer = new AS2_add_Buffer();
	static AccionSemantica as3_check_Longitud = new AS3_check_Longitud(tablaSimbolo, tablaToken); 
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
	static AccionSemantica as16_No_action = new AS16_No_Action();
	
	
	AccionSemantica[][] matrizASemanticas =
	// Mapeado caracter-columna
//  <   >   !   =   .   %   "   f lmin lmay blanco nl  tab c   d   +   -   _   *   /   {   }   (   )   i   ,   ;   eof
//  0   1   2   3   4   5   6   7   8    9    10   11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27
/*0*/		{{as1_inic_Buffer , as1_inic_Buffer , as1_inic_Buffer , as1_inic_Buffer , as1_inic_Buffer, as6_inic_Cadena_Comentario , as16_No_action , as16_No_action, as1_inic_Buffer ,  as1_inic_Buffer ,  as15_count_New_Line , as15_count_New_Line , as15_count_New_Line , as16_No_action, as1_inic_Buffer, as10_inic_BufferSimple, as10_inic_BufferSimple, as16_No_action, as10_inic_BufferSimple,as10_inic_BufferSimple, as10_inic_BufferSimple, as10_inic_BufferSimple, as10_inic_BufferSimple, as10_inic_BufferSimple, as16_No_action, as10_inic_BufferSimple, as10_inic_BufferSimple, as11_skip_Caracter},
/*1*/		 {as16_No_action, as16_No_action, as16_No_action, 19,             as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*2*/		 {as16_No_action, as16_No_action, as16_No_action, 19,             as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*3*/		 {as16_No_action, as16_No_action, as16_No_action, 19,             as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*4*/		 {as16_No_action, as16_No_action, as16_No_action, 19,             as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*5*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 6,  19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*6*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 6,  19, 19, 19,  19,  19,  0 , 19, 6 , 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*7*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 7 ,  19,  19,  19, 19, 19, 7 , 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*8*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  8 ,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*9*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 9 , 19, 19, 10, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*10*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  9 , 19, 11, 19, 19, 10, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*11*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 11, 19, 19, 10, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*12*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, 13, 19, 19, 19, 19,  19,  19,  19, 19, 19, 12, 19, 19, 16, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*13*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 14, 19,  19,  19,  19, 19, 19, 13, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*14*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 15, 15, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*15*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 15, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*16*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*17*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*18*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*19*/		 {as16_No_action, as16_No_action, as16_No_action, as16_No_action, as16_No_action, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19}, };
	public static void main(String[] args) throws IOException {
		
		// Obtengo la ruta del archivo de los argumentos de programa
		String ruta = args[0];
		Archivo archivo = new Archivo();
		// Cargo el archivo para poder usarlo
		archivo.cargarArchivo(ruta);

	}

}
