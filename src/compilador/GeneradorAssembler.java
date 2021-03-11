package compilador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import accionesSemanticas.AS10_Verificar_Rango_Float;

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
	
	private int nroLinea = compilador.Compilador.nroLineaConversion.size()-1;
	private String assembler;
	private Hashtable<String,ArrayList<Simbolo>> tablaSimbolo;
	private String ultimaAsignado=""; 
	private boolean flagOpInteger = false;
	Registros registro = new Registros();
	
	private ArrayList<String> operadoresBinarios = new ArrayList<String>(Arrays.asList("+","-","*","/","<","<=",">",">=","==","!=","="));
	private ArrayList<String> operadoresUnarios = new ArrayList<String>(Arrays.asList("OUT","BF","BI","CALL"));
	private String PROC = "PROC";
	
	private Stack<String> pila;
	
	public static int numeroVar = 1;
	public static String varAux = "@aux";
	
	public static String saltoDeLinea = "\r\n";
	
	private String comparativa = "< > >= <= == !=";
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
			"mensaje db \"Mensaje por pantalla\", 0" + saltoDeLinea +
			"errorOverflowSuma db \"Ha ocurrido overflow al sumar, programa terminado inesperadamente\", 0" + saltoDeLinea +
			"errorDivisionCero db \"Se intenta dividir por cero, programa terminado por su seguridad\", 0" + saltoDeLinea +
			"_MAX dd 3.40282347f+38" + saltoDeLinea;
	private String data = "";
	private String code = ".code" + saltoDeLinea
						+ "overflow:" + saltoDeLinea
						+ "invoke MessageBox, NULL, addr errorOverflowSuma, addr mensaje, MB_OK " + saltoDeLinea
						+ "call fin" + saltoDeLinea
						+ "divcero:" + saltoDeLinea
						+ "invoke MessageBox, NULL, addr errorDivisionCero, addr mensaje, MB_OK " + saltoDeLinea
						+ "call fin" + saltoDeLinea;
	public static String inicioMainAssembler = "main:" + saltoDeLinea 
										     + "FNINIT" + saltoDeLinea 
										     + "FNCLEX" + saltoDeLinea;
	private String main = "";
	public static String finMainAssembler = "FNINIT" + saltoDeLinea 
										  + "FNCLEX" + saltoDeLinea
										  + "fin: invoke ExitProcess, 0" + saltoDeLinea
										  +	"end main" + saltoDeLinea;	
	
	/////////// FIN ESTRUCTURA DEL ARCHIVO CON EL ASEMBLER ////////////////////////
	///////////////////////////////////////////////////////////////////////////////


	///////////////////////////////////////////////////////////////////////////////
	///////////////// PLANTILLAS DE OPERACIONES ENTRE INTEGER /////////////////////
	public static String plantillaOperacion = "MOV XX, OP1" + saltoDeLinea 
											+ "OP XX, OP2" + saltoDeLinea;

	public static String plantillaAsignacion = "MOV XX, OP1" + saltoDeLinea;

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
	
	public static String saltoPorOverflow = "JO overflow" + saltoDeLinea;
	public static String saltoDivCero = "JZ divcero" + saltoDeLinea;
	public static String cargar0ALaPila = "FLDZ" + saltoDeLinea;

	
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
	
	private String corregirOperando(String operando) {
		String corregido = operando;
		if (Character.isDigit(operando.charAt(0)))
			corregido = "_"+corregido;
		return corregido;
	}
	
	private String generarCall(String nombreProc){
		
		String invocacion = "";
		if (nombreProc.matches("[0-9]*"))
			invocacion = plantillaCall.replace("F", "L"+nombreProc);
		else
			invocacion = plantillaCall.replace("F", nombreProc);
		return invocacion;
	}
	
	private String generarMensajePorPantalla(String cadenaAMostrar){
		//.data no aparece el guion
		cadenaAMostrar = cadenaAMostrar.replace("\"", "");
		cadenaAMostrar = cadenaAMostrar.replace("-", "");
		cadenaAMostrar = cadenaAMostrar.replaceAll(" ", "_");
	
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
		bestial = bestial.replace("RA", regComp2);
		bestial = bestial.replace("RB", regComp1);
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
    		   } 
    		   else if(aux.get(i).getUso().equals("ID")) {
				   if(aux.get(i).getTipoParametro().equals("INTEGER"))
					   this.data = this.data + aux.get(i).getAmbito().replaceAll(":", "@") + " dw ?" + saltoDeLinea;
			   		if(aux.get(i).getTipoParametro().equals("FLOAT")) {
			   			String nombre = aux.get(i).getAmbito().replaceAll(":", "@");
			   			nombre = nombre.replace(".", "");
						nombre = nombre.replace("E", "e");
						nombre = nombre.replace("+", "");
						nombre = nombre.replace("-", "");
			   			this.data = this.data + nombre + " dd ?" + saltoDeLinea;
			   		}
    		   }
    		   else if(aux.get(i).getUso().equals("CTE")) {
				   if(aux.get(i).getTipo().equals("int")){
					   // ESTA SECCION SE AGREGA PARA DIFERENCIAR LOS NUMEROS NEGATIVOS EN EL ASSEMBLER
					   if ( (aux.get(i).getValor()).contains("-"))
						   this.data = this.data + "__" + aux.get(i).getValor().replaceAll(":", "@") + " dw " + aux.get(i).getValor() + saltoDeLinea;
					   else
						   this.data = this.data + "_" + aux.get(i).getValor().replaceAll(":", "@") + " dw " + aux.get(i).getValor() + saltoDeLinea;
					   /////////////////////////////////////////////////////////////////////////////////
				   }
				   if(aux.get(i).getTipo().equals("float")) {
					   String nombre = aux.get(i).getValor().replaceAll(":", "@");
					   String valor = aux.get(i).getValor();
					   // CON MODIFICACION PARA DIFERENCIAR VARIABLES CON . EN EL ASSEMBLER
					   nombre = nombre.replace(".", "_");
					   ////////////////////////////////////////////////////////////////////
					   nombre = nombre.replace("E", "e");
					   if (nombre.charAt(0)== '-')
						   nombre = "__"+nombre.substring(1, nombre.length());
					   else
						   nombre = "_"+nombre;
					   nombre = nombre.replace("+", "");
					   nombre = nombre.replace("-", "");
					   valor = valor.replace("E", "e");
					   this.data = this.data + nombre + " dd " + valor + saltoDeLinea;
				   }
			   }
    		   else if(aux.get(i).getUso().equals("CADENA")) {
    			   	   String cadenipi = aux.get(i).getValor();
    			   	   String cadenipi2 = cadenipi;
    			   	   cadenipi = cadenipi.replaceAll("\"", "");
    			   	   cadenipi = cadenipi.replaceAll(" ", "_");
    			   	   cadenipi = cadenipi.replaceAll("-", "");
    			   	   this.data = this.data + cadenipi + " db " + cadenipi2 + ", 0" + saltoDeLinea;			   
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
	/////////////////////////////////// METODO PRINCIPAL DE GENERACION DE ASSEMBLER //////////////////////////////////////////////	
	
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
					nroLinea--;
				}
				if (elemento.equals("<") || elemento.equals("<=") || elemento.equals(">") || elemento.equals(">=") || elemento.equals("==") || elemento.equals("!=")) {
					String operando2 = pila.pop();  // Ver el assembler si es el op1
					String operando1 = pila.pop();  // Ver el assembler si es el op2
					i++; 
					String salto = listaPolaca.get(i).getValor(); // Posicion  para generar el label
					// generar comparacion
//					System.out.println("1-->OPERANDO1: "+operando1);
//					System.out.println("1-->OPERANDO2: "+operando2);
					generarComparadores(salto, elemento, operando1, operando2);
				}
			}
			// generar labels
			if (elemento.charAt(0) == 'L' ) {
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
							char separador = '@';
							//System.out.println("1-->NOMBRE DE LA LLAMADA: "+nProc);
							int posPrimerSeparador = nProc.indexOf("@");
							//System.out.println("2-->POSICION DEL PRIMER SEPARADOR: "+posPrimerSeparador);
							String nombreABorrar = separador+nProc.substring(0,posPrimerSeparador);
							//System.out.println("3-->NOMBRE A BORRAR: "+nombreABorrar);
							String restoDelNombre = nProc.substring(posPrimerSeparador);
							if (!restoDelNombre.contains(nombreABorrar))
								this.code = this.code + generarCall(nProc);
							else {
								//System.out.println("4-->RESTO DEL NOMBRE: "+restoDelNombre);
								String nombreFinal = nProc.substring(posPrimerSeparador);
								//System.out.println("5-->PRIMERA POSICION DEL NOMBRE A BORRAR: "+nombreFinal.indexOf(nombreABorrar));
								restoDelNombre = restoDelNombre.replace(nombreABorrar, "");
								//System.out.println("6-->RESTO FINAL DEL NOMBRE: "+restoDelNombre);
								String nombreDefinitivo = nombreABorrar+restoDelNombre;
								//System.out.println("7-->NOMBRE DEFINITIVO QUE SE VA A EJECUTAR: "+nombreDefinitivo);
								int principioIdDemas = nombreDefinitivo.length()-1;
								while (principioIdDemas>0 && nombreDefinitivo.charAt(principioIdDemas) != separador)
									principioIdDemas -=1;
								//System.out.println("8-->PRINCIPIO DEL ULTIMO ID: "+principioIdDemas);
								//System.out.println("9-->SEPARADOR ENCONTRADO: "+nombreDefinitivo.charAt(principioIdDemas));
								//System.out.println("10-->ultraInstinto: "+nombreDefinitivo.substring(0, principioIdDemas));
								String ultraInstintoNombre = nombreDefinitivo.substring(1, principioIdDemas);
								this.code = this.code + generarCall(ultraInstintoNombre);
							}
						}
						if (elemento.equals("BI")) {
							String salto = pila.pop();
							this.code = this.code + generarBI(salto);
						}
					}
					if (operadoresBinarios.contains(elemento)) {
						if (elemento.equals("+") || elemento.equals("-") || elemento.equals("*") || elemento.equals("/")) {
							this.generarAritmeticaProc(elemento);
						}
						if (elemento.equals("=")) {
							this.getCodAsignacionProc();
							
							nroLinea--;
						}
						if (elemento.equals("<") || elemento.equals("<=") || elemento.equals(">") || elemento.equals(">=") || elemento.equals("==") || elemento.equals("!=")) {
							String operando2 = pila.pop();  // Ver el assembler si es el op1
							String operando1 = pila.pop();  // Ver el assembler si es el op2
							i++; 
							String salto = listaPolaca.get(i).getValor(); // Posicion  para generar el label
							i++;
							// generar comparacion
							generarComparadoresProc(salto, elemento, operando1, operando2);
							// generar salto
							this.code  =this.code + generarCall(salto);
						}
					}
					if (elemento.charAt(0) == 'L') {
						this.code = this.code + generarInvocacion(elemento);
					}
					i++;
				}
				if (listaPolaca.get(i).getValor() == "RET")           // cuendo detecto RET lo agrego al code para cerar el procedimiento 
					this.code = this.code + "ret" + saltoDeLinea;
			}
		}
	}

	/////////////////////////////////// FIN METODO PRINCIPAL DE GENERACIKON DE ASSEMBLER /////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	private void generarAritmetica(String operador) {
		///////////////////////////////////////////////////// revisar este metodo para lo controles del tp4
		// OPERANDO1 + OPERANDO2;
		//PILA: OPERANDO1 OPERANDO2 (TOPE)
		String operando2 = pila.pop();
		String operando1 = pila.pop();
		if (operando1.contains("_")) {
			int pos = operando1.indexOf('_');
			operando1 = operando1.substring(0, pos);
		}
		
		//SON DOS NUMEROS O CTE
		if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				generarCodigoParaInteger(operando1, operando2, operador);
			}
			if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.main = this.main + generarCodigoParaFlotantes(operando1, operando2, operador, 0);
			}
			//COVERSION
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				this.main = this.main + generarCodigoParaFlotantes(operando1, operando2, operador, 2);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.main = this.main + generarCodigoParaFlotantes(operando1, operando2, operador, 1);
			}
		}
		//SON UN NUMERO/CTE Y UN REG/AUX
		else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.registroInt(operando2)) {
				generarCodigoParaInteger(operando1, operando2, operador);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.registroFloat(operando2)) {
				this.main = this.main + generarCodigoParaFlotantes(operando1, operando2, operador, 0);
			}
			//COVERSION
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroInt(operando2)) {
				this.main = this.main + generarCodigoParaFlotantes(operando1, operando2, operador, 2);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && registroFloat(operando2)) {
				this.main = this.main + generarCodigoParaFlotantes(operando1, operando2, operador, 1);
			}
		}
		//SON UN REG/AUX Y UN NUMERO/CTE
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
			if(this.registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				generarCodigoParaInteger(operando1, operando2, operador);
			}
			else if(this.registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.main = this.main + generarCodigoParaFlotantes(operando1, operando2, operador, 0);
			}
			//COVERSION
			else if(registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				this.main = this.main + generarCodigoParaFlotantes(operando1, operando2, operador, 2);
			}
			else if( registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.main = this.main + generarCodigoParaFlotantes(operando1, operando2, operador, 1);
			}
		}
		//SON DOS REG/AUX
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
			if(this.registroInt(operando1) && this.registroInt(operando2)) {
				generarCodigoParaInteger(operando1, operando2, operador);
			}
			else if(this.registroFloat(operando1) && this.registroFloat(operando2)) {
				this.main = this.main + generarCodigoParaFlotantes(operando1, operando2, operador, 0);
			}
			//COVERSION
			else if(this.registroFloat(operando1) && this.registroInt(operando2)) {
				this.main = this.main + generarCodigoParaFlotantes(operando1, operando2, operador, 2);
			}
			else if(this.registroInt(operando1) && this.registroFloat(operando2)) {
				this.main = this.main + generarCodigoParaFlotantes(operando1, operando2, operador, 1);
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
//				System.out.println("2->CODIGO A AGREGAR SIN CAMBIOS DE OPERANDOS: "+saltoDeLinea + codigo);
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				if (ultimaAsignado.contains("X"))
					codigo = codigo.replace("OP1", ultimaAsignado);
				else
					codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "ADD");
				pila.push(registro.getRegistro(1, "INTEGER"));
				codigo = codigo + saltoPorOverflow;
