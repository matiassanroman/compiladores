
// Clases central desde donde se invoca todo

package compilador;
import java.io.*;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import accionesSemanticas.*;

public class Compilador {
	
	// int cantidad de errores; tener en cuenta para etapas 3 y 4
	static StringBuffer buffer = new StringBuffer();
	public static void limpiarBuffer() { buffer.delete(0, buffer.length()); }
	public static int nroLinea= 1;
	static Diccionario diccionario = new Diccionario();
	private static boolean acomodarLinea= false; // acomodar linea y tomar la lectura anterior
	FileReader fr;
	static BufferedReader br;
	protected static int asciiAnterior = 0;
	
	//No ponemos privado para evitar mas metodos y que se pueda acceder de cualquier lado. 
	static Hashtable<String,ArrayList<Simbolo>> tablaSimbolo = new Hashtable<String,ArrayList<Simbolo>>();
	static HashMap<String, Integer> tablaToken = new HashMap<String,Integer>();

	public static String ambito = "";
	public static int na = 0;
	public static boolean primero = true;
	public static int naa = 0;
	public static ArrayList<Integer> anidamientos = new ArrayList<Integer>(); 
	
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
	static AccionSemantica as12_error = new AS12_Error();

	//							  					lmin lmay  =  ;   d   _   %  /nl "    -   (  )  b  bb   +  *  /   ,  {  }  <   >   !   i   .   f  tab   eof  c  
	//							   					   0    1  2  3   4   5   6   7  8    9  10 11 12  13  14 15 16  17 18 19 20  21  22  23  24  25  26    27   28
	int[][] matrizTEstados = { 	  
/*                             Estado inicial      0*/{1,	3, 2, 0, 12,  0,  4,  0, 6,	  0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 9, 10, 11,  1, 18,  1,  0,   0,   0}, /* 0 */
/*                               Camino de id      1*/{1,	0, 0, 0,  1,  1,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  1,  0,  1,  0,	0,   0}, /* 1  Camino de id */
/*                           Camino de = o ==      2*/{0,	0, 0, 0,  0,  0,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  0,  0,  0,  0,	0,   0}, /* 2  Camino de = o == */
/*              Camino de palabras reservadas      3*/{0,	3, 0, 0,  0,  3,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  0,  0,  0,  0,   0,   0}, /* 3  Camino de palabras reservadas */
/*       Reconozco el primer % del comentario      4*/{0,   0, 0, 0,  0,  0,  5,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  0,  0,  0,  0,   0,   0}, /* 4  Reconozco el primer % del comentario */
/*      Reconozco el segundo % del comentario      5*/{5,   5, 5, 5,  5,  5,  5,  0, 5,   5,  5, 5,	5,	0,  5, 5, 5,  5, 5, 5, 5,  5,  5,  5,  5,  5,  5,   0,   0}, /* 5  Reconozco el segundo % del comentario */
/*               Reconozco cadenas multilinea      6*/{6,   6, 0, 0,  6,  0,  0,  0, 6,   7,  0, 0,	6,	6,  0, 0, 0,  0, 0, 0, 0,  0,  0,  6,  0,  6,  6,   0,   0}, /* 6  Reconozco cadenas multilinea */
/*               Reconozco cadenas multilinea      7*/{8,   8, 0, 0,  8,  0,  0,  6, 7,   7,  0, 0,	8,	8,  0, 0, 0,  0, 0, 0, 0,  0,  0,  8,  0,  8,  8,   0,   0}, /* 7  Reconozco cadenas multilinea */
/*               Reconozco cadenas multilinea      8*/{8,   8, 0, 0,  8,  0,  0,  0, 8,   7,  0, 0,	8,	8,  0, 0, 0,  0, 0, 0, 0,  0,  0,  8,  0,  8,  8,   0,   0}, /* 8  Reconozco cadenas multilinea */
/*                             Reconozco < <=      9*/{0,	0, 0, 0,  0,  0,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  0,  0,  0,  0,   0,   0}, /* 9  Reconozco < <= */
/*                            Reconozco > >=      10*/{0,	0, 0, 0,  0,  0,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  0,  0,  0,  0,   0,   0}, /* 10 Reconozco > >= */
/*                              Reconozco !=      11*/{0,	0, 0, 0,  0,  0,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  0,  0,  0,  0,   0,   0}, /* 11 Reconozco != */
/*                Reconozco constante entera      12*/{0,	0, 0, 0, 12, 13,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  0, 19,  0,  0,   0,   0}, /* 12 Reconozco constante entera */					
/*                Reconozco constante entera      13*/{0,	0, 0, 0,  0,  0,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0, 17,  0,  0,  0,   0,   0}, /* 13 Reconozco constante entera */
/*          Reconozco flotantes del tipo 10.      14*/{0,	0, 0, 0, 14,  0,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  0,  0, 15,  0,   0,   0}, /* 14 Reconozco flotantes del tipo 10. */
/*   Reconozco flotantes del tipo 10.10f+-10      15*/{0,	0, 0, 0,  0,  0,  0,  0, 0,  16,  0, 0,	0,	0, 16, 0, 0,  0, 0, 0, 0,  0,  0,  0,  0,  0,  0,   0,   0}, /* 15 Reconozco flotantes del tipo 10.10f+-10 */
/* Reconozco flotantes del tipo 10.10f+-10 y 0.50 16*/{0,	0, 0, 0, 16,  0,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  0,  0,  0,  0,   0,   0}, /* 16 Reconozco flotantes del tipo 10.10f+-10*/
/* Reconozco constante entera                     17*/{0,	0, 0, 0,  0,  0,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  0,  0,  0,  0,   0,   0}, /* 17 Reconozco constante entera */ 
/* Casos especiales de flotantes                  18*/{0,	0, 0, 0, 19,  0,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  0,  0,  0,  0,   0,   0}, /* 18 Reconozco flotantes del tipo .10 */
/* Casos especiales de flotantes                  19*/{0,	0, 0, 0, 19,  0,  0,  0, 0,   0,  0, 0,	0,	0,  0, 0, 0,  0, 0, 0, 0,  0,  0,  0,  0, 15,  0,   0,   0}, /* 19 Reconozco flotantes del tipo .10 */
							}; 

