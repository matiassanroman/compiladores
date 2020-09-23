
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
	static AccionSemantica as11_no_accion = new AS11_No_Accion();
	
	int[][] matrizTEstados = 
// Mapeado caracter-columna
//     <   >   !   =   .   %   "   f lmin lmay blanco nl  tab c   d   +   -   _   *   /   {   }   (   )   i   ,   ;   eof
//     0   1   2   3   4   5   6   7   8    9    10   11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26  27
/*0*/{{1 , 2 , 3 , 4 , 17, 5 , 9 , 19, 7 ,  8 ,  0 ,  0 , 0 , 19, 12, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*1*/ {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*2*/ {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*3*/ {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*4*/ {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*5*/ {19, 19, 19, 19, 19, 6,  19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*6*/ {19, 19, 19, 19, 19, 6,  19, 19, 19,  19,  19,  0 , 19, 6 , 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*7*/ {19, 19, 19, 19, 19, 19, 19, 19, 7 ,  19,  19,  19, 19, 19, 7 , 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*8*/ {19, 19, 19, 19, 19, 19, 19, 19, 19,  8 ,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*9*/ {19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 9 , 19, 19, 10, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*10*/{19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  9 , 19, 11, 19, 19, 10, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*11*/{19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 11, 19, 19, 10, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*12*/{19, 19, 19, 19, 13, 19, 19, 19, 19,  19,  19,  19, 19, 19, 12, 19, 19, 16, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*13*/{19, 19, 19, 19, 19, 19, 19, 14, 19,  19,  19,  19, 19, 19, 13, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*14*/{19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 15, 15, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*15*/{19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 15, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*16*/{19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*17*/{19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*18*/{19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 18, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19},
/*19*/{19, 19, 19, 19, 19, 19, 19, 19, 19,  19,  19,  19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19}, };
	
	
	AccionSemantica[][] matrizASemanticas =
// Mapeado caracter-columna
//     <                   >                   !                   =                         .                   %                   "               f                   lmin                lmay                blanco          nl                  tab             c                          d                   +                   -                   _                   *                /                {                }                (                )                i                        ,                ;                eof
//     0                   1                   2                   3                         4                   5                   6               7                   8                   9                   10              11                  12              13                         14                  15                  16                  17                  18               19               20               21               22               23               24                       25               26               27
/*0*/{{as1_agregar_buffer, as1_agregar_buffer, as1_agregar_buffer, as1_agregar_buffer      , as1_agregar_buffer, as1_agregar_buffer, as11_no_accion, as11_no_accion    , as1_agregar_buffer, as1_agregar_buffer, as11_no_accion, as11_no_accion    , as11_no_accion, as11_no_accion           , as1_agregar_buffer, as6_end_simbolo   , as6_end_simbolo   , as11_no_accion    , as6_end_simbolo, as6_end_simbolo, as6_end_simbolo, as6_end_simbolo, as6_end_simbolo, as6_end_simbolo, as11_no_accion         , as6_end_simbolo, as6_end_simbolo, as11_no_accion},
/*1*/ {as11_no_accion    , as11_no_accion    , as11_no_accion    , as8_end_simbolo_complejo, as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as7_end_simbolo_simple   , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*2*/ {as11_no_accion    , as11_no_accion    , as11_no_accion    , as8_end_simbolo_complejo, as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as7_end_simbolo_simple   , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*3*/ {as11_no_accion    , as11_no_accion    , as11_no_accion    , as8_end_simbolo_complejo, as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as11_no_accion           , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*4*/ {as11_no_accion    , as11_no_accion    , as11_no_accion    , as8_end_simbolo_complejo, as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as7_end_simbolo_simple   , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*5*/ {as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as1_agregar_buffer, as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as11_no_accion           , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*6*/ {as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as4_end_comentario, as11_no_accion, as1_agregar_buffer       , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*7*/ {as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as1_agregar_buffer, as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as2_verificar_longitud_id, as1_agregar_buffer, as11_no_accion    , as11_no_accion    , as1_agregar_buffer, as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*8*/ {as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as1_agregar_buffer, as11_no_accion, as11_no_accion    , as11_no_accion, as4_end_comentario       , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*9*/ {as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as1_agregar_buffer       , as11_no_accion    , as11_no_accion    , as1_agregar_buffer, as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*10*/{as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as5_end_cadena, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as1_agregar_buffer       , as11_no_accion    , as11_no_accion    , as1_agregar_buffer, as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*11*/{as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as1_agregar_buffer       , as11_no_accion    , as11_no_accion    , as1_agregar_buffer, as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*12*/{as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as1_agregar_buffer, as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as11_no_accion           , as1_agregar_buffer, as11_no_accion    , as11_no_accion    , as1_agregar_buffer, as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*13*/{as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as11_no_accion, as1_agregar_buffer, as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as10_verificar_float     , as1_agregar_buffer, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*14*/{as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as11_no_accion           , as11_no_accion    , as1_agregar_buffer, as1_agregar_buffer, as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*15*/{as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as10_verificar_float     , as1_agregar_buffer, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*16*/{as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as11_no_accion           , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as9_verificar_rango_cte, as11_no_accion , as11_no_accion , as11_no_accion},
/*17*/{as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as11_no_accion           , as1_agregar_buffer, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*18*/{as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as9_verificar_rango_cte  , as1_agregar_buffer, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
/*19*/{as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as11_no_accion           , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion}, };
	
	public static void main(String[] args) throws IOException {
		
		// Obtengo la ruta del archivo de los argumentos de programa
		String ruta = args[0];
		Archivo archivo = new Archivo();
		// Cargo el archivo para poder usarlo
		archivo.cargarArchivo(ruta);

	}

}
