
// Clases central desde donde se invoca todo

package compilador;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import accionesSemanticas.*;

public class Compilador {
	
	
	// int cantidad de errores; tener en cuenta para etapas 3 y 4
	static StringBuffer buffer = new StringBuffer();
	public static void limpiarBuffer() { buffer.delete(0, buffer.length()); }
	private static int nroLinea= 1;
	static Diccionario diccionario = new Diccionario();
	private static boolean acomodarLinea= false; // acomodar linea y tomar la lectura anterior
	FileReader fr;
	static BufferedReader br;
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
/*9*/ {as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion          , as11_no_accion    , as11_no_accion    , as5_end_cadena, as11_no_accion    , as11_no_accion    , as11_no_accion    , as11_no_accion, as11_no_accion    , as11_no_accion, as1_agregar_buffer       , as11_no_accion    , as11_no_accion    , as1_agregar_buffer, as11_no_accion    , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion , as11_no_accion         , as11_no_accion , as11_no_accion , as11_no_accion},
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
	
	
	public void cargarArchivo(String origen) throws IOException{
		File archivo = new File (origen);
		fr = new FileReader(archivo);
	    br = new BufferedReader(fr);
	}
	
	// Metodo que retorna la tabla de simbolos
	public Hashtable<String,Simbolo> getTablaSimbolo(){
		return Compilador.tablaSimbolo;
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
				System.out.println("Entra??");
				asciiActual = asciiAnterior;
				acomodarLinea = false;
			}
			else {
				
				System.out.println("Anterior " + asciiAnterior);
				asciiActual = br.read();
				System.out.println("Actual " + asciiActual);
				if(asciiActual == 13) { nroLinea++; }
			}
			
		
			
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
			
			asciiAnterior = asciiActual;
			int columna = diccionario.asciiToColumna(asciiActual);
			System.out.println("Estado Actual: " + estadoActual);
			System.out.println("Columna: " + columna);
			estadoSiguiente = matrizTEstados[estadoActual][columna];
			AccionSemantica AS = matrizASemanticas[estadoActual][columna];
			token.setToken(AS.execute(buffer, (char)asciiActual));
			acomodarLinea = AS.acomodarLinea();
			estadoActual = estadoSiguiente;
		
		
		}
		while (!hayToken);
		//System.out.println(t);
		return token;		
	}
	
	
	public static void main(String[] args) throws IOException {
		
		tablaToken.put("CTE",257);
		tablaToken.put("ID",258);
		tablaToken.put("IF",259);
		tablaToken.put("THEN",260);
		tablaToken.put("ELSE",261);
		tablaToken.put("END_IF",262);
		tablaToken.put("OUT",263);
		tablaToken.put("FUNC",264);
		tablaToken.put("RETURN",265);
		tablaToken.put("FOR",266);
		tablaToken.put("INTEGER",267);
		tablaToken.put("FLOAT",268);
		tablaToken.put("PROC",269);
		tablaToken.put("NS",270);
		tablaToken.put("NA",271);
		tablaToken.put("CADENA",272);
		tablaToken.put("UP",273);
		tablaToken.put("DOWN",274);
		
		tablaToken.put("<=", 275);
		tablaToken.put(">=", 276);
		tablaToken.put("!=", 277);
		tablaToken.put("==", 278);
		
		Compilador c = new Compilador();
		ArrayList<String> errores = new ArrayList<String>();
		ArrayList<String> reconocidos = new ArrayList<String>();		
			
		// Obtengo la ruta del archivo de los argumentos de programa
		if(args.length > 0) {
			try {
				String ruta = args[0];
				c.cargarArchivo(ruta);
				Parser p = new Parser(c, errores);
				p.yyparse();
				errores = p.getErrores();
				reconocidos = p.getReconocidos();
				for (int i=0; i<errores.size(); i++)
					System.out.println(errores.get(i));
				
				for (int i=0; i<reconocidos.size(); i++)
					System.out.println(reconocidos.get(i));
				
			} catch (IOException e) {
				System.out.print("Hubo un error con el Archivo.");
			}
		}
		else
			System.out.print("Se produjo un error con los argumentos del programa.");
		
	}
}