		//												 lmin					    lmay			           		=                         ;                  		d                       _			            %					  	/n			       		"	    				 -				  		     (				              )			            blanco				      bb   					 	   +   					  	   *    					 	 /   					    , 	  					{   					}		  		   		  <				 			>			   		     	!							  i					.			             f					       tab				      eof                    c
	AccionSemantica[][] matrizASemanticas =   { 
									/*  0 */  {as1_agregar_buffer     , as1_agregar_buffer		 , as1_agregar_buffer	    , as6_end_simbolo		   , as1_agregar_buffer	    , as6_end_simbolo		 , as1_agregar_buffer     , as11_no_accion	  	   , as1_agregar_buffer 		, as6_end_simbolo		 , as6_end_simbolo	        , as6_end_simbolo	       , as11_no_accion		   	  , as11_no_accion 		   , as6_end_simbolo   		  , as6_end_simbolo   		 , as6_end_simbolo   		, as6_end_simbolo          , as6_end_simbolo   		, as6_end_simbolo   	 , as1_agregar_buffer       , as1_agregar_buffer	   , as1_agregar_buffer	   	  , as1_agregar_buffer	  , as1_agregar_buffer     , as1_agregar_buffer	    , as11_no_accion	     , as11_no_accion			,as12_error}, /* 0  */
									/*  1 */  {as1_agregar_buffer     , as2_verificar_longitud_id, as2_verificar_longitud_id, as2_verificar_longitud_id, as1_agregar_buffer     , as1_agregar_buffer	 , as11_no_accion	      , as2_verificar_longitud_id, as11_no_accion 		, as2_verificar_longitud_id, as2_verificar_longitud_id, as2_verificar_longitud_id, as2_verificar_longitud_id, as11_no_accion 		   , as2_verificar_longitud_id, as2_verificar_longitud_id, as2_verificar_longitud_id, as2_verificar_longitud_id, as11_no_accion    		, as11_no_accion    	 , as2_verificar_longitud_id, as2_verificar_longitud_id, as2_verificar_longitud_id, as1_agregar_buffer	  , as11_no_accion         , as1_agregar_buffer	    , as11_no_accion , as11_no_accion	        ,as12_error}, /* 1  */
									/*  2 */  {as7_end_simbolo_simple , as7_end_simbolo_simple   , as8_end_simbolo_complejo , as7_end_simbolo_simple   , as7_end_simbolo_simple , as7_end_simbolo_simple , as11_no_accion	      , as11_no_accion    	   , as11_no_accion 		, as7_end_simbolo_simple , as11_no_accion 	        , as11_no_accion	       , as7_end_simbolo_simple   , as11_no_accion 		   , as11_no_accion    		  , as11_no_accion    		 , as11_no_accion    		, as11_no_accion           , as11_no_accion    		, as11_no_accion    	 , as11_no_accion           , as11_no_accion           , as11_no_accion		   	  , as7_end_simbolo_simple, as7_end_simbolo_simple , as7_end_simbolo_simple , as11_no_accion		 , as11_no_accion			,as12_error}, /* 2  */
									/*  3 */  {as3_devolver_pr        , as1_agregar_buffer       , as3_devolver_pr		    , as3_devolver_pr		   , as3_devolver_pr	    , as1_agregar_buffer	 , as3_devolver_pr	      , as3_devolver_pr   	   , as3_devolver_pr		, as3_devolver_pr		 , as3_devolver_pr	        , as3_devolver_pr	       , as3_devolver_pr		  , as3_devolver_pr		   , as3_devolver_pr   	  	  , as3_devolver_pr   		 , as3_devolver_pr   		, as3_devolver_pr          , as3_devolver_pr   		, as3_devolver_pr   	 , as11_no_accion		    , as11_no_accion           , as11_no_accion           , as11_no_accion		  , as11_no_accion         , as3_devolver_pr	    , as3_devolver_pr		 , as3_devolver_pr			,as12_error}, /* 3  */
									/*  4 */  {as4_end_comentario  	  , as4_end_comentario		 , as4_end_comentario		, as4_end_comentario	   , as4_end_comentario	 	, as4_end_comentario	 , as1_agregar_buffer     , as4_end_comentario    	       , as4_end_comentario 		    , as4_end_comentario		     , as4_end_comentario	            , as4_end_comentario		       , as4_end_comentario		   	      , as4_end_comentario 		   	   , as4_end_comentario    		  	  , as4_end_comentario    	         , as4_end_comentario    		    , as4_end_comentario               , as4_end_comentario    		    , as4_end_comentario    	     , as4_end_comentario		        , as4_end_comentario               , as4_end_comentario  		      , as4_end_comentario		      , as4_end_comentario             , as4_end_comentario		        , as4_end_comentario		     , as4_end_comentario		,as4_end_comentario}, /* 4  */
									/*  5 */  {as1_agregar_buffer     , as1_agregar_buffer       , as1_agregar_buffer	    , as1_agregar_buffer	   , as1_agregar_buffer	    , as1_agregar_buffer	 , as1_agregar_buffer     , as4_end_comentario     , as1_agregar_buffer     , as1_agregar_buffer	 , as1_agregar_buffer	    , as1_agregar_buffer	   , as1_agregar_buffer		  , as4_end_comentario 	   , as1_agregar_buffer    	  , as1_agregar_buffer    	 , as1_agregar_buffer       , as1_agregar_buffer       , as1_agregar_buffer    	, as1_agregar_buffer     , as1_agregar_buffer		, as1_agregar_buffer       , as1_agregar_buffer		  , as1_agregar_buffer	  , as1_agregar_buffer 	   , as1_agregar_buffer	    , as1_agregar_buffer	 , as4_end_comentario		,as12_error}, /* 5  */
									/*  6 */  {as1_agregar_buffer     , as1_agregar_buffer		 , as5_end_cadena  			, as5_end_cadena	   	   , as1_agregar_buffer	    , as5_end_cadena	 	 , as5_end_cadena         , as5_end_cadena     	   , as1_agregar_buffer     , as1_agregar_buffer     , as5_end_cadena           , as5_end_cadena	       , as1_agregar_buffer	   	  , as11_no_accion 		   , as5_end_cadena		  	  , as5_end_cadena		     , as5_end_cadena		    , as5_end_cadena           , as5_end_cadena		    , as5_end_cadena	     , as5_end_cadena	        , as5_end_cadena           , as5_end_cadena	   	      , as1_agregar_buffer	  , as6_end_simbolo        , as1_agregar_buffer	    , as1_agregar_buffer	 , as5_end_cadena			,as12_error}, /* 6  */
									/*  7 */  {as1_agregar_buffer     , as1_agregar_buffer		 , as5_end_cadena  			, as5_end_cadena	   	   , as1_agregar_buffer	    , as5_end_cadena	 	 , as5_end_cadena         , as11_no_accion     	   , as1_agregar_buffer     , as1_agregar_buffer     , as5_end_cadena           , as5_end_cadena	       , as1_agregar_buffer	   	  , as5_end_cadena 		   , as5_end_cadena		  	  , as5_end_cadena		     , as5_end_cadena		    , as5_end_cadena           , as5_end_cadena		    , as5_end_cadena	     , as5_end_cadena	        , as5_end_cadena           , as5_end_cadena	   	      , as1_agregar_buffer	  , as6_end_simbolo        , as1_agregar_buffer	    , as1_agregar_buffer	 , as5_end_cadena			,as12_error}, /* 7  */
									/*  8 */  {as1_agregar_buffer     , as1_agregar_buffer		 , as5_end_cadena  			, as5_end_cadena	   	   , as1_agregar_buffer	    , as5_end_cadena	 	 , as5_end_cadena         , as5_end_cadena         	   , as1_agregar_buffer     , as1_agregar_buffer     , as5_end_cadena           , as5_end_cadena	       , as1_agregar_buffer	   	  , as5_end_cadena 		   , as5_end_cadena		  	  , as5_end_cadena		     , as5_end_cadena		    , as5_end_cadena           , as5_end_cadena		    , as5_end_cadena	     , as5_end_cadena	        , as5_end_cadena           , as5_end_cadena	   	      , as1_agregar_buffer	  , as6_end_simbolo        , as1_agregar_buffer	    , as1_agregar_buffer	 , as5_end_cadena			,as12_error}, /* 8  */  
				   				    /*  9 */  {as7_end_simbolo_simple , as7_end_simbolo_simple   , as8_end_simbolo_complejo , as7_end_simbolo_simple   , as7_end_simbolo_simple , as7_end_simbolo_simple , as11_no_accion	      , as11_no_accion         , as11_no_accion 		, as11_no_accion		 , as11_no_accion	        , as11_no_accion		   , as7_end_simbolo_simple   , as11_no_accion 		   , as11_no_accion    		  , as11_no_accion    		 , as11_no_accion    		, as11_no_accion           , as11_no_accion    		, as11_no_accion    	 , as11_no_accion   		, as11_no_accion           , as1_agregar_buffer  	  , as7_end_simbolo_simple, as11_no_accion         , as7_end_simbolo_simple , as11_no_accion		 , as11_no_accion			,as12_error}, /* 9  */
				   				    /* 10 */  {as7_end_simbolo_simple , as7_end_simbolo_simple   , as8_end_simbolo_complejo , as7_end_simbolo_simple   , as7_end_simbolo_simple , as7_end_simbolo_simple , as11_no_accion	      , as11_no_accion         , as11_no_accion 		, as11_no_accion		 , as11_no_accion	        , as11_no_accion		   , as7_end_simbolo_simple   , as11_no_accion 		   , as11_no_accion    		  , as11_no_accion    		 , as11_no_accion    		, as11_no_accion           , as11_no_accion    		, as11_no_accion    	 , as11_no_accion		    , as11_no_accion           , as11_no_accion		   	  , as7_end_simbolo_simple, as11_no_accion         , as7_end_simbolo_simple , as11_no_accion		 , as11_no_accion			,as12_error}, /* 10 */
				   				    /* 11 */  {as11_no_accion         , as11_no_accion			 , as8_end_simbolo_complejo , as11_no_accion		   , as11_no_accion		    , as11_no_accion		 , as1_agregar_buffer     , as11_no_accion    	   , as11_no_accion 		, as11_no_accion		 , as11_no_accion	        , as11_no_accion		   , as11_no_accion		      , as11_no_accion 		   , as11_no_accion    		  , as11_no_accion    	     , as11_no_accion    		, as11_no_accion           , as11_no_accion    		, as11_no_accion    	 , as11_no_accion		    , as11_no_accion           , as11_no_accion  		  , as11_no_accion		  , as11_no_accion         , as11_no_accion		    , as11_no_accion		 , as11_no_accion			,as12_error}, /* 11 */
				   				    /* 12 */  {as9_verificar_rango_cte, as9_verificar_rango_cte  , as9_verificar_rango_cte  , as9_verificar_rango_cte  , as1_agregar_buffer     , as1_agregar_buffer     , as11_no_accion	      , as9_verificar_rango_cte, as11_no_accion 		, as9_verificar_rango_cte, as11_no_accion	        , as11_no_accion		   , as9_verificar_rango_cte  , as9_verificar_rango_cte, as9_verificar_rango_cte  , as9_verificar_rango_cte	 , as9_verificar_rango_cte	, as11_no_accion           , as11_no_accion    		, as11_no_accion    	 , as9_verificar_rango_cte  , as9_verificar_rango_cte  , as9_verificar_rango_cte  , as9_verificar_rango_cte , as1_agregar_buffer   , as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte	,as12_error}, /* 12 */
				   				   /* 13 */   {as9_verificar_rango_cte, as9_verificar_rango_cte	 , as9_verificar_rango_cte  , as9_verificar_rango_cte  , as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte	, as9_verificar_rango_cte  , as9_verificar_rango_cte  , as9_verificar_rango_cte, as9_verificar_rango_cte  , as9_verificar_rango_cte  , as9_verificar_rango_cte 	, as9_verificar_rango_cte  , as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte	, as9_verificar_rango_cte  , as9_verificar_rango_cte  , as1_agregar_buffer    , as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte	,as12_error}, /* 13 */
				   				    /* 14 */  {as10_verificar_float	  , as10_verificar_float	 , as10_verificar_float     , as10_verificar_float     , as1_agregar_buffer     , as10_verificar_float   , as10_verificar_float	  , as10_verificar_float   , as10_verificar_float   , as10_verificar_float   , as10_verificar_float	    , as10_verificar_float     , as10_verificar_float     , as10_verificar_float   , as10_verificar_float     , as10_verificar_float     , as10_verificar_float 	, as10_verificar_float     , as10_verificar_float   , as10_verificar_float   , as10_verificar_float	 	, as10_verificar_float	   , as10_verificar_float     , as10_verificar_float  , as1_agregar_buffer     , as1_agregar_buffer     , as10_verificar_float   , as10_verificar_float		,as12_error}, /* 14 */
				   				    /* 15 */  {as10_verificar_float	      	  , as10_verificar_float	         	 , as10_verificar_float           , as10_verificar_float           , as10_verificar_float   	    , as10_verificar_float   		 , as10_verificar_float	      , as10_verificar_float         , as10_verificar_float         , as1_agregar_buffer     , as10_verificar_float	        , as10_verificar_float           , as10_verificar_float           , as10_verificar_float         , as1_agregar_buffer       , as10_verificar_float     		 	 , as10_verificar_float 			, as10_verificar_float     	   , as10_verificar_float   		, as10_verificar_float    	 , as10_verificar_float	 		, as10_verificar_float	       , as10_verificar_float               , as10_verificar_float   	      , as10_verificar_float         , as10_verificar_float    	    , as10_verificar_float		 , as10_verificar_float				,as12_error}, /* 15 */
				   				    /* 16 */  {as10_verificar_float	  , as10_verificar_float	 , as11_no_accion     		, as10_verificar_float     , as1_agregar_buffer   	, as11_no_accion   		 , as11_no_accion	      , as11_no_accion         , as11_no_accion    		, as10_verificar_float   , as10_verificar_float	    , as10_verificar_float           , as10_verificar_float     , as10_verificar_float   , as10_verificar_float     , as10_verificar_float     , as10_verificar_float      , as10_verificar_float     , as11_no_accion   , as11_no_accion   , as11_no_accion	 	, as11_no_accion	   , as11_no_accion     , as11_no_accion  , as11_no_accion   , as11_no_accion   , as11_no_accion   , as11_no_accion		,as12_error}, /* 16 */
				   				    /* 17 */  {as9_verificar_rango_cte, as9_verificar_rango_cte	 , as9_verificar_rango_cte  , as9_verificar_rango_cte  , as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte	, as9_verificar_rango_cte  , as9_verificar_rango_cte  , as9_verificar_rango_cte, as9_verificar_rango_cte  , as9_verificar_rango_cte  , as9_verificar_rango_cte 	, as9_verificar_rango_cte  , as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte	, as9_verificar_rango_cte  , as9_verificar_rango_cte  , as9_verificar_rango_cte        , as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte, as9_verificar_rango_cte	,as12_error}, /* 17 */
				   				    /* 18 */  {as10_verificar_float   , as10_verificar_float	 , as10_verificar_float     , as10_verificar_float	   , as1_agregar_buffer		, as10_verificar_float	 , as10_verificar_float	  , as10_verificar_float   , as10_verificar_float   , as10_verificar_float   , as10_verificar_float	    , as10_verificar_float     , as10_verificar_float	  , as10_verificar_float   , as10_verificar_float  	  , as10_verificar_float  	 , as10_verificar_float 	, as10_verificar_float     , as10_verificar_float	, as10_verificar_float 	 , as10_verificar_float	 	, as10_verificar_float	   , as10_verificar_float  	  , as10_verificar_float  , as10_verificar_float   , as10_verificar_float   , as10_verificar_float   , as10_verificar_float		,as12_error}, /* 18 */
				   				    /* 19 */  {as10_verificar_float   , as10_verificar_float	 , as10_verificar_float           	, as10_verificar_float	   , as1_agregar_buffer		, as10_verificar_float        	 , as10_verificar_float	  		  , as10_verificar_float   		   , as10_verificar_float				, as10_verificar_float	 		 , as10_verificar_float				, as10_verificar_float     		   , as10_verificar_float 	  		  , as10_verificar_float   		   , as10_verificar_float     		  , as10_verificar_float  	 		 , as10_verificar_float 	  	 		, as10_verificar_float     		   , as10_verificar_float				, as10_verificar_float   			 , as10_verificar_float	 			, as10_verificar_float	  		   , as10_verificar_float  	  		  , as10_verificar_float  		  , as10_verificar_float   		   , as1_agregar_buffer	    , as10_verificar_float   			 , as10_verificar_float				,as12_error}, /* 19 */
	
	}; 
	