//				System.out.println("2->OPERANDO1: "+operando1);
//				System.out.println("2->OPERANDO2: "+operando2);
//				System.out.println("2->ULTIMO ASIGNADO: "+ultimaAsignado);
//				System.out.println("2->CODIGO A AGREGAR: "+saltoDeLinea + codigo);
				ultimaAsignado = "";
				this.main = this.main + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
					codigo = "ADD XX, OP2" + saltoDeLinea;
					codigo = codigo.replace("XX", operando1);
					codigo = codigo.replace("OP2", operando2);
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
				codigo = codigo + saltoPorOverflow;
				this.main = this.main + codigo;
			}
			//SITUACION 4A - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				codigo = "ADD XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando2);
				codigo = codigo.replace("OP2", operando1);
				pila.push(operando2);
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
				codigo = codigo + "CWD" + saltoDeLinea;
				this.main = this.main + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
					codigo = "IMUL XX, OP2" + saltoDeLinea;
					codigo = codigo.replace("XX", operando1);
					codigo = codigo.replace("OP2", operando2);
					pila.push(operando1);
					codigo = codigo + "CWD" + saltoDeLinea;
					this.main = this.main + codigo;
			}
			//SITUACION 3 - (2 REGISTROS)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
				codigo = "IMUL XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(registro.getRegistro(1, "INTEGER"));
				codigo = codigo + "CWD" + saltoDeLinea;
				registro.ocuparRegistro(operando2, 0);
				this.main = this.main + codigo;
			}
			//SITUACION 4A - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				codigo = "IMUL XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando2);
				codigo = codigo.replace("OP2", operando1);
				pila.push(operando2);
				codigo = codigo + "CWD" + saltoDeLinea;
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
				ultimaAsignado = registro.getRegistro(0, "INTEGER");
				codigo = codigo.replace("XX", ultimaAsignado);
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "SUB");
				registro.ocuparRegistro(operando2, 0);
				pila.push(operando1);
