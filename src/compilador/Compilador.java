
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
	static Diccionario diccionario = new Diccionario();
	private static boolean acomodarLinea= false; // acomodar linea y tomar la lectura anterior
	Archivo archivo = new Archivo();
	protected static int asciiAnterior = 0;
	
	//No ponemos privado para evitar mas metodos y que se pueda acceder de cualquier lado. 
	static Hashtable<String,Simbolo> tablaSimbolo = new Hashtable<String,Simbolo>();
	private static HashMap<String, Integer> tablaToken = new HashMap<String,Integer>();

	
	//Acciones Semanticas
	static AccionSemantica as1_agregar_buffer = new AS1_Agregar_Buffer();
	static AccionSemantica as2_verificar_longitud_id = new AS2_Verificar_Longitud_Id(tablaSimbolo, tablaToken); 
	static AccionSemantica as3_devolver_pr = new AS3_Devolver_PR(tablaSimbolo, tablaToken);
	static AccionSemantica as4_end_comentario = new AS4_Fin_Comentario();
	static AccionSemantica as5_end_cadena = new AS5_Fin_Cadena(tablaSimbolo, tablaToken);
	static AccionSemantica as6_end_simbolo = new AS6_Fin_Simbolo();
	static AccionSemantica as7_end_simbolo_simple = new AS7_Fin_Simbolo_Simple();
	static AccionSemantica as8_end_simbolo_complejo = new AS8_Fin_Simbolo_Complejo(tablaToken);
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
	
	
	// Metodo que retorna la tabla de simbolos
	public Hashtable<String,Simbolo> getTablaSimbolo(){
		return this.tablaSimbolo;
	}

	// Metodo que sirve para pedir tokens, EXPLICACION A COMPLETAR
	public Token getToken() throws IOException {
		Token token = new Token();
		//int asciiAnterior; 
		
		if(asciiAnterior == -1){    	//Fin del archivo, devuelve 0
			token.setToken(0); 
			return token;
		}		
		
		int estadoSiguiente = 0;
		int estadoActual = 0;
		boolean hayToken = false;
		int asciiActual;
		
		do{	
			if (acomodarLinea){
				asciiActual = asciiAnterior;
				acomodarLinea = false;
			}
			else {
				asciiActual = archivo.leerBuffer(); 
				if(asciiActual == 13) { nroLinea++; }
			}
			
			asciiAnterior = asciiActual;
			int columna = diccionario.asciiToColumna(asciiActual);
			estadoSiguiente = matrizTEstados[estadoActual][columna];
			AccionSemantica AS = matrizASemanticas[estadoActual][columna];
			token.setToken(AS.execute(buffer, (char)asciiActual));
			boolean acomodarLinea = AS.acomodarLinea();
			estadoActual = estadoSiguiente;
			
			if(token.getToken() > 0) {
				//if (buffer.length() > 0)
				token.setLexema(buffer.toString());
				token.setLinea(nroLinea);
				hayToken = true;
				buffer.delete(0, buffer.length());	
			}
			else if (asciiAnterior == -1) {	
				token.setToken(0); 
				return token;
			}									//TRATAMIENTO DE ERRORES LÉXICOS
			else if (token.getToken() == -2){ System.out.println("Error: caracter inválido "+asciiActual+ " en la linea " + nroLinea); }
				else if (token.getToken() == -3){ System.out.println("Warning en la linea "+nroLinea+": identificador supera la longitud máxima"); }
					else if (token.getToken() == -4){ System.out.println("Error en la linea "+nroLinea+": constante fuera del rango permitido"); }
		}
		while (!hayToken);
		//System.out.println(t);
		return token;		
	}
	
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException {
		
		Compilador c = new Compilador();
		
		CrearSalida.crearTxtSalida(c);
		
		/*
		// Obtengo la ruta del archivo de los argumentos de programa
		if(args.length > 0) {
			try {
				String ruta = args[0];
				String strCurrentLine;   
				System.out.print("Hola " + args[0]);
				Archivo archivo = new Archivo();
				//Cargo el archivo para poder usarlo
				archivo.cargarArchivo(ruta);
				
				while ((strCurrentLine = archivo.getBufferLectura().readLine ()) != null) {   
					System.out.println (strCurrentLine);
				}				
			} catch (IOException e) {
				System.out.print("Hubo un error con el Archivo.");
			}
		}
		else
			System.out.print("No se pudo cargar Archivo.");
		*/
	}
}