	//ver palabra reservada devolver pr esta en el + - *..
	
	public void cargarArchivo(String origen) throws IOException{
		File archivo = new File (origen);
		fr = new FileReader(archivo);
	    br = new BufferedReader(fr);
	}
	
	// Metodo que retorna la tabla de simbolos
	public Hashtable<String,ArrayList<Simbolo>> getTablaSimbolo(){
		return Compilador.tablaSimbolo;
	}
	
	// Metodo que sirve para pedir tokens, EXPLICACION A COMPLETAR
	public Token getToken() throws IOException {
		Token token = new Token();
		
		//Si en caracter que se proces� antes fue de codigo -1, se llegaal fin del archivo
		if(asciiAnterior == -1){    	
			token.setToken(0); 
			return token;
		}		
		
		// Definicion de algunas variables usadas
		int estadoSiguiente = 0;
		int estadoActual = 0;
		boolean hayToken = false;
		int asciiActual;
		
		// con el do-while hacemos que siempre se ejecute al menos 
		// una vez el bloque de codigo del do, luego se contiuara
		// ciclando hasta que no haya mas tokens
		
		do{	
			// Si una accion semantica X, luego de operar requiere que se acomode la
			// linea, se debera retroceder al caracter anterior 
			if (acomodarLinea){
				asciiActual = asciiAnterior;
				acomodarLinea = false;
			}
			// Si no hubo que acomodar la linea, se lee el buffer de lectura
			// y si el ascci leido coresponde con el salto de linea se incrementa
			// en 1 el contador de lineas
			else {
				asciiActual = br.read();
				if(asciiActual == 13){ nroLinea++; }
			}
			
			// Avanzo el caracter ascci, obtengo la columna de la matriz de tracision de estados
			// desde un mapeo que hicimos en una clase llamada diccionario, actualizamos el estado
			// siguiente, disparamos la accion semantica asociada a esa transicion, se la accion semantica
			// obtenemos un booleano que nos indica si en el siguiente ciclo se debe acomodar linea o no
			// y por ultimo actualizo el estado actual, siendo este el estado siguiente
			asciiAnterior = asciiActual;
			int columna = diccionario.asciiToColumna(asciiActual);
			//System.out.println("Fila: " + estadoActual + " Columna: " + columna + " Caract: " + (char)asciiActual);
			estadoSiguiente = matrizTEstados[estadoActual][columna];
			AccionSemantica AS = matrizASemanticas[estadoActual][columna];
			token.setToken(AS.execute(buffer, (char)asciiActual));
			acomodarLinea = AS.acomodarLinea();
			estadoActual = estadoSiguiente;
			
			//System.out.println("Toke: " + token.getToken());
			// Si el token el mayor a 0 significa que es un ascii valido, con lo cual a nuestra
			// estrucutra interna "Token" puede ser completada con todos los datos de ese lexema-token
			if(token.getToken() > 0) {
				token.setLexema(buffer.toString());
				token.setLinea(nroLinea);
				hayToken = true;
				buffer.delete(0, buffer.length());	
			}
			// Ssi es un ascii invalido (valor negativo), decimos que el token se vuelve 0
			// para poder seguir operando
			else if (asciiAnterior == -1) {	
				token.setToken(0); 
				return token;
			}			
		}
		while (!hayToken);
		return token; 
	}
		
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException {
		
		// Esta codiicacion fue generada desde yacc, el cual a cada token
		// que nosotros le declaramos le asocia un numero que hace referencia a este.
		tablaToken.put("ID",257);
		tablaToken.put("IF",258);
		tablaToken.put("THEN",259);
		tablaToken.put("ELSE",260);
		tablaToken.put("END_IF",261);
		tablaToken.put("OUT",262);
		tablaToken.put("FUNC",263);
		tablaToken.put("RETURN",264);
		tablaToken.put("FOR",265);
		tablaToken.put("INTEGER",266);
		tablaToken.put("FLOAT",267);
		tablaToken.put("PROC",268);
		tablaToken.put("NS",269);
		tablaToken.put("NA",270);
		tablaToken.put("CADENA",271);
		tablaToken.put("UP",272);
		tablaToken.put("DOWN",273);
		tablaToken.put("CTE",274);
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
					System.out.println("Reconocidos: " + reconocidos.get(i));
		

				if (errores.size() ==  0) {
					CrearSalida.crearTxtSalida(c);
					PolacaInversa polaca = Parser.polaca;
					System.out.println(polaca.toString());
					GeneradorAssembler generador = new GeneradorAssembler(tablaSimbolo,polaca);
					generador.generarAssembler(polaca);
					CrearAssembler.crearTxtSalida(generador.toString());
				}
					
					
			} catch (IOException e) {
				System.out.print("Hubo un error con el Archivo.");
			}
		}
		else
			System.out.print("Se produjo un error con los argumentos del programa.");
		
	}
}