//				System.out.println("1->OPERANDO1: "+operando1);
//				System.out.println("1->OPERANDO2: "+operando2);
//				System.out.println("1->ULTIMO ASIGNADO: "+ultimaAsignado);
//				System.out.println("1->CODIGO A AGREGAR: "+saltoDeLinea+codigo);
				this.main = this.main + codigo;
			}
		}
		//DIVISION
		else if(operador.equals("/")) {
			//SITUACION 1 - (2 VARIABLES O CTES)
			if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = codigo + "MOV XX, OP2" + saltoDeLinea;
				codigo = codigo + plantillaComparacion;
				codigo = codigo + plantillaCompDistinto;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("RA", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("RB", "0");
				codigo = codigo.replace("Llabel", "divcero");
				codigo = codigo + plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				// SECCION PARA BORAR 1ER OPERANDO DEL IDIV
				codigo = codigo.replace("OP1", operando1);    //OP1 DE MOV
				codigo = codigo.replace("OP2", operando2);    //OP2 DE IDIV
				codigo = codigo.replace("OP", "IDIV");
				codigo = codigo.replace("IDIV "+registro.getRegistro(1, "INTEGER")+", ", "IDIV ");
				///////////////////////////////////////////
				// SECCION QUE SOLUCIONA EL GUION BAJO QUE AVECES APARECE Y OTRA NO
				if (!operando1.contains("@"))
					codigo = codigo.replace("IDIV "+operando1+", ", "IDIV _");
				///////////////////////////////////////////////////////////////////
				pila.push(registro.getRegistro(1, "INTEGER"));	
				this.main = this.main + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
				codigo = codigo + plantillaComparacion;
				codigo = codigo + plantillaCompDistinto;
				codigo = codigo.replace("RA", operando2);
				codigo = codigo.replace("RB", "0");
				codigo = codigo.replace("Llabel", "divcero");
				// SECCION PARA BORAR 1ER OPERANDO DEL IDIV				
				codigo = codigo + "IDIV OP2" + saltoDeLinea;
				codigo = codigo.replace("OP2", operando2);
				///////////////////////////////////////////
				pila.push(operando1);
				this.main = this.main + codigo;
			}
			//SITUACION 3 - (2 REGISTROS)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
				codigo = codigo + plantillaComparacion;
				codigo = codigo + plantillaCompDistinto;
				codigo = codigo.replace("RA", operando2);
				codigo = codigo.replace("RB", "0");
				codigo = codigo.replace("Llabel", "divcero");
				// SECCION PARA BORAR 1ER OPERANDO DEL IDIV				
				codigo = codigo + "IDIV OP2" + saltoDeLinea;
				codigo = codigo.replace("OP2", operando2);
				///////////////////////////////////////////
				pila.push(registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(operando2, 0);
				this.main = this.main + codigo;
			}
			//SITUACION 4B - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = codigo + plantillaComparacion;
				codigo = codigo + plantillaCompDistinto;
				codigo = codigo.replace("RA", operando2);
				codigo = codigo.replace("RB", "0");
				codigo = codigo.replace("Llabel", "divcero");
				codigo = codigo  + plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				// SECCION PARA BORAR 1ER OPERANDO DEL IDIV				
				codigo = codigo + "IDIV OP2" + saltoDeLinea;
				codigo = codigo.replace("OP2", operando2);
				///////////////////////////////////////////
				pila.push(operando1);
				registro.ocuparRegistro(operando2, 0);
				this.main = this.main + codigo;
			}
		}
	}
	
	private void generarAritmeticaProc(String operador) {
		
		// OPERANDO1 + OPERANDO2;
		//PILLA: OPERANDO1 OPERANDO2 (TOPE)
		String operando2 = pila.pop();
		String operando1 = pila.pop();
		if (operando1.contains("_")) {
			int pos = operando1.indexOf('_');
			operando1 = operando1.substring(0, pos);
		}
		//SON DOS NUMEROS O CTE
		if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				generarCodigoParaIntegerProc(operando1, operando2, operador);
			}
			if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.code = this.code + generarCodigoParaFlotantes(operando1, operando2, operador, 0);
			}
			//COVERSION
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				this.code = this.code + generarCodigoParaFlotantes(operando1, operando2, operador, 2);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.code = this.code + generarCodigoParaFlotantes(operando1, operando2, operador, 1);
			}
		}
		//SON UN NUMERO/CTE Y UN REG/AUX
		else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.registroInt(operando2)) {
				generarCodigoParaIntegerProc(operando1, operando2, operador);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.registroFloat(operando2)) {
				this.code = this.code + generarCodigoParaFlotantes(operando1, operando2, operador, 0);
			}
			//COVERSION
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroInt(operando2)) {
				this.code = this.code + generarCodigoParaFlotantes(operando1, operando2, operador, 2);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && registroFloat(operando2)) {
				this.code = this.code + generarCodigoParaFlotantes(operando1, operando2, operador, 1);
			}
		}
		//SON UN REG/AUX Y UN NUMERO/CTE
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
			if(this.registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				generarCodigoParaIntegerProc(operando1, operando2, operador);
			}
			else if(this.registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.code = this.code + generarCodigoParaFlotantes(operando1, operando2, operador, 0);
			}
			//COVERSION
			else if(registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				this.code = this.code + generarCodigoParaFlotantes(operando1, operando2, operador, 2);
			}
			else if( registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.code = this.code + generarCodigoParaFlotantes(operando1, operando2, operador, 1);
			}
		}
		//SON DOS REG/AUX
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
			if(this.registroInt(operando1) && this.registroInt(operando2)) {
				generarCodigoParaIntegerProc(operando1, operando2, operador);
			}
			else if(this.registroFloat(operando1) && this.registroFloat(operando2)) {
				this.code = this.code + generarCodigoParaFlotantes(operando1, operando2, operador, 0);
			}
			//COVERSION
			else if(this.registroFloat(operando1) && this.registroInt(operando2)) {
				this.code = this.code + generarCodigoParaFlotantes(operando1, operando2, operador, 2);
			}
			else if(this.registroInt(operando1) && this.registroFloat(operando2)) {
				this.code = this.code + generarCodigoParaFlotantes(operando1, operando2, operador, 1);
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
				if (ultimaAsignado.contains("X"))
					codigo = codigo.replace("OP1", ultimaAsignado);
				else
					codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "ADD");
				pila.push(registro.getRegistro(1, "INTEGER"));
				codigo = codigo + saltoPorOverflow;
//				System.out.println("2-> codigo a agregar: "+saltoDeLinea+codigo);
				ultimaAsignado ="";
				this.code = this.code + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
					codigo = "ADD XX, OP2" + saltoDeLinea;
					codigo = codigo.replace("XX", operando1);
					codigo = codigo.replace("OP2", operando2);
					pila.push(operando1);
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
				codigo = codigo + saltoPorOverflow;
				this.code = this.code + codigo;
			}
			//SITUACION 4A - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				codigo = "ADD XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando2);
				codigo = codigo.replace("OP2", operando1);
				pila.push(operando2);
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
				codigo = codigo + "CWD" + saltoDeLinea;
				this.code = this.code + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
					codigo = "IMUL XX, OP2" + saltoDeLinea;
					codigo = codigo.replace("XX", operando1);
					codigo = codigo.replace("OP2", operando2);
					pila.push(operando1);
					codigo = codigo + "CWD" + saltoDeLinea;
					this.code = this.code + codigo;
			}
			//SITUACION 3 - (2 REGISTROS)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
				codigo = "IMUL XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(operando2, 0);
				codigo = codigo + "CWD" + saltoDeLinea;
				this.code = this.code + codigo;
			}
			//SITUACION 4A - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				codigo = "IMUL XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando2);
				codigo = codigo.replace("OP2", operando1);
				pila.push(operando2);
				codigo = codigo + "CWD" + saltoDeLinea;
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
				ultimaAsignado = registro.getRegistro(0, "INTEGER");
				codigo = codigo.replace("XX", ultimaAsignado);
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "SUB");
				registro.ocuparRegistro(operando2, 0);
				pila.push(operando1);
