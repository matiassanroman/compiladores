package compilador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/*  Para las operaciones entre datos de tipo entero se deberá generar código que utilice los registros del
	procesador (EAX, EBX, ECX Y EDX o AX, BX, CX y DX), y seguimiento de registros.
	Para las operaciones entre datos de punto flotante se deberá utilizar el co-procesador 80X87, y el
	mecanismo para generar código será el de variables auxiliares.
 ***** Controles en tiempo de ejecucion *****   
a) Division por cero:
   El codigo Assembler debero chequear que el divisor sea diferente de cero antes de efectuar una
   division. Este chequeo debera efectuarse para los dos tipos de datos asignados al grupo.
b) Overflow en sumas:
   El codigo Assembler debera  controlar el resultado de la operación indicada, para los dos tipos de
   datos asignados al grupo. Si el mismo excede el rango del tipo del resultado, debero emitir un
   mensaje de error y terminar.
*/
public class GeneradorAssembler {
	// agregar una variable archivo que es donde se va 
	// guardar el assembler
	
	private String assembler;
	private Conversor conversor;
	private Hashtable<String,ArrayList<Simbolo>> tablaSimbolo;
	
	//No va mas esto
	private ArrayList<String> estados;
	private ArrayList<String> registros32Bits;
	private ArrayList<String> registros16Bits;
	private ArrayList<String> registros8BitsBajos;
	private ArrayList<String> registros8BitAaltos;
	//No va mas esto
	
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
	
	public static String plantillaAsignacion = "MOV XX, OP1" + saltoDeLinea
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
	public static String CompMenorIgualFLAOT =     "JA Llabel"  + saltoDeLinea;
	public static String SaltoIncondicionalFLOAT = "JMP Llabel" + saltoDeLinea;
	public static String ComparacionFLOAT = "CMP RA, RB" + saltoDeLinea;
	
