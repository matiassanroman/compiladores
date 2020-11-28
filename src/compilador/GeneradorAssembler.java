package compilador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/*  Para las operaciones entre datos de tipo entero se deber� generar c�digo que utilice los registros del
	procesador (EAX, EBX, ECX Y EDX o AX, BX, CX y DX), y seguimiento de registros.
	Para las operaciones entre datos de punto flotante se deber� utilizar el co-procesador 80X87, y el
	mecanismo para generar c�digo ser� el de variables auxiliares.
 ***** Controles en tiempo de ejecucion *****   
a) Division por cero:
   El codigo Assembler debero chequear que el divisor sea diferente de cero antes de efectuar una
   division. Este chequeo debera efectuarse para los dos tipos de datos asignados al grupo.
b) Overflow en sumas:
   El codigo Assembler debera  controlar el resultado de la operaci�n indicada, para los dos tipos de
   datos asignados al grupo. Si el mismo excede el rango del tipo del resultado, debero emitir un
   mensaje de error y terminar.
*/
public class GeneradorAssembler {
	// agregar una variable archivo que es donde se va 
	// guardar el assembler
	
	private String assembler;
	private Hashtable<String,ArrayList<Simbolo>> tablaSimbolo;
	
	Registros registro = new Registros();
	
	private ArrayList<String> operadoresBinarios = new ArrayList<String>(Arrays.asList("+","-","*","/","<","<=",">",">=","==","!=","="));
	private ArrayList<String> operadoresUnarios = new ArrayList<String>(Arrays.asList("OUT","BF","BI","CALL"));
	private String PROC = "PROC";
	
	private Stack<String> pila;
	
	public static int numeroVar = 1;
	public static String varAux = "@aux";
	
	public static String saltoDeLinea = "\r\n";
	
	///////////////////////////////////////////////////////////////////////////////
	/////////// ESTRUCTURA DEL ARCHIVO CON EL ASEMBLER ///////////////////////////
	
	public static String encabezado = 
			".386\r\n" + 
			".model flat, stdcall\r\n" + 
			"option casemap :none\r\n" + 
			"include \\masm32\\include\\windows.inc\r\n" + 
			"include \\masm32\\include\\kernel32.inc\r\n" + 
			"include \\masm32\\include\\user32.inc\r\n" + 
			"includelib \\masm32\\lib\\kernel32.lib\r\n" + 
			"includelib \\masm32\\lib\\user32.lib\r\n" +
			".data\r\n" + 
			"mensaje db \"Mensaje por pantalla\", 0" + saltoDeLinea;
	private String data = "";
	private String code = ".code" + saltoDeLinea;
	public static String inicioMainAssembler = "main:" + saltoDeLinea;
	private String main = "";
	public static String finMainAssembler = "fin: invoke ExitProcess, 0" + saltoDeLinea
										  +	"end main" + saltoDeLinea;	
	
	/////////// FIN ESTRUCTURA DEL ARCHIVO CON EL ASEMBLER ////////////////////////
	///////////////////////////////////////////////////////////////////////////////


	///////////////////////////////////////////////////////////////////////////////
	///////////////// PLANTILLAS DE OPERACIONES ENTRE INTEGER /////////////////////
	public static String plantillaOperacion = "MOV XX, OP1" + saltoDeLinea 
			  + "OP XX, OP2" + saltoDeLinea;

	public static String plantillaAsignacion = "MOV XX, OP1" + saltoDeLinea;
	public static String extender16a32Bits   = "CWDE" + saltoDeLinea;

	public static String plantillaSuma = "MOV XX, OP1" + saltoDeLinea 

			 						   + "ADD XX, OP2" + saltoDeLinea
			 						   + "MOV VAR-REG, XX" + saltoDeLinea;
	
	public static String plantillaResta = "MOV XX, OP1" + saltoDeLinea 
			   							+ "SUB XX, OP2" + saltoDeLinea
			   							+ "MOV VAR-REG, XX" + saltoDeLinea;
	
	public static String plantillaMultiplicacion = "MOV XX, OP1" + saltoDeLinea 
												 + "MUL XX, OP2" + saltoDeLinea
												 + "MOV VAR-REG, XX" + saltoDeLinea;
	
	public static String plantillaDivision = "MOV XX, OP1" + saltoDeLinea 
			 							   + "DIV XX, OP2" + saltoDeLinea
			 							   + "MOV VAR-REG, XX" + saltoDeLinea;
	
	public static String plantillaCompIgual =          "JNE Llabel" + saltoDeLinea;
	public static String plantillaCompDistinto =       "JE Llabel"  + saltoDeLinea;
	public static String plantillaCompMayor =          "JLE Llabel" + saltoDeLinea;
	public static String plantillaCompMenor =          "JGE Llabel" + saltoDeLinea;
	public static String plantillaCompMayorIgual =     "JL Llabel"  + saltoDeLinea;
	public static String plantillaCompMenorIgual =     "JG Llabel"  + saltoDeLinea;
	public static String plantillaSaltoIncondicional = "JMP Llabel" + saltoDeLinea;
	public static String plantillaComparacion = "CMP RA, RB" + saltoDeLinea;
	
	public static String plantillaMostrarPorPantalla = "invoke MessageBox, NULL, addr VAR, addr mensaje, MB_OK" + saltoDeLinea; //titulo de la ventana
	public static String plantillaMostrarPorPantallaData = "VAR db \"CADENA\", 0" + saltoDeLinea; // texto dentro la ventana de mesaje
	
	public static String plantillaEtiqueta = "ETIQUETA:" + saltoDeLinea;
	public static String plantillaCall = "call F" + saltoDeLinea;
	public static String plantillaAgregarVarINTEGER = "VAR dw ?" + saltoDeLinea;
	public static String plantillaAgregarVarFLOAT   = "VAR dd ?" + saltoDeLinea;
	
	public static String saltoPorOverflow = "JO fin" + saltoDeLinea;
	
	public static String plantillaCargaCompFLOAT = "carga op1" + saltoDeLinea
								                 + "compa op2" + saltoDeLinea;
	
	////////////////// FIN PLANTILLAS DE OPERACIONES PARA INTEGER//////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	///////////////////////////////////////////////////////////////////////////////
	//////////////////FIN PLANTILLAS DE OPERACIONES PARA FLOAT/////////////////////
	