//				System.out.println("1->CODIGO A AGREGAR: "+saltoDeLinea+codigo);
				this.code = this.code + codigo;
			}
		}
		//DIVISION
		else if(operador.equals("/")) {
			//SITUACION 1 - (2 VARIABLES O CTES)
			if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = codigo + "MOV XX, OP2" + saltoDeLinea;
				codigo = codigo + plantillaComparacion;
				codigo = codigo + plantillaCompDistinto;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("RA", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("RB", "0");
				codigo = codigo.replace("Llabel", "divcero");
				codigo = codigo + plantillaOperacion;
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				codigo = codigo.replace("OP", "IDIV");
				if (Character.isDigit(operando2.charAt(0)))
					codigo = codigo.replace("IDIV AX, ", "IDIV _");
				else
					codigo = codigo.replace("IDIV AX, ", "IDIV ");
				pila.push(registro.getRegistro(1, "INTEGER"));	
				this.code = this.code + codigo;
			}
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
				codigo = codigo + plantillaComparacion;
				codigo = codigo + plantillaCompDistinto;
				codigo = codigo.replace("RA", operando2);
				codigo = codigo.replace("RB", "0");
				codigo = codigo.replace("Llabel", "divcero");
				// SECCION PARA BORAR 1ER OPERANDO DEL IDIV				
				codigo = codigo + "IDIV OP2" + saltoDeLinea;
				codigo = codigo.replace("OP2", operando2);
				///////////////////////////////////////////
				pila.push(operando1);
				this.code = this.code + codigo;
			}
			//SITUACION 3 - (2 REGISTROS)
			else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
				codigo = codigo + plantillaComparacion;
				codigo = codigo + plantillaCompDistinto;
				codigo = codigo.replace("RA", operando2);
				codigo = codigo.replace("RB", "0");
				codigo = codigo.replace("Llabel", "divcero");
				// SECCION PARA BORAR 1ER OPERANDO DEL IDIV				
				codigo = codigo + "IDIV OP2" + saltoDeLinea;
				codigo = codigo.replace("OP2", operando2);
				///////////////////////////////////////////
				pila.push(registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(operando2, 0);
				this.code = this.code + codigo;
			}
			//SITUACION 4B - (OPERANDO 2 ES UN REGISTRO)
			else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = codigo + plantillaComparacion;
				codigo = codigo + plantillaCompDistinto;
				codigo = codigo.replace("RA", operando2);
				codigo = codigo.replace("RB", "0");
				codigo = codigo.replace("Llabel", "divcero");
				codigo = codigo + plantillaOperacion;
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

		String operando1 = pila.pop();
		String operando2 = pila.pop();
		String codigo = "";
		String operador = "";

		//I(OPERANDO 2) = J (OPERANDO 1)
		// SITACION 1 - OPERANDO 1 (REG/AUX) Y OPERANDO 2 (VAR)
		if(this.getSimbolo(operando2) == null && this.getSimbolo(operando1) != null){
			// SITACION 1.1 - OPERANDO 1 (REG) Y OPERANDO 2 (VAR) SON INTEGER - VARIANTE DE REGISTROS
			if(registroInt(operando2) && this.getSimbolo(operando1).getTipoParametro().equals("INTEGER")) {
				codigo = plantillaAsignacion;
				codigo = codigo.replace("MOV VAR-REG, XX", "");
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP1", operando2);
				registro.ocuparRegistro(operando2, 0);
				this.main = this.main + codigo;
			}
			else if((registroFloat(operando2) && this.getSimbolo(operando1).getTipoParametro().equals("FLOAT")) || operando2.contains("E") ) {
				this.main = this.main + generarAsignacion(operando2, operando1, 0);
				ultimaAsignado = operando1; // EN GENERAL ES EL LADO IZQUIEROD DE LA ASIGNACION
				///////////////////////////////////////////
			}
			//CONVERSION - OPERANDO 2 VAR(INTEGER) Y OPERANDO 1 AUX(FLOAT)
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && registroFloat(operando2) ) {
				errorDeTipos("Se quiere asignar un FLOAT a un INTEGER",this.getSimbolo(operando1),this.getSimbolo(operando2) );
			}
			//CONVERSION - OPERANDO 2 AUX(FLOAT) Y OPERANDO 1 VAR(INTEGER)
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroInt(operando2) ) {
				this.main = this.main + generarAsignacion(operando2, operando1, 1);
				//// LINEA PARA NO PERDER EL ULTIMA ASIGNADO
				ultimaAsignado = operando1; // EN GENERAL ES EL LADO IZQUIERDO DE LA ASIGNACION
				////////////////////////////////////////////
			}
		}
		//public static String plantillaAsignacion = "MOV XX, OP1" + saltoDeLinea;
		// SITACION 2 - OPERANDO 1 (VAR) Y OPERANDO 2 (VAR)
		else if(this.getSimbolo(operando2) != null && this.getSimbolo(operando1) != null){
			//SITUACION 2.2 OPERANDO 1 Y 2 SON VAR Y SON INTEGER - VARIANTE DE REGISTROS
			if (this.getSimbolo(operando2).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando1).getTipoParametro().equals("INTEGER")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaAsignacion;
				/// CORRECCION DE ALGUNOS GUIONES BAJOS QUE NO APARECIAN
				if (Character.isDigit(operando2.charAt(0)))
					operando2 = "_"+operando2;
				if (operando2.charAt(0) == '-') {
					operando2 = "__"+operando2.replace("-", "");
				}
				//////////////////////////////////////////////////////
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
			else if(this.getSimbolo(operando2).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") ) {
				//// LINEA PARA NO PERDER EL ULTIMA ASIGNADO
				ultimaAsignado = operando1; // EN GENERAL ES EL LADO IZQUIERDO DE LA ASIGNACION
				////////////////////////////////////////////
				//////////////// SECCION PARA CORREGIR ALGUNOS PUNTOS QUE APARECIAN EN OPERANDOS FLOTANTES ///////////////////////
				if (operando2.charAt(operando2.length()-1) == '.') {
					operando2 = operando2.replace(".", "");
					operando2 = "_"+operando2+"_0";
				}
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				this.main = this.main + generarAsignacion(operando2, operando1, 0);
			}
			//CONVERSION - OPERANDO 2 VAR(INTEGER) Y OPERANDO 1 VAR(FLOAT)
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT") ) {
				errorDeTipos("Se quiere asignar un FLOAT a un INTEGER",this.getSimbolo(operando1),this.getSimbolo(operando2) );
			}
			//CONVERSION - OPERANDO 1 VAR(INTEGER) Y OPERANDO 2 VAR(FLOAT)
			else if(this.getSimbolo(operando2).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") ) {
				this.main = this.main + generarAsignacion(operando2, operando1, 1);
			}
		}
		//CONVERSION OPERANDO 2 REG (INTEGER) Y OPERANDO 1 AUX (FLOAT) 
		else if(this.getSimbolo(operando2) == null && this.getSimbolo(operando1) == null){
			if(registroInt(operando1) && registroFloat(operando2)) {
				errorDeTipos("Se quiere asignar un FLOAT a un INTEGER",this.getSimbolo(operando1),this.getSimbolo(operando2) );
			}
			else if(registroFloat(operando2) && registroFloat(operando1)) {
				this.main = this.main + generarAsignacion(operando2, operando1, 0);
			}
			else if(registroInt(operando2) && registroFloat(operando1)) {
				this.main = this.main + generarAsignacion(operando2, operando1, 1);
			}
		}
		//CONVERSION OPERANDO 2 AUX (FLOAT) Y OPERANDO 1 VAR (FLOAT) 
		else if(this.getSimbolo(operando2) != null && this.getSimbolo(operando1) == null){
			if(registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				errorDeTipos("Se quiere asignar un FLOAT a un INTEGER",this.getSimbolo(operando1),this.getSimbolo(operando2) );
			}
			else if(this.getSimbolo(operando2).getTipoParametro().equals("FLOAT") && registroFloat(operando1)) {
				this.main = this.main + generarAsignacion(operando2, operando1, 0);
			}
			else if(registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				this.main = this.main + generarAsignacion(operando2, operando1, 1);
			}
		}
		
		if(this.getSimbolo(operando1) != null)
			if(this.getSimbolo(operando1).getLineaConv().size() > 0)
				this.getSimbolo(operando1).getLineaConv().remove(0);
	}
	
	private void getCodAsignacionProc(){
		
		// x = y => ope1 = x ; ope2 = y
		
		String operando1 = pila.pop();
		String operando2 = pila.pop();
		String codigo = "";
		String operador = "";

		//I(OPERANDO 2) = J (OPERANDO 1)
		// SITACION 1 - OPERANDO 1 (REG/AUX) Y OPERANDO 2 (VAR)
		if(this.getSimbolo(operando2) == null && this.getSimbolo(operando1) != null){
			// SITACION 1.1 - OPERANDO 1 (REG) Y OPERANDO 2 (VAR) SON INTEGER - VARIANTE DE REGISTROS
			if(registroInt(operando2) && this.getSimbolo(operando1).getTipoParametro().equals("INTEGER")) {
				codigo = plantillaAsignacion;
				codigo = codigo.replace("MOV VAR-REG, XX", "");
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP1", operando2);
				registro.ocuparRegistro(operando2, 0);
				this.code = this.code + codigo;
			}
			else if((registroFloat(operando2) && this.getSimbolo(operando1).getTipoParametro().equals("FLOAT")) || operando2.contains("E") ) {
				this.code = this.code + generarAsignacion(operando2, operando1, 0);
			}
			//CONVERSION - OPERANDO 2 VAR(INTEGER) Y OPERANDO 1 AUX(FLOAT)
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && registroFloat(operando2) ) {
				errorDeTipos("Se quiere asignar un FLOAT a un INTEGER",this.getSimbolo(operando1),this.getSimbolo(operando2) );
			}
			//CONVERSION - OPERANDO 2 AUX(FLOAT) Y OPERANDO 1 VAR(INTEGER)
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroInt(operando2) ) {
				this.code = this.code + generarAsignacion(operando2, operando1, 1);
			}
		}
		// SITACION 2 - OPERANDO 1 (VAR) Y OPERANDO 2 (VAR)
		else if(this.getSimbolo(operando2) != null && this.getSimbolo(operando1) != null){
			//SITUACION 2.2 OPERANDO 1 Y 2 SON VAR Y SON INTEGER - VARIANTE DE REGISTROS	
			if (this.getSimbolo(operando2).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando1).getTipoParametro().equals("INTEGER")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",operador), 1);
				codigo = plantillaAsignacion;
				//// CORECCION DE ALGUNOS GUIONES BAJOS QUE NO APARACIAN
				if (Character.isDigit(operando2.charAt(0)))
					operando2 = "_"+operando2;
				if (operando2.charAt(0)=='-')
					operando2 = "__"+operando2.replace("-", "");
				////////////////////////////////////////////////////////
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
			else if(this.getSimbolo(operando2).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") ) {
				if (operando2.charAt(0) == '-')
					operando2 = "__"+operando2.replace("-", "");
				this.code = this.code + generarAsignacion(operando2, operando1, 0);
			}
			//CONVERSION - OPERANDO 2 VAR(INTEGER) Y OPERANDO 1 VAR(FLOAT)
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT") ) {
				errorDeTipos("Se quiere asignar un FLOAT a un INTEGER",this.getSimbolo(operando1),this.getSimbolo(operando2) );
			}
			//CONVERSION - OPERANDO 1 VAR(INTEGER) Y OPERANDO 2 VAR(FLOAT)
			else if(this.getSimbolo(operando2).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") ) {
				//// CORECCION DE ALGUNOS GUIONES BAJOS QUE NO APARACIAN
				if (Character.isDigit(operando2.charAt(0)))
					operando2 = "_"+operando2;
				////////////////////////////////////////////////////////
				this.code = this.code + generarAsignacion(operando2, operando1, 1);
			}
		}
		//CONVERSION OPERANDO 2 REG (INTEGER) Y OPERANDO 1 AUX (FLOAT) 
		else if(this.getSimbolo(operando2) == null && this.getSimbolo(operando1) == null){
			if(registroInt(operando1) && registroFloat(operando2)) {
				errorDeTipos("Se quiere asignar un FLOAT a un INTEGER",this.getSimbolo(operando1),this.getSimbolo(operando2) );
			}
			else if(registroFloat(operando2) && registroFloat(operando1)) {
				this.code = this.code + generarAsignacion(operando2, operando1, 0);
			}
			else if(registroInt(operando2) && registroFloat(operando1)) {
				this.code = this.code + generarAsignacion(operando2, operando1, 1);
			}
		}
		//CONVERSION OPERANDO 2 AUX (FLOAT) Y OPERANDO 1 VAR (FLOAT) 
		else if(this.getSimbolo(operando2) != null && this.getSimbolo(operando1) == null){
			if(registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				errorDeTipos("Se quiere asignar un FLOAT a un INTEGER",this.getSimbolo(operando1),this.getSimbolo(operando2) );
			}
			else if(this.getSimbolo(operando2).getTipoParametro().equals("FLOAT") && registroFloat(operando1)) {
				this.code = this.code + generarAsignacion(operando2, operando1, 0);
			}
			else if(registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				this.code = this.code + generarAsignacion(operando2, operando1, 1);
			}
		}
		
		if(this.getSimbolo(operando1) != null)
			if(this.getSimbolo(operando1).getLineaConv().size() > 0)
				this.getSimbolo(operando1).getLineaConv().remove(0);
	}
	
	private void generarComparadores(String salto, String comparacion, String operando1, String operando2) {
		//SON DOS NUMEROS O CTE
		if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				String codigo = plantillaAsignacion;		
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",""), 1);
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando2);
				this.main = this.main + codigo + generarComparacion(salto, comparacion, 3, operando1, registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(registro.getRegistro(1, "INTEGER"), 0);
			}
			if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				// SECCION PARA RENOMBRAR BIEN VARIABLES EN ASSEMBLER /////////////////////