	//////////////////FIN PLANTILLAS DE OPERACIONES PARA FLOAT/////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	private String generarVarAux() {
		String var = varAux + Integer.toString(numeroVar) ;
		numeroVar += 1;
		return var;
	}
	
	public GeneradorAssembler(Hashtable<String,ArrayList<Simbolo>> tablaSimbolo, PolacaInversa polaca) {
		this.assembler = "";
		this.conversor = new Conversor();
		this.pila = new Stack<String>();
		
		//No va mas esto
		this.estados  = new ArrayList<String>(Arrays.asList("L","L","L","L"));
		this.registros32Bits     = new ArrayList<String>(Arrays.asList("EAX","EBX","ECX","EDX"));
		this.registros16Bits     = new ArrayList<String>(Arrays.asList( "AX", "BX", "CX", "DX"));
		this.registros8BitsBajos = new ArrayList<String>(Arrays.asList( "AL", "BL", "CL", "DL"));
		this.registros8BitAaltos = new ArrayList<String>(Arrays.asList( "AH", "BH", "CH", "DH")); 
		this.tablaSimbolo = tablaSimbolo;
	}
	
	private String generarInvocacion(String etiqueta) {
		String nombreProc = etiqueta.replace("PROC ","");
		String paraCode = plantillaCall.replace("ETIQUETA", nombreProc);
		return paraCode;
	}
	
	private String generarCall(String nombreProc){
		String invocacion = plantillaCall.replace("F", nombreProc);
		return invocacion;
	}
	
	private String generarMensajePorPantalla(String cadenaAMostrar, String destino){
		cadenaAMostrar = cadenaAMostrar.replace("\"", "");
		cadenaAMostrar = "_"+cadenaAMostrar;
		cadenaAMostrar = cadenaAMostrar.replace(" ", "_");
		String codigo = plantillaMostrarPorPantalla.replace("VAR", cadenaAMostrar);	
		destino = destino + codigo;	
		return destino;
	}

	public String generarSaltos(String comp, String pos, String salto, String regComp1, String regComp2){
		String bestial;
		bestial = plantillaComparacion;
		if (salto.equals("BF")) {
			if (comp.equals("<"))  bestial = bestial + plantillaCompMenor;
			else if (comp.equals(">"))  bestial = bestial + plantillaCompMayor;
				else if (comp.equals("<=")) bestial = bestial + plantillaCompMenorIgual;
					else if (comp.equals(">=")) bestial = bestial + plantillaCompMayorIgual;
						else if (comp.equals("==")) bestial = bestial + plantillaCompIgual;
							else if (comp.equals("!=")) bestial = bestial + plantillaCompDistinto;
		}
		else if (salto.equals("BI"))
			bestial = bestial + plantillaSaltoIncondicional;
		bestial = bestial.replace("RA", regComp1);
		bestial = bestial.replace("RB", regComp2);
		bestial = bestial.replace("label", pos);
		return bestial;
	}
	
	public String generarEtiqueta(String label){
		String abominacion = plantillaEtiqueta;
		abominacion = abominacion.replace("ETIQUETA", label.replace("PROC ", ""));
		return abominacion;
	}
	
	public String generarAsignacion(String reg, String izquierdo, String derecho){
		String omunculo = plantillaAsignacion;
		omunculo = omunculo.replace("XX", reg);
		omunculo = omunculo.replace("OP1", derecho);
		omunculo = omunculo.replace("VAR-REG", izquierdo);
		return omunculo;
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
						main = generarMensajePorPantalla(cadena, main);
				}
				if (elemento.equals("CALL")){              // Si es CALL generar llamado
					String nProc = pila.pop();
					this.main = this.main + generarCall(nProc);
				}
				if (operadoresBinarios.contains(elemento)) {
					if (elemento.equals("=")) {
						String operando1 = pila.pop();
						String operando2 = pila.pop();	
					}	
				}
			}
			if (operadoresBinarios.contains(elemento)) {
				if (elemento.equals("+")) {
//					this.getCodSuma();
				}
				if (elemento.equals("=")) {
					this.getCodAsignacion();
				}
			}
			
			
			

			
			if (elemento.contains(PROC)) {                            // Si el PROC. agrego todo ese codigo con su nombre de pro en la seccion .code
				this.code = this.code + generarEtiqueta(elemento);				  //AGREGA INVOCAION AL PROCEDIMIENTO DESDE DESDE EL MAIN
				i++;
				while (listaPolaca.get(i).getValor() != "RET") {  	  // mientras no llegue RET agrego todo ese codigo a la funcion en el code
					elemento = listaPolaca.get(i).getValor();
					if ( !this.operadoresUnarios.contains(elemento) && !this.operadoresBinarios.contains(elemento) && !elemento.contains(PROC) )  // Si son ids o ctes las apilo
						pila.add(elemento);
					if (operadoresUnarios.contains(elemento)) {    // Si es un operador unario
						if (elemento.equals("OUT")) {              // Si es OUT generar mensaje
								String cadena = pila.pop();
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
	
//	private void getCodSuma() {
//		
//		// x + y => ope1 = x ; ope2 = y
//		String operando2 = pila.pop();
//		String operando1 = pila.pop();
//		
//		String codigo = "";
//		
//		if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
//			//SEGUIMIENTO DE REGISTROS - INTEGER - (2 VARIABLES O CTES)
//			//SITUACION 1
//			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
//				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER"), 1);
//				codigo = plantillaSuma;
//				codigo = codigo.replace("MOV VAR-REG, XX", "");
//				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
//				codigo = codigo.replace("OP1", operando1);
//				codigo = codigo.replace("OP2", operando2);
//				pila.push(registro.getRegistro(1, "INTEGER"));
//				this.main = this.main + codigo;
//			}
//			//VARIABLES AUXILIARES - FLOAT - LOS DOS OPERANDOS
//			//SITUACION 2
//			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
//				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT"), 1);
//				String reg = registro.getRegistro(1, "FLOAT");
//				this.main = this.main + generarIstruccionesVariableAux(reg, operando1, operando2, "+");
//				registro.ocuparRegistro(reg, 0);
//			}
//			//SINO CONVERTIR
//			else {
//				//OPERANDO 1 FLOAT Y OPERANDO 2 INTEGER
//				//SITUACION 3
//				if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
//					codigo = "FILD OP1" + saltoDeLinea +
//							 "FIADD OP1" + saltoDeLinea;
//					codigo = codigo.replace("OP1", "_"+operando2);
//					registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT"), 1);
//					String reg = registro.getRegistro(1, "FLOAT");
//					this.main = this.main + generarIstruccionesVariableAux(reg, operando1, operando2, "+");
//					registro.ocuparRegistro(reg, 0);
//				}
//				//OPERANDO 1 INTEGER Y OPERANDO 2 FLOAT
//				//SITUACION 4
//				else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
//					
//				}
//			}
//		}
//		//(OPERANDO 1 ES UN REGISTRO)
//		//SITUACION 5
//		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null &&
//			(registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) ) {
//				codigo = "ADD XX, OP2" + saltoDeLinea;
//				codigo = codigo.replace("XX", operando1);
//				codigo = codigo.replace("OP2", operando2);
//				pila.push(operando1);
//				this.main = this.main + codigo;
//		}
//		//VARIABLES AUXILIARES - FLOAT - OPERANDO 1: AUX OPERANDO 2: FLOAT
//		//SITUACION 6
//		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null &&	
//		(registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) ) {
//				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT"), 1);
//				String reg = registro.getRegistro(1, "FLOAT");
//				this.main = this.main + generarIstruccionesVariableAux(reg, operando1, operando2, "+");
//				registro.ocuparRegistro(reg, 0);
//		}
//		//SINO CONVERTIR
//		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
//			//OPERANDO 1 AUX Y OPERANDO 2 INTEGER
//			//SITUACION 7
//			if(registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
//				
//			}
//			//OPERANDO 1 REG Y OPERANDO 2 FLOAT
//			//SITUACION 8
//			else if(registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
//				
//			}
//		}
//		//(2 REGISTROS) - (DEPENDE DEL REGISTRO QUE MUESTRE HAY QUE CONVERTIR O NO)
//		//SITUACION 9
//		else if( (this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) &&
//			(registroInt(operando1) && registroInt(operando2)) ) {
//				codigo = "ADD XX, OP2" + saltoDeLinea;
//				codigo = codigo.replace("XX", operando1);
//				codigo = codigo.replace("OP2", operando2);
//				pila.push(registro.getRegistro(1, "INTEGER"));
//				registro.ocuparRegistro(operando2, 0);
//				this.main = this.main + codigo;
//			}
//			//SINO CONVERTIR
//			else {
//				//OPERANDO 1 AUX Y OPERANDO 2 REG
//				//SITUACION 10
//				if(registroFloat(operando1) && registroInt(operando2)) {
//					
//				}
//				//OPERANDO 1 REG Y OPERANDO 2 AUX
//				//SITUACION 11
//				if(registroInt(operando1) && registroFloat(operando2)) {
//					
//				}
//			}
//		}
//		else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
//			//(OPERANDO 2 ES UN REGISTRO)
//			//SITUACION 12
//			if(registroInt(operando2) && this.getSimbolo(operando1).getTipoParametro().equals("INTEGER")) {
//				codigo = "ADD XX, OP2" + saltoDeLinea;
//				codigo = codigo.replace("XX", operando2);
//				codigo = codigo.replace("OP2", operando1);
//				pila.push(operando2);
//				this.main = this.main + codigo;
//			}
//			//VARIABLES AUXILIARES - FLOAT - OPERANDO 2: AUX
//			//SITUACION 13
//			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroFloat(operando2)) {
//				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT"), 1);
//				String reg = registro.getRegistro(1, "FLOAT");
//				this.main = this.main + generarIstruccionesVariableAux(reg, operando1, operando2, "+");
//				registro.ocuparRegistro(reg, 0);
//			}
//			//SINO CONVERTIR
//			else {
//				//OPERANDO 1 VAR/CTE(INT) Y OPERANDO 2 AUX
//				//SITUACION 14
//				if(registroFloat(operando1) && registroInt(operando2)) {
//					
//				}
//				//OPERANDO 1 VAR/CTE(FLOAT) Y OPERANDO 2 REG
//				//SITUACION 15
//				else if(registroInt(operando1) && registroFloat(operando2)) {
//				}
//			}
//		}		
//	}


	private void getCodAsignacion(){
		
		// x = y => ope1 = x ; ope2 = y
		String operando2 = pila.pop();
		String operando1 = pila.pop();
		System.out.println("ENTRO 1: " + operando1);
		String codigo = "";
		
		//I(OPERANDO 2) = J (OPERANDO 1)
		
		//SITACION 1
		// OPERANDO 1 Y OPERANDO 2 SON INTEGER - 1 ES REG
		if( (this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) && 
		 (registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) ) {
			codigo = plantillaAsignacion;
			codigo = codigo.replace("MOV VAR-REG, XX", "");
			codigo = codigo.replace("XX", operando2);
			codigo = codigo.replace("OP1", operando1);
			registro.ocuparRegistro(operando1, 0);
			this.main = this.main + codigo;
		}
		//SITUACION 2
		//OPERANDO 1 Y OPERANDO 2 SON INTEGER - 2 VAR/CTE
		else if( (this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) && 
			(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) ) {
			registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER"), 1);
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
		//SITUACION 3
		//OPERANDO 1 Y OPERANDO 2 SON FLOAT
		else if( (this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) &&
			(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) ) {
			registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT"), 1);
			String reg = registro.getRegistro(1, "FLOAT");
			this.main = this.main + generarAsignacion(reg, operando2, operando1);
			registro.ocuparRegistro(reg, 0);
		}
		//SITUACION 4
		// OPERANDO 2 SEA INTEGER Y OPERANDO 1 FLOAT - ERROR
		else if(this.getSimbolo(operando2) != null && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				errorDeEjecucion("Se quiere asignar un FLOAT a un INTEGER");
		}
		//SITUACION 4
		// OPERANDO 2 SEA INTEGER Y OPERANDO 1 FLOAT - ERROR
		else if(this.getSimbolo(operando2) == null && registroInt(operando2)) {
				errorDeEjecucion("Se quiere asignar un FLOAT a un INTEGER");
		}
		//SITUACION 5
		// OPERANDO 2 SEA FLOAT Y OPERANDO 1 INTEGER - CONVERSION
		else {
			registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT"), 1);
			String reg = registro.getRegistro(1, "FLOAT");
			codigo = "FILD OP1" + saltoDeLinea +
					 "FSTP OP2" + saltoDeLinea;
			codigo = codigo.replace("OP1", "_"+operando1);
			codigo = codigo.replace("OP2", "_"+operando2);
			this.main = this.main + codigo ;
			registro.ocuparRegistro(reg, 0);
		}
	}
	
	void errorDeEjecucion(String mensaje) {
		System.out.println(mensaje);
	}
	
	//Me devuelve el Simbolo para poder saber el tipo (INTEGER - FLOAT) y el uso (CTE - ID).
	private Simbolo getSimbolo(String elemento) {
		 
		String [] aux = elemento.split("\\:");
		
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
		/*= "OpPila REG1" + saltoDeLinea      Agrega REG a la pila de coprocesador
		 + "OpArit REG2" + saltoDeLinea       // Realiza la operacion entre el tope de de l apila de coprocesador y REG
		 + "OpSacar resul" + saltoDeLinea;*/
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