	// FLD para FLOAT de entrada --- FILD para un INTEGER de entrada ----- se copian a la pila del coprocesador
	// FISTP saca el tope de la pila del coprocesador y lo pasa aun registro, pasa a ser entero
	// FSTP saca el tope de la pila del coprocesador y lo guarda como real 
	// operaciones del Co-procesador matematico
	//
	//  FLOAT               INTEGER
 	//
	//	FLD mem				FILD mem   cargar a la pila
	//	FSTP mem			FISTP mem  sacar de la pila
	//
	//  FADD mem			FIADD mem
	//  FSUB mem			FISUB mem
	//  FMUL mem			FIMUL mem
	//  FDIV mem			FIDIV mem
	//
	//  FCOMP mem			FICOMP mem
	public static String plantillaOperacionFloat = "OpPila REG1" + saltoDeLinea        // Agrega REG a la pila de coprocesador
												 + "OpArit REG2" + saltoDeLinea       // Realiza la operacion entre el tope de de l apila de coprocesador y REG
												 + "FSTP resul" + saltoDeLinea;
	
	public static String CompIgualFLOAT =          "JNE Llabel" + saltoDeLinea;
	public static String CompDistintoFLOAT =       "JE Llabel"  + saltoDeLinea;
	public static String CompMayorFLOAT =          "JBE Llabel" + saltoDeLinea;
	public static String CompMenorFLOAT =          "JAE Llabel" + saltoDeLinea;
	public static String CompMayorIgualFLOAT =     "JB Llabel"  + saltoDeLinea;
	public static String CompMenorIgualFLOAT =     "JA Llabel"  + saltoDeLinea;
	
	public static String plantillaComparacionFloat = "FSTSW aux"   + saltoDeLinea
									 			   + "MOV AX, aux" + saltoDeLinea
									 			   + "SAHF"        + saltoDeLinea;
	