//				System.out.println("2-->OPERANDO1: "+operando1);
//				System.out.println("2-->OPERANDO2: "+operando2);
				if (operando1.contains(".")) {
					if (operando1.charAt(operando1.length()-1) == '.'){
						operando1 = operando1.replace(".", "");
						operando1 = "_"+operando1+"_0";
					}
					else {
						operando1 = operando1.replace(".", "_");
						operando1 = "_"+operando1;
					}
				}
				///////////////////////////////////////////////////////////////////////////
//				String codigo = plantillaAsignacion;		
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",""), 1);
//				codigo = codigo.replace("XX", registro.getRegistro(1, "FLOAT"));
//				codigo = codigo.replace("OP1", operando2);
//				codigo = codigo + "poronga" + saltoDeLinea;
				ultimaAsignado = operando2;
				this.main = this.main + generarComparacion(salto, comparacion, 0, operando1, registro.getRegistro(1, "FLOAT"));
				registro.ocuparRegistro(registro.getRegistro(1, "FLOAT"), 0);
			}
			//COVERSION
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				this.main = this.main + generarComparacion(salto, comparacion, 2, operando1, operando2);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.main = this.main + generarComparacion(salto, comparacion, 1, operando1, operando2);
			}
		}
		//SON UN NUMERO/CTE Y UN REG/AUX
		else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.registroInt(operando2)) {
				this.main = this.main + generarComparacion(salto, comparacion, 3, operando1, operando2);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.registroFloat(operando2)) {
				this.main = this.main + generarComparacion(salto, comparacion, 0, operando1, operando2);
			}
			//COVERSION
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroInt(operando2)) {
				this.main = this.main + generarComparacion(salto, comparacion, 2, operando1, operando2);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && registroFloat(operando2)) {
				this.main = this.main + generarComparacion(salto, comparacion, 1, operando1, operando2);
			}
		}
		//SON UN REG/AUX Y UN NUMERO/CTE
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
			if(this.registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				this.main = this.main + generarComparacion(salto, comparacion, 3, operando1, operando2);
			}
			else if(this.registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.main = this.main + generarComparacion(salto, comparacion, 0, operando1, operando2);
			}
			//COVERSION
			else if(registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				this.main = this.main + generarComparacion(salto, comparacion, 2, operando1, operando2);
			}
			else if( registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.main = this.main + generarComparacion(salto, comparacion, 1, operando1, operando2);
			}
		}
		//SON DOS REG/AUX
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
			if(this.registroInt(operando1) && this.registroInt(operando2)) {
				this.main = this.main + generarComparacion(salto, comparacion, 3, operando1, operando2);
			}
			else if(this.registroFloat(operando1) && this.registroFloat(operando2)) {
				this.main = this.main + generarComparacion(salto, comparacion, 0, operando1, operando2);
			}
			//COVERSION
			else if(this.registroFloat(operando1) && this.registroInt(operando2)) {
				this.main = this.main + generarComparacion(salto, comparacion, 2, operando1, operando2);
			}
			else if(this.registroInt(operando1) && this.registroFloat(operando2)) {
				this.main = this.main + generarComparacion(salto, comparacion, 1, operando1, operando2);
			}
		}
	}
	
	private void generarComparadoresProc(String salto, String comparacion, String operando1, String operando2) {
		//SON DOS NUMEROS O CTE
		if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null /*&& comparativa.contains(comparacion)*/) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				String codigo = plantillaAsignacion;		
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",""), 1);
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando2);
				this.code = this.code + codigo +generarComparacion(salto, comparacion, 3, corregirOperando(operando1), registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(registro.getRegistro(1, "INTEGER"), 0);
			}
			if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				///String codigo = plantillaAsignacion;		
//				System.out.println("1-->OPERANDO1: "+operando1);
//				System.out.println("1-->OPERANDO2: "+operando2);
//				System.out.println("1-->CODIGO A AGREGAR SIN CAMBIO DE OPERANDOS: "+ saltoDeLinea + codigo);
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT",""), 1);
//				codigo = codigo.replace("XX", registro.getRegistro(1, "FLOAT"));
//				codigo = codigo.replace("OP1", operando2);
//				System.out.println("1-->CODIGO A AGREGAR CON CAMBIO DE OPERANDOS: "+ saltoDeLinea + codigo);
				ultimaAsignado = operando2;
