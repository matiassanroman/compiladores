
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
	
	//							   l    =	;	d	 _	eof
	//							   0    1   2    3   4   5
	int[][] matrizTEstados = { 	  {1,	2,  0,   0,	 0,  0}, 
			   				      {1,	0,  0,   1,  1,  0},
			   				      {0,	0,  0,   0,  0,  0} 
			   				 };
	//												 lmin					    =			                ;                          d                  		  _                           eof
	AccionSemantica[][] matrizASemanticas = { {as1_agregar_buffer,	    as1_agregar_buffer,		    as6_end_simbolo,            as11_no_accion,				as11_no_accion,			    as11_no_accion}, 
							  				  {as1_agregar_buffer,	    as2_verificar_longitud_id,	as2_verificar_longitud_id,	as1_agregar_buffer,	   		as1_agregar_buffer,		    as11_no_accion},
											  {as7_end_simbolo_simple,	as11_no_accion,             as7_end_simbolo_simple,     as7_end_simbolo_simple,		as7_end_simbolo_simple,		as11_no_accion}
							  				};
	
	
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
				//System.out.println("Entra??");
				asciiActual = asciiAnterior;
				acomodarLinea = false;
			}
			else {
				//System.out.println("Anterior " + asciiAnterior);
				asciiActual = br.read();
				//System.out.println("Actual " + asciiActual);
				if(asciiActual == 13) { nroLinea++; }
			}
			
			asciiAnterior = asciiActual;
			System.out.println("asciiActual: " + asciiActual);
			int columna = diccionario.asciiToColumna(asciiActual);
			System.out.println("Fila: " + estadoActual + " Columna: " + columna);
			estadoSiguiente = matrizTEstados[estadoActual][columna];
			AccionSemantica AS = matrizASemanticas[estadoActual][columna];
			token.setToken(AS.execute(buffer, (char)asciiActual));
			acomodarLinea = AS.acomodarLinea();
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
		tablaToken.put("<=", 274);
		tablaToken.put(">=", 275);
		tablaToken.put("!=", 276);
		tablaToken.put("==", 277);
		
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
					System.out.println("Errores: " + errores.get(i));
				
				for (int i=0; i<reconocidos.size(); i++)
					System.out.println("Reconocidos: " + reconocidos.get(i));
				
			} catch (IOException e) {
				System.out.print("Hubo un error con el Archivo.");
			}
		}
		else
			System.out.print("Se produjo un error con los argumentos del programa.");
		
	}
}