	//////////////////FIN PLANTILLAS DE OPERACIONES PARA FLOAT/////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	private String generarVarAux() {
		String var = varAux + Integer.toString(numeroVar) ;
		numeroVar += 1;
		return var;
	}
	
	public GeneradorAssembler(Hashtable<String,ArrayList<Simbolo>> tablaSimbolo, PolacaInversa polaca) {
		this.assembler = "";
		this.pila = new Stack<String>(); 
		this.tablaSimbolo = tablaSimbolo;
	}
	
	private String generarCall(String nombreProc){
		String invocacion = plantillaCall.replace("F", nombreProc);
		return invocacion;
	}
	
	private String generarMensajePorPantalla(String cadenaAMostrar){
		cadenaAMostrar = cadenaAMostrar.replace("\"", "");
		cadenaAMostrar = "_"+cadenaAMostrar;
		cadenaAMostrar = cadenaAMostrar.replace(" ", "_");
		String codigo = plantillaMostrarPorPantalla.replace("VAR", cadenaAMostrar);	
		return codigo; 
	}

	public String generarSaltosInteger(String comp, String regComp1, String regComp2){
		String bestial;
		bestial = plantillaComparacion;
		if (comp.equals("<"))  bestial = bestial + plantillaCompMenor;
		else if (comp.equals(">"))  bestial = bestial + plantillaCompMayor;
			else if (comp.equals("<=")) bestial = bestial + plantillaCompMenorIgual;
				else if (comp.equals(">=")) bestial = bestial + plantillaCompMayorIgual;
					else if (comp.equals("==")) bestial = bestial + plantillaCompIgual;
						else if (comp.equals("!=")) bestial = bestial + plantillaCompDistinto;
		bestial = bestial.replace("RA", regComp1);
		bestial = bestial.replace("RB", regComp2);
		return bestial;
	}
	
	public String generarBI(String pos) {
		String bestial = plantillaSaltoIncondicional;
		bestial = bestial.replace("label", pos);
		return bestial;
	}
	
	public String generarInvocacion(String label){
		String abominacion = plantillaEtiqueta;
		abominacion = abominacion.replace("ETIQUETA", label.replace("PROC ", ""));
		return abominacion;
	}
		
	public void generarData(){
		// volcar toda la tabla de simbolos 
		// a la variable data que luego se agregara a la salida final
		// revisar formato
		
		Set<String> keys = this.tablaSimbolo.keySet();
	    Iterator<String> itr = keys.iterator();
	    String str;
	    
	    while (itr.hasNext()) { 
	       str = itr.next();
	       ArrayList<Simbolo> aux =  eliminarRepetidos(tablaSimbolo.get(str));
    	   for(int i=0; i<aux.size(); i++) {
    		   if (aux.get(i).getTipo().equals("Proc")) { 
    			   this.data = this.data + "_" +aux.get(i).getAmbito().replaceAll(":", "@") + " dw ?" + saltoDeLinea;
    		   } 
    		   else if(aux.get(i).getUso().equals("ID")) {
				   if(aux.get(i).getTipoParametro().equals("INTEGER"))
					   this.data = this.data + "_" +aux.get(i).getAmbito().replaceAll(":", "@") + " dw ?" + saltoDeLinea;
			   		if(aux.get(i).getTipoParametro().equals("FLOAT"))
			   			this.data = this.data + "_" +aux.get(i).getAmbito().replaceAll(":", "@") + " dd ?" + saltoDeLinea;
    		   }
    		   else if(aux.get(i).getUso().equals("CTE")) {
				   if(aux.get(i).getTipo().equals("int"))
					   this.data = this.data + "_" + aux.get(i).getValor().replaceAll(":", "@") + " dw " + aux.get(i).getValor() + saltoDeLinea;
				   if(aux.get(i).getTipo().equals("float"))
					   this.data = this.data + "_" + aux.get(i).getValor().replaceAll(":", "@") + " dd " + aux.get(i).getValor() + saltoDeLinea;
    		   }
    		   else if(aux.get(i).getUso().equals("CADENA")) {
    			   	   String cadenipi = aux.get(i).getValor();
    			   	   String cadenipi2 = cadenipi;
    			   	   cadenipi = cadenipi.replaceAll("\"", "");
    			   	   cadenipi = cadenipi.replaceAll(" ", "_");
    			   	   this.data = this.data + "_" + cadenipi + " db " + cadenipi2 + ", 0" + saltoDeLinea;
    				   //this.data = this.data + "_" + aux.get(i).getValor() + " DB " + aux.get(i).getValor() + " , 0" + saltoDeLinea;			   
    		   }	   
    		   else if(aux.get(i).getUso().equals("AUX")) {
				   if(aux.get(i).getTipo().equals("int"))
					   this.data = this.data + "@" + aux.get(i).getValor() + " dw " + aux.get(i).getValor() + saltoDeLinea;
				   if(aux.get(i).getTipo().equals("float"))
					   this.data = this.data + "@" + aux.get(i).getValor() + " dd " + aux.get(i).getValor() + saltoDeLinea;
    		   }
    	   } 
	    }		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////// METODO PRINCIPAL DE GENERACIKON DE ASSEMBLER /////////////////////////////////////////////	
	
	public void generarAssembler(PolacaInversa polaca) {
		// generar assembler a partir de la polaca
		ArrayList<Par> listaPolaca = polaca.getPolaca();
		
		for (int i = 0; i < listaPolaca.size(); i++) {
			String elemento = listaPolaca.get(i).getValor();
			if ( !this.operadoresUnarios.contains(elemento) && !this.operadoresBinarios.contains(elemento) && !elemento.contains(PROC) )  // Si son ids o ctes las apilo
				pila.add(elemento);
			if (operadoresUnarios.contains(elemento)) {    // Si es un operador unario
				if (elemento.equals("OUT")) {              // Si es OUT generar mensaje
						String cadena = pila.pop();
						this.main = this.main + generarMensajePorPantalla(cadena);
				}
				if (elemento.equals("CALL")){              // Si es CALL generar llamado
					String nProc = pila.pop();
					this.main = this.main + generarCall(nProc);
				}
				if (elemento.equals("BI")) {
					String salto = pila.pop();
					this.main = this.main + generarBI(salto);
				}
			}
			if (operadoresBinarios.contains(elemento)) {
				if (elemento.equals("+") || elemento.equals("-") || elemento.equals("*") || elemento.equals("/")) {
					this.generarAritmetica(elemento);
				}
				if (elemento.equals("=")) {
					this.getCodAsignacion();
				}
				if (elemento.equals("<") || elemento.equals("<=") || elemento.equals(">") || elemento.equals(">=") || elemento.equals("==") || elemento.equals("!=")) {
					String operando1 = pila.pop();  // Ver el assembler si es el op1
					String operando2 = pila.pop();  // Ver el assembler si es el op2
					i++; String salto = listaPolaca.get(i).getValor(); // Posicion  para generar el label
					i++; String BF = listaPolaca.get(i).getValor();    // BF que ya no es necesario y por eso se lo saca de la lista
					// generar comparacion
					//this.main = this.main + generarComparacion(salto, elem, caso, reg1, reg2)
					// generar salto
					this.main  =this.main + generarCall(salto);
				}
			}
			if (elemento.contains("L")) {
				this.main = this.main + generarInvocacion(elemento);
			}
			if (elemento.contains(PROC)) {                            // Si el PROC. agrego todo ese codigo con su nombre de pro en la seccion .code
				this.code = this.code + generarInvocacion(elemento);				  //AGREGA INVOCAION AL PROCEDIMIENTO DESDE DESDE EL MAIN
				i++;
				while (listaPolaca.get(i).getValor() != "RET") {  	  // mientras no llegue RET agrego todo ese codigo a la funcion en el code
					elemento = listaPolaca.get(i).getValor();
					if ( !this.operadoresUnarios.contains(elemento) && !this.operadoresBinarios.contains(elemento) && !elemento.contains(PROC) )  // Si son ids o ctes las apilo
						pila.add(elemento);
					if (operadoresUnarios.contains(elemento)) {    // Si es un operador unario
						if (elemento.equals("OUT")) {              // Si es OUT generar mensaje
								String cadena = pila.pop();
								this.code = this.code + generarMensajePorPantalla(cadena);
						}
						if (elemento.equals("CALL")){              // Si es CALL generar llamado
							String nProc = pila.pop();
							this.code = this.code + generarCall(nProc);
						}
					}
					//generar codigo para todos los tipos de instrucciones para dentro de ese procedimiento
					//agregandolas al code
					i++;
				}
				if (listaPolaca.get(i).getValor() == "RET")           // cuendo detecto RET lo agrego al code para cerar el procedimiento 
					this.code = this.code + "ret" + saltoDeLinea;
			}
		}
	}

	/////////////////////////////////// FIN METODO PRINCIPAL DE GENERACIKON DE ASSEMBLER /////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static ArrayList<Simbolo> eliminarRepetidos(ArrayList<Simbolo> l){
		ArrayList<Simbolo> aux = new ArrayList<Simbolo>();
	    boolean p = true;
	    
	    for(int i=0; i<l.size(); i++) {
	    	//Es una declaracion de ID
	    	if(l.get(i).isDeclarada()) 
	    		aux.add(l.get(i));	    
	    	//Es una CTE de NA o NS
	    	if(l.get(i).getUso().equals("CTE") && (l.get(i).getTipo().equals("NA_PROC")) || (l.get(i).getTipo().equals("NS_PROC")))
	    		aux.add(l.get(i));
	    }
	    
	    for(int i=0; i<l.size(); i++) {
	    	if(p) {
	    		if(l.get(i).getUso().equals("CTE") && !l.get(i).getTipo().equals("NA_PROC") && !l.get(i).getTipo().equals("NS_PROC")) {
	    			p = false;
	    			aux.add(l.get(i));
	    		}
	    		else if(l.get(i).getUso().equals("CADENA")){
	    			p = false;
	    			aux.add(l.get(i));
	    		}
	    	}
	    	else{
	    		boolean r = true;
	    		for(int j=0; j<aux.size(); j++) {
	    			if(aux.get(j).getUso().equals("CTE") && !aux.get(j).getTipo().equals("NA_PROC") && !aux.get(j).getTipo().equals("NS_PROC")) {
	    				if(aux.get(j).ambitoSinNombre().equals(l.get(i).ambitoSinNombre()))
	    					r = false;
	    			}
	    			else if(aux.get(j).getUso().equals("CADENA")){
	    				if(aux.get(j).ambitoSinNombre().equals(l.get(i).ambitoSinNombre()))
	    					r = false;
		    		}
	    		}
	    		if(r)
	    			aux.add(l.get(i));
	    	}		
	    }
	    return aux;
	}
	
	private void generarAritmetica(String operador) {
		
		// OPERANDO1 + OPERANDO2;
		//PILLA: OPERANDO1 OPERANDO2 (TOPE)
		String operando2 = pila.pop();
		String operando1 = pila.pop();
		
		//SON DOS NUMEROS O CTE
		if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				generarCodigoParaInteger(operando1, operando2, operador);
			}
			if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int contador = 0;
				this.main = this.main + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			//COVERSION
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 2;
				this.main = this.main + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 1;
				this.main = this.main + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
		}
		//SON UN NUMERO/CTE Y UN REG/AUX
		else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.registroInt(operando2)) {
				generarCodigoParaInteger(operando1, operando2, operador);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.registroFloat(operando2)) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int contador = 0;
				this.main = this.main + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			//COVERSION
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroInt(operando2)) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 2;
				this.main = this.main + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && registroFloat(operando2)) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 1;
				this.main = this.main + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
		}
		//SON UN REG/AUX Y UN NUMERO/CTE
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
			if(this.registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				generarCodigoParaInteger(operando1, operando2, operador);
			}
			else if(this.registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int contador = 0;
				this.main = this.main + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			//COVERSION
			else if(registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 2;
				this.main = this.main + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			else if( registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 1;
				this.main = this.main + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
		}
		//SON DOS REG/AUX
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
			if(this.registroInt(operando1) && this.registroInt(operando2)) {
				generarCodigoParaInteger(operando1, operando2, operador);
			}
			else if(this.registroFloat(operando1) && this.registroFloat(operando2)) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int contador = 0;
				this.main = this.main + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			//COVERSION
			else if(this.registroFloat(operando1) && this.registroInt(operando2)) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 2;
				this.main = this.main + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			else if(this.registroInt(operando1) && this.registroFloat(operando2)) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 1;
				this.main = this.main + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
		}
	}
	
	//SEGUIMIENTO DE REGISTROS
	private void generarCodigoParaInteger(String operando1, String operando2, String operador){
		
		String codigo = "";
		//SUMA
		if(operador.equals("+")) {
			//SITUACION 1 - (2 VARIABLES O CTES)
			if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "ADD");
				pila.push(registro.getRegistro(1, "INTEGER"));
				codigo = codigo + plantillaComparacion;
				codigo = codigo.replace("RA", operando1);
				codigo = codigo.replace("RB", operando2);
				codigo = codigo + saltoPorOverflow;
				this.main = this.main + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
					codigo = "ADD XX, OP2" + saltoDeLinea;
					codigo = codigo.replace("XX", operando1);
					codigo = codigo.replace("OP2", operando2);
					pila.push(operando1);
					codigo = codigo + plantillaComparacion;
					codigo = codigo.replace("RA", operando1);
					codigo = codigo.replace("RB", operando2);
					codigo = codigo + saltoPorOverflow;
					this.main = this.main + codigo;
			}
			//SITUACION 3 - (2 REGISTROS)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
				codigo = "ADD XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(operando2, 0);
				codigo = codigo + plantillaComparacion;
				codigo = codigo.replace("RA", operando1);
				codigo = codigo.replace("RB", operando2);
				codigo = codigo + saltoPorOverflow;
				this.main = this.main + codigo;
			}
			//SITUACION 4A - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				codigo = "ADD XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando2);
				codigo = codigo.replace("OP2", operando1);
				pila.push(operando2);
				codigo = codigo + plantillaComparacion;
				codigo = codigo.replace("RA", operando1);
				codigo = codigo.replace("RB", operando2);
				codigo = codigo + saltoPorOverflow;
				this.main = this.main + codigo;
			}
		}
		//MULTIPLICACION
		else if(operador.equals("*")) {
			//SITUACION 1 - (2 VARIABLES O CTES)
			if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "IMUL");
				pila.push(registro.getRegistro(1, "INTEGER"));	
				this.main = this.main + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
					codigo = "IMUL XX, OP2" + saltoDeLinea;
					codigo = codigo.replace("XX", operando1);
					codigo = codigo.replace("OP2", operando2);
					pila.push(operando1);
					this.main = this.main + codigo;
			}
			//SITUACION 3 - (2 REGISTROS)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
				codigo = "IMUL XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(operando2, 0);
				this.main = this.main + codigo;
			}
			//SITUACION 4A - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				codigo = "IMUL XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando2);
				codigo = codigo.replace("OP2", operando1);
				pila.push(operando2);
				this.main = this.main + codigo;
			}
		}
		//RESTA
		else if(operador.equals("-")) {
			//SITUACION 1 - (2 VARIABLES O CTES)
			if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "SUB");
				pila.push(registro.getRegistro(1, "INTEGER"));	
				this.main = this.main + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
					codigo = "SUB XX, OP2" + saltoDeLinea;
					codigo = codigo.replace("XX", operando1);
					codigo = codigo.replace("OP2", operando2);
					pila.push(operando1);
					this.main = this.main + codigo;
			}
			//SITUACION 3 - (2 REGISTROS)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
				codigo = "SUB XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(operando2, 0);
				this.main = this.main + codigo;
			}
			//SITUACION 4B - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "SUB");
				registro.ocuparRegistro(operando2, 0);
				pila.push(operando1);
				this.main = this.main + codigo;
			}
		}
		//DIVISION
		else if(operador.equals("/")) {
			//SITUACION 1 - (2 VARIABLES O CTES)
			if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "IDIV");
				pila.push(registro.getRegistro(1, "INTEGER"));	
				this.main = this.main + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
					codigo = "IDIV XX, OP2" + saltoDeLinea;
					codigo = codigo.replace("XX", operando1);
					codigo = codigo.replace("OP2", operando2);
					pila.push(operando1);
					this.main = this.main + codigo;
			}
			//SITUACION 3 - (2 REGISTROS)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
				codigo = "IDIV XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(operando2, 0);
				this.main = this.main + codigo;
			}
			//SITUACION 4B - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "IDIV");
				registro.ocuparRegistro(operando2, 0);
				pila.push(operando1);
				this.main = this.main + codigo;
			}
		}
	}
	
	private void generarAritmeticaProc(String operador) {
		
		// OPERANDO1 + OPERANDO2;
		//PILLA: OPERANDO1 OPERANDO2 (TOPE)
		String operando2 = pila.pop();
		String operando1 = pila.pop();
		
		//SON DOS NUMEROS O CTE
		if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				generarCodigoParaIntegerProc(operando1, operando2, operador);
			}
			if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int contador = 0;
				this.code = this.code + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			//COVERSION
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 2;
				this.code = this.code + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 1;
				this.code = this.code + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
		}
		//SON UN NUMERO/CTE Y UN REG/AUX
		else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.registroInt(operando2)) {
				generarCodigoParaIntegerProc(operando1, operando2, operador);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.registroFloat(operando2)) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int contador = 0;
				this.code = this.code + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			//COVERSION
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroInt(operando2)) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 2;
				this.code = this.code + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && registroFloat(operando2)) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 1;
				this.code = this.code + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
		}
		//SON UN REG/AUX Y UN NUMERO/CTE
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
			if(this.registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				generarCodigoParaIntegerProc(operando1, operando2, operador);
			}
			else if(this.registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int contador = 0;
				this.code = this.code + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			//COVERSION
			else if(registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 2;
				this.code = this.code + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			else if( registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 1;
				this.code = this.code + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
		}
		//SON DOS REG/AUX
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
			if(this.registroInt(operando1) && this.registroInt(operando2)) {
				generarCodigoParaIntegerProc(operando1, operando2, operador);
			}
			else if(this.registroFloat(operando1) && this.registroFloat(operando2)) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int contador = 0;
				this.code = this.code + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			//COVERSION
			else if(this.registroFloat(operando1) && this.registroInt(operando2)) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 2;
				this.code = this.code + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
			else if(this.registroInt(operando1) && this.registroFloat(operando2)) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",operador), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				int convertir = 1;
				this.code = this.code + "FUNCION ARIEEEEEEEEL";
				registro.ocuparRegistro(reg, 0);
			}
		}
	}
	
	//SEGUIMIENTO DE REGISTROS
	private void generarCodigoParaIntegerProc(String operando1, String operando2, String operador){
		
		String codigo = "";
		//SUMA
		if(operador.equals("+")) {
			//SITUACION 1 - (2 VARIABLES O CTES)
			if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "ADD");
				pila.push(registro.getRegistro(1, "INTEGER"));
				codigo = codigo + plantillaComparacion;
				codigo = codigo.replace("RA", operando1);
				codigo = codigo.replace("RB", operando2);
				codigo = codigo + saltoPorOverflow;
				this.code = this.code + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
					codigo = "ADD XX, OP2" + saltoDeLinea;
					codigo = codigo.replace("XX", operando1);
					codigo = codigo.replace("OP2", operando2);
					pila.push(operando1);
					codigo = codigo + plantillaComparacion;
					codigo = codigo.replace("RA", operando1);
					codigo = codigo.replace("RB", operando2);
					codigo = codigo + saltoPorOverflow;
					this.code = this.code + codigo;
			}
			//SITUACION 3 - (2 REGISTROS)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
				codigo = "ADD XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(operando2, 0);
				codigo = codigo + plantillaComparacion;
				codigo = codigo.replace("RA", operando1);
				codigo = codigo.replace("RB", operando2);
				codigo = codigo + saltoPorOverflow;
				this.code = this.code + codigo;
			}
			//SITUACION 4A - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				codigo = "ADD XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando2);
				codigo = codigo.replace("OP2", operando1);
				pila.push(operando2);
				codigo = codigo + plantillaComparacion;
				codigo = codigo.replace("RA", operando1);
				codigo = codigo.replace("RB", operando2);
				codigo = codigo + saltoPorOverflow;
				this.code = this.code + codigo;
			}
		}
		//MULTIPLICACION
		else if(operador.equals("*")) {
			//SITUACION 1 - (2 VARIABLES O CTES)
			if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "IMUL");
				pila.push(registro.getRegistro(1, "INTEGER"));	
				this.code = this.code + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
					codigo = "IMUL XX, OP2" + saltoDeLinea;
					codigo = codigo.replace("XX", operando1);
					codigo = codigo.replace("OP2", operando2);
					pila.push(operando1);
					this.code = this.code + codigo;
			}
			//SITUACION 3 - (2 REGISTROS)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
				codigo = "IMUL XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(operando2, 0);
				this.code = this.code + codigo;
			}
			//SITUACION 4A - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				codigo = "IMUL XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando2);
				codigo = codigo.replace("OP2", operando1);
				pila.push(operando2);
				this.code = this.code + codigo;
			}
		}
		//RESTA
		else if(operador.equals("-")) {
			//SITUACION 1 - (2 VARIABLES O CTES)
			if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "SUB");
				pila.push(registro.getRegistro(1, "INTEGER"));	
				this.code = this.code + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
					codigo = "SUB XX, OP2" + saltoDeLinea;
					codigo = codigo.replace("XX", operando1);
					codigo = codigo.replace("OP2", operando2);
					pila.push(operando1);
					this.code = this.code + codigo;
			}
			//SITUACION 3 - (2 REGISTROS)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
				codigo = "SUB XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(operando2, 0);
				this.code = this.code + codigo;
			}
			//SITUACION 4B - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "SUB");
				registro.ocuparRegistro(operando2, 0);
				pila.push(operando1);
				this.code = this.code + codigo;
			}
		}
		//DIVISION
		else if(operador.equals("/")) {
			//SITUACION 1 - (2 VARIABLES O CTES)
			if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "IDIV");
				pila.push(registro.getRegistro(1, "INTEGER"));	
				this.code = this.code + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
					codigo = "IDIV XX, OP2" + saltoDeLinea;
					codigo = codigo.replace("XX", operando1);
					codigo = codigo.replace("OP2", operando2);
					pila.push(operando1);
					this.code = this.code + codigo;
			}
			//SITUACION 3 - (2 REGISTROS)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
				codigo = "IDIV XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(operando2, 0);
				this.code = this.code + codigo;
			}
			//SITUACION 4B - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "IDIV");
				registro.ocuparRegistro(operando2, 0);
				pila.push(operando1);
				this.code = this.code + codigo;
			}
		}
	}
	
	private void getCodAsignacion(){
		
		// x = y => ope1 = x ; ope2 = y

		String operando2 = pila.pop();
		String operando1 = pila.pop();
		String codigo = "";
		String operador = "";
		
		//I(OPERANDO 2) = J (OPERANDO 1)
		
		// SITACION 1 - OPERANDO 1 (REG/AUX) Y OPERANDO 2 (VAR)
		if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null){
			// SITACION 1.1 - OPERANDO 1 (REG) Y OPERANDO 2 (VAR) SON INTEGER - VARIANTE DE REGISTROS
			if(registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				codigo = plantillaAsignacion;
				codigo = codigo.replace("MOV VAR-REG, XX", "");
				codigo = codigo.replace("XX", operando2);
				codigo = codigo.replace("OP1", operando1);
				registro.ocuparRegistro(operando1, 0);
				this.main = this.main + codigo;
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroFloat(operando2) ) {
				int convertir = 0;
				//FUNCION ARIEL CONVERTIR
			}
			//CONVERSION - OPERANDO 2 VAR(INTEGER) Y OPERANDO 1 AUX(FLOAT)
			else if(this.getSimbolo(operando2).getTipoParametro().equals("INTEGER") && registroFloat(operando1) ) {
				errorDeEjecucion("Se quiere asignar un FLOAT a un INTEGER");
			}
			//CONVERSION - OPERANDO 2 AUX(FLOAT) Y OPERANDO 1 VAR(INTEGER)
			else if(this.getSimbolo(operando2).getTipoParametro().equals("FLOAT") && registroInt(operando1) ) {
				int convertir = 1;
				//FUNCION ARIEL CONVERTIR
			}
		}
		// SITACION 2 - OPERANDO 1 (VAR) Y OPERANDO 2 (VAR)
		else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null){
			//SITUACION 2.2 OPERANDO 1 Y 2 SON VAR Y SON INTEGER - VARIANTE DE REGISTROS	
			if (this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaAsignacion;
				codigo = codigo.replace("MOV VAR-REG, XX", "");
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando2);
				codigo = codigo + plantillaAsignacion;
				codigo = codigo.replace("MOV VAR-REG, XX", "");
				codigo = codigo.replace("OP1", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("XX", operando1);
				registro.ocuparRegistro(registro.getRegistro(1, "INTEGER"), 0);
				this.main = this.main + codigo;
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT") ) {
				int convertir = 0;
				//FUNCION ARIEL CONVERTIR
			}
			//CONVERSION - OPERANDO 2 VAR(INTEGER) Y OPERANDO 1 VAR(FLOAT)
			else if(this.getSimbolo(operando2).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") ) {
				errorDeEjecucion("Se quiere asignar un FLOAT a un INTEGER");
			}
			//CONVERSION - OPERANDO 1 VAR(INTEGER) Y OPERANDO 2 VAR(FLOAT)
			else if(registroFloat(operando2) && registroInt(operando1)) {
				int convertir = 1;
				//FUNCION ARIEL CONVERTIR
			}
		}
		//CONVERSION OPERANDO 2 REG (INTEGER) Y OPERANDO 1 AUX (FLOAT) 
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null){
			if(registroInt(operando2) && registroFloat(operando1)) {
				errorDeEjecucion("Se quiere asignar un FLOAT a un INTEGER");
			}
			else if(registroFloat(operando1) && registroFloat(operando2)) {
				int convertir = 0;
				//FUNCION ARIEL CONVERTIR
			}
			else if(registroInt(operando1) && registroFloat(operando2)) {
				int convertir = 1;
				//FUNCION ARIEL CONVERTIR
			}
		}
		//CONVERSION OPERANDO 2 AUX (FLOAT) Y OPERANDO 1 VAR (FLOAT) 
		else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null){
			if(registroInt(operando2) && this.getSimbolo(operando1).getTipoParametro().equals("FLOAT")) {
				errorDeEjecucion("Se quiere asignar un FLOAT a un INTEGER");
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroFloat(operando2)) {
				int convertir = 0;
				//FUNCION ARIEL CONVERTIR
			}
			else if(registroFloat(operando2) && this.getSimbolo(operando1).getTipoParametro().equals("INTEGER")) {
				int convertir = 1;
				//FUNCION ARIEL CONVERTIR
			}
		}
	}
	
	private void getCodAsignacionProc(){
		
		// x = y => ope1 = x ; ope2 = y

		String operando2 = pila.pop();
		String operando1 = pila.pop();
		String codigo = "";
		String operador = "";
		
		//I(OPERANDO 2) = J (OPERANDO 1)
		
		// SITACION 1 - OPERANDO 1 (REG/AUX) Y OPERANDO 2 (VAR)
		if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null){
			// SITACION 1.1 - OPERANDO 1 (REG) Y OPERANDO 2 (VAR) SON INTEGER - VARIANTE DE REGISTROS
			if(registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				codigo = plantillaAsignacion;
				codigo = codigo.replace("MOV VAR-REG, XX", "");
				codigo = codigo.replace("XX", operando2);
				codigo = codigo.replace("OP1", operando1);
				registro.ocuparRegistro(operando1, 0);
				this.code = this.code + codigo;
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroFloat(operando2) ) {
				int convertir = 0;
				//FUNCION ARIEL CONVERTIR
			}
			//CONVERSION - OPERANDO 2 VAR(INTEGER) Y OPERANDO 1 AUX(FLOAT)
			else if(this.getSimbolo(operando2).getTipoParametro().equals("INTEGER") && registroFloat(operando1) ) {
				errorDeEjecucion("Se quiere asignar un FLOAT a un INTEGER");
			}
			//CONVERSION - OPERANDO 2 AUX(FLOAT) Y OPERANDO 1 VAR(INTEGER)
			else if(this.getSimbolo(operando2).getTipoParametro().equals("FLOAT") && registroInt(operando1) ) {
				int convertir = 1;
				//FUNCION ARIEL CONVERTIR
			}
		}
		// SITACION 2 - OPERANDO 1 (VAR) Y OPERANDO 2 (VAR)
		else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null){
			//SITUACION 2.2 OPERANDO 1 Y 2 SON VAR Y SON INTEGER - VARIANTE DE REGISTROS	
			if (this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaAsignacion;
				codigo = codigo.replace("MOV VAR-REG, XX", "");
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando2);
				codigo = codigo + plantillaAsignacion;
				codigo = codigo.replace("MOV VAR-REG, XX", "");
				codigo = codigo.replace("OP1", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("XX", operando1);
				registro.ocuparRegistro(registro.getRegistro(1, "INTEGER"), 0);
				this.code = this.code + codigo;
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT") ) {
				int convertir = 0;
				//FUNCION ARIEL CONVERTIR
			}
			//CONVERSION - OPERANDO 2 VAR(INTEGER) Y OPERANDO 1 VAR(FLOAT)
			else if(this.getSimbolo(operando2).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") ) {
				errorDeEjecucion("Se quiere asignar un FLOAT a un INTEGER");
			}
			//CONVERSION - OPERANDO 1 VAR(INTEGER) Y OPERANDO 2 VAR(FLOAT)
			else if(registroFloat(operando2) && registroInt(operando1)) {
				int convertir = 1;
				//FUNCION ARIEL CONVERTIR
			}
		}
		//CONVERSION OPERANDO 2 REG (INTEGER) Y OPERANDO 1 AUX (FLOAT) 
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null){
			if(registroInt(operando2) && registroFloat(operando1)) {
				errorDeEjecucion("Se quiere asignar un FLOAT a un INTEGER");
			}
			else if(registroFloat(operando1) && registroFloat(operando2)) {
				int convertir = 0;
				//FUNCION ARIEL CONVERTIR
			}
			else if(registroInt(operando1) && registroFloat(operando2)) {
				int convertir = 1;
				//FUNCION ARIEL CONVERTIR
			}
		}
		//CONVERSION OPERANDO 2 AUX (FLOAT) Y OPERANDO 1 VAR (FLOAT) 
		else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null){
			if(registroInt(operando2) && this.getSimbolo(operando1).getTipoParametro().equals("FLOAT")) {
				errorDeEjecucion("Se quiere asignar un FLOAT a un INTEGER");
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroFloat(operando2)) {
				int convertir = 0;
				//FUNCION ARIEL CONVERTIR
			}
			else if(registroFloat(operando2) && this.getSimbolo(operando1).getTipoParametro().equals("INTEGER")) {
				int convertir = 1;
				//FUNCION ARIEL CONVERTIR
			}
		}
	}
	
	void errorDeEjecucion(String mensaje) {
		System.out.println(mensaje);
	}
	
	//Me devuelve el Simbolo para poder saber el tipo (INTEGER - FLOAT) y el uso (CTE - ID).
	private Simbolo getSimbolo(String elemento) {
		
		String [] aux = elemento.split("\\@");
		
		if(compilador.Compilador.tablaSimbolo.get(aux[0]) != null) {
			for(int i=0; i<compilador.Compilador.tablaSimbolo.get(aux[0]).size(); i++)
				if(compilador.Compilador.tablaSimbolo.get(aux[0]).get(i).getUso().equals("CTE")){
					return compilador.Compilador.tablaSimbolo.get(aux[0]).get(i);
				}
				else if(compilador.Compilador.tablaSimbolo.get(aux[0]).get(i).getAmbito().equals(elemento)){
					return compilador.Compilador.tablaSimbolo.get(aux[0]).get(i);
				}				
		}
		return null;		
	}
	
	
	private boolean registroInt(String op1) {
		if(op1.equals("AX") || op1.equals("BX") || op1.equals("CX") || op1.equals("DX")) 
			return true;
		else return false;
	}
	
	private boolean registroFloat(String op1) {
		if(op1.contains("@aux")) 
			return true;
		else 
			return false;
	}

	public String generarIstruccionesVariableAux(String reg, String operando1, String operando2, String operando) {
		String auxAux = generarVarAux();
		String testo  = "";
		if (operando.equals("+"))
			testo = plantillaSuma;
		if (operando.equals("-"))
			testo = plantillaResta;
		if (operando.equals("/"))
			testo = plantillaDivision;
		if (operando.equals("*"))
			testo = plantillaMultiplicacion;
		else
			return testo;
		testo = testo.replace("VAR-REG", auxAux);
		testo = testo.replace("XX", reg);
		testo = testo.replace("OP1", operando1);
		testo = testo.replace("OP2", operando2);
		data= data.concat(auxAux + " dd " + "?" + saltoDeLinea);
		System.out.println("AUXXXXX: " + auxAux);
		pila.push(auxAux);
		return testo;
	}
	
	public void generarInstruccionesFLOAT(String operando1, String operando2, String operacionARIT, int conv) {
		String formato = plantillaOperacionFloat;
		// ESTABLECER OPERACION
		// NINGUNO SE TIENE QUE CONVERTIR
		if (conv==0) {
			// REEMPLAZO DE LA OPERACION DE LA PILA
			formato = formato.replace("OpPila", "FLD");
			// REEMPLAZO DE APERACION ARITMETICA
			if (operacionARIT.equals("+")) { formato = formato.replace("OpArit", "FADD"); }
			if (operacionARIT.equals("-")) { formato = formato.replace("OpArit", "FSUB"); }
			if (operacionARIT.equals("/")) { formato = formato.replace("OpArit", "FDIV"); }
			if (operacionARIT.equals("*")) { formato = formato.replace("OpArit", "FMUL"); }

		}
		// SE TIENE QUE CONVERTIR OPRENDO2
		else if (conv==1) {
			// REEMPLAZO DE LA OPERACION DE LA PILA
			formato = formato.replace("OpPila", "FILD");
			// REEMPLAZO DE APERACION ARITMETICA
			if (operacionARIT.equals("+")) { formato = formato.replace("OpArit", "FADD"); }
			if (operacionARIT.equals("-")) { formato = formato.replace("OpArit", "FSUB"); }
			if (operacionARIT.equals("/")) { formato = formato.replace("OpArit", "FDIV"); }
			if (operacionARIT.equals("*")) { formato = formato.replace("OpArit", "FMUL"); }
		}
		else if (conv==2) {
			// REEMPLAZO DE LA OPERACION DE LA PILA
			formato = formato.replace("OpPila", "FLD");
			// REEMPLAZO DE APERACION ARITMETICA
			if (operacionARIT.equals("+")) { formato = formato.replace("OpArit", "FIADD"); }
			if (operacionARIT.equals("-")) { formato = formato.replace("OpArit", "FISUB"); }
			if (operacionARIT.equals("/")) { formato = formato.replace("OpArit", "FIDIV"); }
			if (operacionARIT.equals("*")) { formato = formato.replace("OpArit", "FIMUL"); }
		}
		// REEMPLAZO DE REGISTROS
		formato = formato.replace("REG1", operando1);
		formato = formato.replace("REG2", operando2);
		String auxiliar = generarVarAux();
		formato = formato.replace("resul", auxiliar);
		// AGREGAR INSTRUCCION AL MAIN
		this.main = this.main + formato;
		// AGREGAR VAIRABLE AL DATA
		String variableAAgregar = plantillaAgregarVarFLOAT; 
		variableAAgregar = variableAAgregar.replace("VAR", auxiliar);
		this.data = this.data + variableAAgregar;
	}
	
	
	public String generarCodigoParaFlotantes(String operando1, String operando2, String operacion, String registro, int operandoAConvertir) {
		// Generacion cuando NO hay que convertir
		if (operandoAConvertir==0) {
			if (operacion.equals("+")) { generarInstruccionesFLOAT(operando1, operando2, "+",0); }
			if (operacion.equals("-")) { generarInstruccionesFLOAT(operando1, operando2, "-",0); }
			if (operacion.equals("/")) { generarInstruccionesFLOAT(operando1, operando2, "/",0); }
			if (operacion.equals("*")) { generarInstruccionesFLOAT(operando1, operando2, "*",0); }
		}
		// Generacion cuando SI hay que convertir
		else {
			// operando1 operacion operando2
			// Si se necesita convertir el operando1
			if (operandoAConvertir==1) {
				if (operacion.equals("+")) { generarInstruccionesFLOAT(operando1, operando2, "+",1); }
				if (operacion.equals("-")) { generarInstruccionesFLOAT(operando1, operando2, "-",1); }
				if (operacion.equals("/")) { generarInstruccionesFLOAT(operando1, operando2, "/",1); }
				if (operacion.equals("*")) { generarInstruccionesFLOAT(operando1, operando2, "*",1); }
			}
			// Si se necesita convertir el operando2
			else if (operandoAConvertir==2) {
				if (operacion.equals("+")) { generarInstruccionesFLOAT(operando1, operando2, "+",2); }
				if (operacion.equals("-")) { generarInstruccionesFLOAT(operando1, operando2, "-",2); }
				if (operacion.equals("/")) { generarInstruccionesFLOAT(operando1, operando2, "/",2); }
				if (operacion.equals("*")) { generarInstruccionesFLOAT(operando1, operando2, "*",2); }
			}
		}
		return null;
	}
	
	public String generarAsignacion(String operando1, String operando2, int caso) {
		//public static String plantillaAsignacion = "MOV XX, OP1" + saltoDeLinea;
		//ublic static String extender16a32Bits   = "CWDE" + saltoDeLinea;
		String ardiente;
		if (caso == 0) {
			// GENERACION DE COGIDO
			String linea1 = plantillaAsignacion;
			String linea2 = plantillaAsignacion;
			String aux = generarVarAux();
			// REEMPLAZOD DE VARIABLES
			linea1 = linea1.replace("XX", aux); linea1 = linea1.replace("OP1", operando2);
			linea2 = linea2.replace("XX", operando1); linea2 = linea2.replace("OP1", aux);
			// AGREGAR VARIABLE
			String variableData = plantillaAgregarVarFLOAT;
			variableData = variableData.replace("VAR", aux);
			this.data = this.data + variableData;
			// AGREGAR CODIGO
			ardiente = linea1 + linea2;
			return ardiente;
		}
		if (caso ==1) {
			// GENERACION DE CODIGO
			String linea1 = plantillaAsignacion;
			String linea2 = extender16a32Bits;
			String linea3 = plantillaAsignacion;
			// REEMPLAZO DE VARIABLES
			linea1 = linea1.replace("XX", "AX"); linea1 = linea1.replace("OP1", operando2);
			linea3 = linea3.replace("XX", operando1); linea3 = linea3.replace("OP1", "EAX");
			// AGREGAR CODIGO
			ardiente = linea1 + linea2 + linea3;
			return ardiente;
		}
		return null;
	}	
	
	public String generarComparacion(String salto, String comparacion, int caso, String reg1, String reg2) {
		// FLOAT comp FLOAT
		if (caso==0) { return generarCodigoComparacion(salto, comparacion, caso, reg1, reg2); }
		// INTEGER comp FLOAT
		if (caso==1) { return generarCodigoComparacion(salto, comparacion, caso, reg1, reg2); }
		// FLOAT comp INTEGER
		if (caso==2) { return generarCodigoComparacion(salto, comparacion, caso, reg1, reg2); }
		// INTEGER comp INTEGER
		if (caso==3) { return generarSaltosInteger(comparacion, reg1, reg2); }
		return null;
	}
	
	public String generarCodigoComparacion(String salto, String comparacion, int caso, String reg1, String reg2) {
		String auxiliar = generarVarAux();
		String codigo = plantillaCargaCompFLOAT + plantillaComparacionFloat;
		// FLOAT comp FLOAT
		if (caso==0) {
			// REEMPLAZO DE OPERACION DE CARGA Y COMPARACION
			codigo = codigo.replace("carga", "FLD");
			codigo = codigo.replace("compa", "FCOMP");
		}
		// INTEGER comp FLOAT
		if (caso==1) {
			// REEMPLAZO DE OPERACION DE CARGA Y COMPARACION
			codigo = codigo.replace("carga", "FILD");
			codigo = codigo.replace("compa", "FCOMP");
		}
		// FLOAT comp INTEGER
		if (caso==2) {
			// REEMPLAZO DE OPERACION DE CARGA Y COMPARACION
			codigo = codigo.replace("carga", "FLD");
			codigo = codigo.replace("compa", "FICOMP");
		}
		// ESTABLECER SALTO
		String lineaSalto = "";
		if (comparacion.equals("<"))  { lineaSalto = CompMenorFLOAT; }
		if (comparacion.equals("<=")) { lineaSalto = CompMenorIgualFLOAT; }
		if (comparacion.equals(">"))  { lineaSalto = CompMayorFLOAT; }
		if (comparacion.equals(">=")) { lineaSalto = CompMayorIgualFLOAT; }
		if (comparacion.equals("==")) { lineaSalto = CompIgualFLOAT; }
		if (comparacion.equals("!=")) { lineaSalto = CompDistintoFLOAT; }
		codigo = codigo + lineaSalto;
		codigo = codigo.replace("label", salto);
		// REEMPLASO DE VARIABLE AUXILIAR Y OPERANDOS
		codigo = codigo.replaceAll("aux", auxiliar);
		codigo = codigo.replace("op1", reg1);
		codigo = codigo.replace("op2", reg2);
		// AGREGAR VARIABLE AL .data
		String variableData = plantillaAgregarVarFLOAT;
		variableData = variableData.replace("VAR", auxiliar);
		this.data = this.data + variableData;
		// RETORNA CODIGO QUE POSTERIORMENTE SE AGREGARA A .main, O A .code
		return codigo;
	}
	
	public String toString(){
		this.generarData();
		assembler = assembler + encabezado;
		assembler = assembler + data;
		assembler = assembler + code;
		assembler = assembler + inicioMainAssembler;
		assembler = assembler + main;
		assembler = assembler + finMainAssembler;
		return assembler;
	}
	
}