//				System.out.println("ALPHA--GAMMA: "+ultimaAsignado);
				this.code = this.code + generarComparacion(salto, comparacion, 0, corregirOperando(operando1), registro.getRegistro(1, "FLOAT"));
				registro.ocuparRegistro(registro.getRegistro(1, "FLOAT"), 0);
			}
			//COVERSION
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				this.code = this.code + generarComparacion(salto, comparacion, 2, corregirOperando(operando1), operando2);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.code = this.code + generarComparacion(salto, comparacion, 1, corregirOperando(operando1), operando2);
			}
		}
		//SON UN NUMERO/CTE Y UN REG/AUX
		else if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null && comparativa.contains(comparacion)) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.registroInt(operando2)) {
				this.code = this.code + generarComparacion(salto, comparacion, 3, corregirOperando(operando1), operando2);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.registroFloat(operando2)) {
				this.code = this.code + generarComparacion(salto, comparacion, 0, corregirOperando(operando1), operando2);
			}
			//COVERSION
			else if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && registroInt(operando2)) {
				this.code = this.code + generarComparacion(salto, comparacion, 2, corregirOperando(operando1), operando2);
			}
			else if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && registroFloat(operando2)) {
				this.code = this.code + generarComparacion(salto, comparacion, 1, corregirOperando(operando1), operando2);
			}
		}
		//SON UN REG/AUX Y UN NUMERO/CTE
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null && comparativa.contains(comparacion)) {
			if(this.registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				this.code = this.code + generarComparacion(salto, comparacion, 3, corregirOperando(operando1), operando2);
			}
			else if(this.registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.code = this.code + generarComparacion(salto, comparacion, 0, corregirOperando(operando1), operando2);
			}
			//COVERSION
			else if(registroFloat(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				this.code = this.code + generarComparacion(salto, comparacion, 2, corregirOperando(operando1), operando2);
			}
			else if( registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				this.code = this.code + generarComparacion(salto, comparacion, 1, corregirOperando(operando1), operando2);
			}
		}
		//SON DOS REG/AUX
		else if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null && comparativa.contains(comparacion)) {
			if(this.registroInt(operando1) && this.registroInt(operando2)) {
				this.code = this.code + generarComparacion(salto, comparacion, 3, corregirOperando(operando1), operando2);
			}
			else if(this.registroFloat(operando1) && this.registroFloat(operando2)) {
				this.code = this.code + generarComparacion(salto, comparacion, 0, corregirOperando(operando1), operando2);
			}
			//COVERSION
			else if(this.registroFloat(operando1) && this.registroInt(operando2)) {
				this.code = this.code + generarComparacion(salto, comparacion, 2, corregirOperando(operando1), operando2);
			}
			else if(this.registroInt(operando1) && this.registroFloat(operando2)) {
				this.code = this.code + generarComparacion(salto, comparacion, 1, corregirOperando(operando1), operando2);
			}
		}
	}
	
	public String generarInstruccionesFLOAT(String operando1, String operando2, String operacionARIT, int conv) {
		// FORMATEO PREVIO DE LOS OPERANDOS
		if (operando1.charAt(0) != '@') { operando1 = "_"+operando1; }
		if (operando2.charAt(0) != '@') { operando2 = "_"+operando2; }
		if (operando1.contains("@") && (operando1.charAt(0)=='_')) { operando1 = operando1.replace("_", ""); }
		if (operando2.contains("@") && (operando2.charAt(0)=='_')) { operando2 = operando2.replace("_", ""); }
		operando1 = operando1.replace("E", "e");
		operando2 = operando2.replace("E", "e");
		operando1 = operando1.replace("+", "");
		operando2 = operando2.replace("+", "");
		operando1 = operando1.replace("-", "");
		operando2 = operando2.replace("-", "");
		operando1 = operando1.replace(".", "_");
		operando2 = operando2.replace(".", "_");
		/// SECCION AGREGADA PARA SOLUCIONAR PROBLEMAS CUANDO ALGUN OPERANDO ES N.,
		if (operando1.charAt(operando1.length()-1) == '_')
			operando1 = operando1 + "0";
		if (operando2.charAt(operando2.length()-1) == '_')
			operando2 = operando2 + "0";
		/////////////////////////////////////////////////////////
		String formato = plantillaOperacionFloat;
		String cargar = plantillaCargaCompFLOAT;
		String comparativa = plantillaComparacionFloat;
		String saltito = saltoPorOverflow;
		String saltito0 = saltoDivCero;
		String variableAAgregar = plantillaAgregarVarFLOAT;
		String variableAAgregar2 = plantillaAgregarVarFLOAT;
		String compSimple = "cmp opio" + saltoDeLinea;
		String ceroALaPila = cargar0ALaPila;
		String auxiliar = generarVarAux();
		String auxiliar2 = generarVarAux();
		variableAAgregar = variableAAgregar.replace("VAR", auxiliar);	
		// NINGUNO SE TIENE QUE CONVERTIR (FLOAT-FLOAT)
		if (conv==0) {
			// REEMPLAZO DE LA OPERACION DE LA PILA
			formato = formato.replace("OpPila", "FLD");
			// REEMPLAZO DE APERACION ARITMETICA
			if (operacionARIT.equals("+")) { 
				formato = formato.replace("OpArit", "FADD");
				// GENERACION DE COMPROBACION DE OVERFLOW EN SUMAS
				formato = formato + cargar + comparativa + saltito;
				formato = formato.replace("carga", "FLD"); 
				formato = formato.replace("compa", "FCOMP"); 
				formato = formato.replace("op2", "_MAX"); 
				formato = formato.replace("carga", "FLD"); 
				formato = formato.replace(" aux", " "+ auxiliar2);
				variableAAgregar2 = variableAAgregar2.replace("VAR", auxiliar2);
				variableAAgregar2 = variableAAgregar2.replace("dd", "dw");
			}
			if (operacionARIT.equals("/")) { 
				formato = formato.replace("OpArit", "FDIV"); 
				formato = ceroALaPila + compSimple + comparativa + saltito0 + formato;
				formato = formato.replace("cmp", "FCOMP");
				formato = formato.replace("opio", operando2);
				formato = formato.replace(" aux", " "+ auxiliar2);				
				variableAAgregar2 = variableAAgregar2.replace("VAR", auxiliar2);
				variableAAgregar2 = variableAAgregar2.replace("dd", "dw");
			}
			if (operacionARIT.equals("-")) { 
				formato = formato.replace("OpArit", "FSUB"); 
			}
			if (operacionARIT.equals("*")) { 
				formato = formato.replace("OpArit", "FMUL"); 
			}
		}
		// SE TIENE QUE CONVERTIR OPRENDO1 (INTEGER-FLOAT)
		else if (conv==1) {
			// REEMPLAZO DE LA OPERACION DE LA PILA
			formato = formato.replace("OpPila", "FILD");
			// REEMPLAZO DE APERACION ARITMETICA
			if (operacionARIT.equals("+")) { 
				formato = formato.replace("OpArit", "FADD"); 
				// GENERACION DE COMPROBACION DE OVERFLOW EN SUMAS
				formato = formato + cargar + comparativa + saltito;
				formato = formato.replace("carga", "FLD"); 
				formato = formato.replace("compa", "FCOMP"); 
				formato = formato.replace("op2", "_MAX"); 
				formato = formato.replace("carga", "FLD"); 
				formato = formato.replace(" aux", " "+ auxiliar2);
				variableAAgregar2 = variableAAgregar2.replace("VAR", auxiliar2);
				// linea de cambio de tipo, puede no ir 
				variableAAgregar2 = variableAAgregar2.replace("dd", "dw");
				///////////////////////////////////////
				this.data = this.data + variableAAgregar2;
			}
			if (operacionARIT.equals("/")) { 
				formato = formato.replace("OpArit", "FDIV"); 
				formato = ceroALaPila + compSimple + comparativa + saltito0 + formato;
				formato = formato.replace("cmp", "FCOMP");
				formato = formato.replace("opio", operando2);
				formato = formato.replace(" aux", " "+ auxiliar2);
				formato = formato.replace(" aux", " "+ auxiliar2);
				variableAAgregar2 = variableAAgregar2.replace("VAR", auxiliar2);
				//this.data = this.data + variableAAgregar2;
				variableAAgregar2 = variableAAgregar2.replace("dd", "dw");
			}
			if (operacionARIT.equals("-")) { formato = formato.replace("OpArit", "FSUB"); }
			if (operacionARIT.equals("*")) { formato = formato.replace("OpArit", "FMUL"); }
		}
		// SE TIENE QUE CONVERTIR OPRENDO2 (FLOAT-INTEGER)
		else if (conv==2) {
			// REEMPLAZO DE LA OPERACION DE LA PILA
			formato = formato.replace("OpPila", "FLD");
			// REEMPLAZO DE APERACION ARITMETICA
			if (operacionARIT.equals("+")) { 
				formato = formato.replace("OpArit", "FIADD"); 
				// GENERACION DE COMPROBACION DE OVERFLOW EN SUMAS
				formato = formato + cargar + comparativa + saltito;
				formato = formato.replace("carga", "FLD"); 
				formato = formato.replace("compa", "FCOMP"); 
				formato = formato.replace("op2", "_MAX"); 
				formato = formato.replace("carga", "FLD"); 
				formato = formato.replace(" aux", " "+ auxiliar2);
				variableAAgregar2 = variableAAgregar2.replace("VAR", auxiliar2);
				// linea de cambio de tipo, puede no ir 
				variableAAgregar2 = variableAAgregar2.replace("dd", "dw");
				///////////////////////////////////////
			}
			if (operacionARIT.equals("/")) { 
				formato = formato.replace("OpArit", "FIDIV"); 
				formato = ceroALaPila + compSimple + comparativa + saltito0 + formato;
				formato = formato.replace("cmp", "FICOMP");
				formato = formato.replace("opio", operando2);
				formato = formato.replace(" aux", " "+ auxiliar2);
				variableAAgregar2 = variableAAgregar2.replace("VAR", auxiliar2);
				variableAAgregar2 = variableAAgregar2.replace("dd", "dw");
			}
			if (operacionARIT.equals("-")) { formato = formato.replace("OpArit", "FISUB"); variableAAgregar2 = variableAAgregar2.replace("VAR", "PORONGA");}
			if (operacionARIT.equals("*")) { formato = formato.replace("OpArit", "FIMUL"); }
		}
		// REEMPLAZO DE REGISTROS		
		formato = formato.replace("REG1", operando1);
		formato = formato.replace("REG2", operando2);
		formato = formato.replace("resul", auxiliar);
		formato = formato.replace("op1", auxiliar);  
		// AGREGAR VAIRABLE AL DATA
		this.data = this.data + variableAAgregar;
		if (operacionARIT.equals("/") || operacionARIT.equals("+"))
			this.data = this.data + variableAAgregar2;
		pila.push(auxiliar);
		return formato;
	}
	
	public String generarCodigoParaFlotantes(String operando1, String operando2, String operacion, int operandoAConvertir) {
		// Generacion cuando NO hay que convertir
		if (operandoAConvertir==0) {
			if (operacion.equals("+")) { return generarInstruccionesFLOAT(operando1, operando2, "+",0); }
			if (operacion.equals("-")) { return generarInstruccionesFLOAT(operando1, operando2, "-",0); }
			if (operacion.equals("/")) { return generarInstruccionesFLOAT(operando1, operando2, "/",0); }
			if (operacion.equals("*")) { return generarInstruccionesFLOAT(operando1, operando2, "*",0); }
		}
		// Generacion cuando SI hay que convertir
		else {
			// operando1 operacion operando2
			// Si se necesita convertir el operando1
			if (operandoAConvertir==1) {
				if (operacion.equals("+")) { return generarInstruccionesFLOAT(operando1, operando2, "+",1); }
				if (operacion.equals("-")) { return generarInstruccionesFLOAT(operando1, operando2, "-",1); }
				if (operacion.equals("/")) { return generarInstruccionesFLOAT(operando1, operando2, "/",1); }
				if (operacion.equals("*")) { return generarInstruccionesFLOAT(operando1, operando2, "*",1); }
			}
			// Si se necesita convertir el operando2
			else if (operandoAConvertir==2) {
				if (operacion.equals("+")) { return generarInstruccionesFLOAT(operando1, operando2, "+",2); }
				if (operacion.equals("-")) { return generarInstruccionesFLOAT(operando1, operando2, "-",2); }
				if (operacion.equals("/")) { return generarInstruccionesFLOAT(operando1, operando2, "/",2); }
				if (operacion.equals("*")) { return generarInstruccionesFLOAT(operando1, operando2, "*",2); }
			}
		}
		return null;
	}
		
	public String generarAsignacion(String operando1, String operando2, int caso) {
		String ardiente;
		if (caso == 0) {
			// no convertir nada
			// se tiene  que mover a un regitro antes de pasarlo a la varaible final
			// GENERACION DE COGIDO
			String aux = generarVarAux();
			String linea1 = plantillaCargaCompFLOAT;
			// REEMPLAZOD DE VARIABLES
			if (operando1.contains("E")) {
				operando1 = operando1.replace("E", "e");
				operando1 = operando1.replace("+", "");
				operando1 = operando1.replace(".", "_");
				
				if (operando1.charAt(0)=='-') {
					operando1 = "__"+operando1.substring(1,operando1.length());
				}
				else
					if (operando1.charAt(0)!='_')
						operando1 = "_"+operando1;
				operando1 = operando1.replace("-", "");
			}
			if (operando1.contains(".") ) {
				operando1 = "_"+operando1.replace(".", "_");
			}
			linea1 = linea1.replace("carga", "FLD");
			linea1 = linea1.replace("op1", operando1);
			linea1 = linea1.replace("compa", "FSTP");
			linea1 = linea1.replace("op2", operando2);
			// AGREGAR VARIABLE
			String variableData = plantillaAgregarVarFLOAT;
			variableData = variableData.replace("VAR", aux);
			this.data = this.data + variableData;
			// AGREGAR CODIGO
			ardiente = linea1;
			return ardiente;
		}
		if (caso ==1) {
//			System.out.println("1-->OPERANDO1: "+operando1);
//			System.out.println("1-->OPERANDO2: "+operando2);
			// Convertir lado derecho
			// GENERACION DE CODIGO
			String linea1 = plantillaAsignacion;
			String linea2 = plantillaCargaCompFLOAT;
			linea1 = linea1.replace("MOV XX, OP1"+saltoDeLinea, "");
			String aux = generarVarAux();
//			String variableData = plantillaAgregarVarFLOAT;
//			variableData = variableData.replace("VAR", aux);
//			variableData = variableData.replace("dd", "dw");
//			this.data = this.data + variableData;
			
			linea1 = linea1.replace("XX", aux);
//			registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER",""), 1);
//			linea1 = linea1.replace("OP1", registro.getRegistro(1, "INTEGER"));
//			linea1 = linea1.replace("OP1", aux);
			linea1 = linea1.replace("OP1", operando1);
			
			linea2 = linea2.replace("carga", "FILD");
			linea2 = linea2.replace("op1", operando1);
			linea2 = linea2.replace("compa", "FSTP");
			linea2 = linea2.replace("op2", operando2);
			
			ardiente = linea1 + linea2;
//			System.out.println(saltoDeLinea + ardiente + saltoDeLinea);
			return ardiente;
		}
		return null;
	}	
	
	public String generarComparacion(String salto, String comparacion, int caso, String reg1, String reg2) {
		// FLOAT comp FLOAT
		if (caso==0) { 
			if (reg1.contains("."))
				reg1 = reg1.replace(".", "_");
//			System.out.println("2-->OPERANDO1: "+reg1);
//			System.out.println("2-->OPERANDO2: "+reg2);
			return generarCodigoComparacion(salto, comparacion, caso, reg1, reg2); }
		// INTEGER comp FLOAT
		if (caso==1) { return generarCodigoComparacion(salto, comparacion, caso, reg1, reg2); }
		// FLOAT comp INTEGER
		if (caso==2) { return generarCodigoComparacion(salto, comparacion, caso, reg1, reg2); }
		// INTEGER comp INTEGER
		if (caso==3) { return generarSaltosInteger(comparacion, reg1, reg2).replace("label", salto); }
		return null;
	}
	
	public String generarCodigoComparacion(String salto, String comparacion, int caso, String reg1, String reg2) {
		String auxiliar = generarVarAux();
		String codigo = plantillaCargaCompFLOAT + plantillaComparacionFloat;
		codigo = codigo.replace("E", "");
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
		codigo = codigo.replaceAll(" aux", " "+auxiliar);
		////// NUEVA SECION BASADA EN LA ANTERIOR
		codigo = codigo.replace("op1", ultimaAsignado);
		ultimaAsignado = "";
		codigo = codigo.replace("op2", reg1);
		/////////////////////////////////////////
		// AGREGAR VARIABLE AL .data
		String variableData = plantillaAgregarVarFLOAT;
		variableData = variableData.replace("VAR", auxiliar);
		variableData = variableData.replace("dd", "dw");
		this.data = this.data + variableData;
		// RETORNA CODIGO QUE POSTERIORMENTE SE AGREGARA A .main, O A .code
//		System.out.println("3-->CODIGO A AGREGAR: "+ saltoDeLinea +codigo);
		return codigo;
	}
	
	//UTILIDADES
	//Me devuelve el Simbolo para poder saber el tipo (INTEGER - FLOAT) y el uso (CTE - ID).
	private Simbolo getSimbolo(String elemento) {
			String [] aux = elemento.split("\\@");
			Double flotante = 0.0;
			String flot = "";
			
			if(!aux[0].equals("")) {
				if(aux[0].charAt(0) >= '0' && aux[0].charAt(0) <= '9') {
					if(aux[0].contains("f") || aux[0].contains(".")){
						flotante = Double.parseDouble(aux[0].replace('f', 'E'));
						if (aux[0].contains("."))
							flot = String.valueOf(AS10_Verificar_Rango_Float.normalizar(flotante));
					}
				}
				if(!flot.equals(""))
					aux[0] = flot;
			}
			String n = "";
			if(!aux[0].equals("")) {
				if(aux[0].charAt(0) == '-')
						n = aux[0].replace("-", "");
				else
					n = aux[0];
			}
			if(compilador.Compilador.tablaSimbolo.get(n) != null) {
				for(int i=0; i<compilador.Compilador.tablaSimbolo.get(n).size(); i++)
					if(compilador.Compilador.tablaSimbolo.get(n).get(i).getUso().equals("CTE")){
						return compilador.Compilador.tablaSimbolo.get(n).get(i);
					}
					else if(compilador.Compilador.tablaSimbolo.get(n).get(i).getAmbito().equals(comprobarAlcance(elemento))){
						return compilador.Compilador.tablaSimbolo.get(n).get(i);
					}				
			}
			return null;		
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
	    				if(aux.get(j).getValor().equals(l.get(i).getValor()))
	    					r = false;
	    			}
	    			else if(aux.get(j).getUso().equals("CADENA")){
	    				if(aux.get(j).getValor().equals(l.get(i).getValor()))
	    					r = false;
		    		}
	    		}
	    		if(r)
	    			aux.add(l.get(i));
	    	}		
	    }
	    return aux;
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

	void errorDeTipos(String mensaje, Simbolo op1, Simbolo op2) {
		String msj1 = mensaje + " en la linea: " + op1.getLineaConv().get(0) + ".\n";
		String msj2 = "Descripcion: la asignacion de la variable " + op1.getAmbito() + " es de tipo INTEGER y se le quiere asignar algo del tipo FLOAT.";
		String msj3 = "Descripcion: el parametro formal " + op1.getAmbito() + " es de tipo INTEGER y se le quiere asignar un parametro real del tipo FLOAT.";
		
		if(op1.getTipo().equals("PARAM_PROC"))
			compilador.Compilador.errores.add(msj1 + msj3);
		else
			compilador.Compilador.errores.add(msj1 + msj2);
	}
	
	String getAmbitoProc(String ambitoGeneral) {
		String [] arreglo = ambitoGeneral.split("\\@");
		String aux = arreglo[arreglo.length-1];
		for(int i=0; i<arreglo.length-1; i++)
			aux = aux + "@" + arreglo[i];
		return aux;	
	}

	int getNsProc(String ambitoProc, String sval) {
		String [] arreglo = ambitoProc.split("\\@");
		String aux = sval;
		for(int j=0; j<arreglo.length-1; j++)
			aux = aux + "@" + arreglo[j];
		
		for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
			if(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(aux))
				return compilador.Compilador.tablaSimbolo.get(sval).get(i).getNs();
		}
		return 0;
	}

	int getNivelVar(String sval) {
		String [] arreglo = sval.split("\\@");
		return arreglo.length-1;
	}

	boolean dadoProcVerDeclaracionVar(String ambitoProc, String sval) {
		String [] ambitoProcedimiento = ambitoProc.split("\\@");
		String ambitoVar = sval;
		if(ambitoProcedimiento.length > 1) {
			for(int i=1; i<ambitoProcedimiento.length; i++) {
				ambitoVar =  ambitoVar + "@" + ambitoProcedimiento[i];
			}
			ambitoVar = ambitoVar + "@" + ambitoProcedimiento[0];
		}
		else {
			ambitoVar = ambitoVar + "@" + ambitoProc;
		}
		
		for(int i=0; i<compilador.Compilador.tablaSimbolo.get(sval).size(); i++){
			if(compilador.Compilador.tablaSimbolo.get(sval).get(i).getAmbito().equals(ambitoVar))
				if(compilador.Compilador.tablaSimbolo.get(sval).get(i).isDeclarada() && (compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("Var") || compilador.Compilador.tablaSimbolo.get(sval).get(i).getTipo().equals("PARAM_PROC")))
					return true;				
		}
		return false;				
	}

	String construirAmbito (String[] array) {
		String delimiter = "@";
	    return String.join(delimiter, array);
	}

	String[] construirAmbitoMenosUltimo (String[] array) {
		String aux = "";
		for(int j=0; j<array.length-1; j++)
			if(j==0)
				aux = array[j];
			else
				aux = aux + "@" + array[j];
		return aux.split("\\@");
		
	}

	String construirAmbitoMenosUltimoString (String array2) {
		String [] array = array2.split("\\@");
		String aux = "";
		for(int j=0; j<array.length-1; j++)
			if(j==0)
				aux = array[j];
			else
				aux = aux + "@" + array[j];
		String delimiter = "@";
	    return String.join(delimiter, aux);
		
	}

	boolean verificarAnidamientos(String ambitoProc, String ambitoVar, int ns) {	
		String proc = "";
		String [] aux = ambitoProc.split("\\@");
		for(int i=1; i<aux.length; i++)
			if(i==1)
				proc = aux[i];
			else
				proc = proc + "@" + aux[i];
		
		String var = "";
		String [] aux2 = ambitoVar.split("\\@");
		for(int i=1; i<aux2.length; i++)
			if(i==1)
				var = aux2[i];
			else
				var = var + "@" + aux2[i];
		
		proc = proc + "@" + aux[0];
		int nivel = 0;
		
		while(var.length() > 0) {
			if(var.equals(proc)) {
				if(ns >= nivel) {
					return true;
				}
				else {
					return false;
				}
			}
			var = construirAmbitoMenosUltimoString(var);
			nivel++;
		}
		
		return false;
	}

	String comprobarAlcance(String elemento) {
		String [] elementoAux = elemento.split("\\@");
		
		String ambito = "Main";
		
		for(int i=2; i<elementoAux.length; i++)
			ambito = ambito + "@" + elementoAux[i];
		
		String sval = elementoAux[0];
		
		String [] ambitoAux = ambito.split("\\@");
		String ambitoUsoVar = sval + "@" + ambito;
		String [] ambitoArr = ambito.split("\\@");
		boolean primero = true;
		for(int i = ambitoArr.length-1; i>=0; i--) {
			if(!ambitoArr[i].equals("Main")) {
				if(primero) {
					primero = false;
					if(dadoProcVerDeclaracionVar(getAmbitoProc(construirAmbito(ambitoAux)),sval)) {
						return sval + "@" + construirAmbito(ambitoAux);
					}
						
				}
				else {
					if(dadoProcVerDeclaracionVar(getAmbitoProc(construirAmbito(ambitoAux)),sval)) {
						if(verificarAnidamientos(getAmbitoProc(construirAmbito(ambitoAux)), ambitoUsoVar,getNsProc(construirAmbito(ambitoAux),ambitoArr[i]))) {
							return sval + "@" + construirAmbito(ambitoAux);
						}
					}
				}
			}
			else {
				if(dadoProcVerDeclaracionVar(getAmbitoProc(construirAmbito(ambitoAux)),sval)) {
					return sval + "@" + construirAmbito(ambitoAux);
				}
			}
			
			ambitoAux = construirAmbitoMenosUltimo(ambitoAux);
		}
		return "";		
	}
	
	public String toString(){
		this.generarData();
		// acomodar .data
		this.data = this.data.replaceAll("_-", "_");
		assembler = assembler + encabezado;
		assembler = assembler + data;
		assembler = assembler + code;
		assembler = assembler + inicioMainAssembler;
		assembler = assembler + main;
		assembler = assembler + finMainAssembler;
		return assembler;
	}
}