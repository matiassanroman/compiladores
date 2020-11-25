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
	
	private String data = "";
	private String code = "";
	private String main = "";
	
	public static int numeroVar = 1;
	public static String varAux = "@aux";
	
	public static String saltoDeLinea = "\r\n";
	
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
	
	public static String inicioMainAssembler = "main:" + saltoDeLinea;
	
	public static String finMainAssembler = "invoke ExitProcess, 0" + saltoDeLinea
										  +	"end main";	
	
	public static String plantillaSuma = "MOV XX, OP1" + saltoDeLinea 
			 						   + "ADD XX, OP2" + saltoDeLinea
			 						   + "MOV VAR-REG, XX" + saltoDeLinea;
	
	public static String plantillaResta = "MOV XX, OP1" + saltoDeLinea 
			   							+ "ADD XX, OP2" + saltoDeLinea
			   							+ "MOV VAR-REG, XX" + saltoDeLinea;
	
	public static String plantillaMultiplicacion = "MOV XX, OP1" + saltoDeLinea 
												 + "MUL XX, OP2" + saltoDeLinea
												 + "MOV VAR-REG, XX" + saltoDeLinea;
	
	public static String plantillaDivision = "MOV XX, OP1" + saltoDeLinea 
			 							   + "DIV XX, OP2" + saltoDeLinea
			 							   + "MOV VAR-REG, XX" + saltoDeLinea;
	
	public static String plantillaAsignacion = "MOV XX, OP1" + saltoDeLinea
											 + "MOV VAR-REG, XX" + saltoDeLinea;
	
	public static String plantillaCompIgual = "";
	public static String plantillaCompDistinto = "";
	public static String plantillaCompMayor = "";
	public static String plantillaCompMenor = "";
	public static String plantillaCompMayorIgual = "";
	public static String plantillaCompMenorIgual = "";
	
	public static String plantillaMostrarPorPantallaCode = "invoke MessageBox, NULL, addr VAR, addr mensaje, MB_OK" + saltoDeLinea; //titulo de la ventana
	public static String plantillaMostrarPorPantallaData = "VAR db \"CADENA\", 0" + saltoDeLinea; // texto dentro la ventana de mesaje
	
	public static String plantillaEtiquetaProcedimiento = "ETIQUETA:" + saltoDeLinea;
	public static String plantillaCall = "call F" + saltoDeLinea;
	public static String plantillaAgregarVar = "";

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
		//No va mas esto
		
		this.generarData(tablaSimbolo);
		//System.out.println(this.code);
		this.generarAssembler(polaca);
	}
	
	
	
	private void generarInvocacion(String etiqueta, String destino) {
		String nombreProc = etiqueta.replace("PROC ","");
		String paraCode = plantillaEtiquetaProcedimiento.replace("ETIQUETA", nombreProc);
		destino = destino + paraCode;
	}
	
	private void generarCall(String nombreProc, String destino){
		String invocacion = plantillaCall.replace("F", "nombreProc");
		destino = destino + invocacion;
	}
	
	private void generarMensajePorPantalla(String cadenaAMostrar, String destino){
		String codigo = plantillaMostrarPorPantallaCode.replace("VAR", cadenaAMostrar);
		String paraData = plantillaMostrarPorPantallaData.replace("VAR", cadenaAMostrar);
		paraData = paraData.replace("CADENA", cadenaAMostrar);
		destino = destino + codigo;
		this.data = this.data + paraData;
	}
	
	public void generarData(Hashtable<String,ArrayList<Simbolo>> tablaSimbolo){
		// volcar toda la tabla de simbolos 
		// a la variable data que luego se agregara a la salida final
		// revisar formato
		
		Set<String> keys = tablaSimbolo.keySet();
	    Iterator<String> itr = keys.iterator();
	    String str;
	    
	    while (itr.hasNext()) { 
	       str = itr.next();
	       ArrayList<Simbolo> aux =  eliminarRepetidos(tablaSimbolo.get(str));
    	   for(int i=0; i<aux.size(); i++) {
    		   if (aux.get(i).getTipo().equals("Proc")) { 
    			   this.data = this.data + "_" +aux.get(i).getAmbito() + " DW  ?" + saltoDeLinea;
    		   } 
    		   else if(aux.get(i).getUso().equals("ID")) {
				   if(aux.get(i).getTipoParametro().equals("INTEGER"))
					   this.data = this.data + "_" +aux.get(i).getAmbito() + " DW  ?" + saltoDeLinea;
			   		if(aux.get(i).getTipoParametro().equals("FLOAT"))
			   			this.data = this.data + "_" +aux.get(i).getAmbito() + " DD  ?" + saltoDeLinea;
    		   }
    		   else if(aux.get(i).getUso().equals("CTE")) {
				   if(aux.get(i).getTipo().equals("int"))
					   this.data = this.data + "_" + aux.get(i).getValor() + " DW " + aux.get(i).getValor() + saltoDeLinea;
				   if(aux.get(i).getTipo().equals("float"))
					   this.data = this.data + "_" + aux.get(i).getValor() + " DD " + aux.get(i).getValor() + saltoDeLinea;
    		   }
    		   else if(aux.get(i).getUso().equals("CADENA")) {
    				   this.data = this.data + "_" + aux.get(i).getValor() + " DB " + aux.get(i).getValor() + " , 0" + saltoDeLinea;			   
    		   }	   
    		   else if(aux.get(i).getUso().equals("AUX")) {
				   if(aux.get(i).getTipo().equals("int"))
					   this.data = this.data + "@" + aux.get(i).getValor() + " DW " + aux.get(i).getValor() + saltoDeLinea;
				   if(aux.get(i).getTipo().equals("float"))
					   this.data = this.data + "@" + aux.get(i).getValor() + " DD " + aux.get(i).getValor() + saltoDeLinea;
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
						generarMensajePorPantalla(cadena, this.main);
				}
				if (elemento.equals("CALL")){              // Si es CALL generar llamado
					String nProc = pila.pop();
					generarCall(nProc, this.main);
				}
			}
			if (operadoresBinarios.contains(elemento)) {
				if (elemento.equals("+")) {
					this.getCodSuma();
				}
				if (elemento.equals("=")) {
					this.getCodAsignacion();
				}
			}
			
			
			
			
			
			
			
			
			if (elemento.contains(PROC)) {                            // Si el PROC. agrego todo ese codigo con su nombre de pro en la seccion .code
				generarInvocacion(elemento, this.main);
				i++;
				while (listaPolaca.get(i).getValor() != "RET") {  	  // mientras no llegue RET agrego todo ese codigo a la funcion en el code
					elemento = listaPolaca.get(i).getValor();
					if ( !this.operadoresUnarios.contains(elemento) && !this.operadoresBinarios.contains(elemento) && !elemento.contains(PROC) )  // Si son ids o ctes las apilo
						pila.add(elemento);
					if (operadoresUnarios.contains(elemento)) {    // Si es un operador unario
						if (elemento.equals("OUT")) {              // Si es OUT generar mensaje
								String cadena = pila.pop();
								generarMensajePorPantalla(cadena, this.code);
						}
						if (elemento.equals("CALL")){              // Si es CALL generar llamado
							String nProc = pila.pop();
							generarCall(nProc, this.code);
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

	public String toString(){
		assembler = assembler + encabezado;
		assembler = assembler + data;
		assembler = assembler + code;
		assembler = assembler + inicioMainAssembler;
		assembler = assembler + main;
		assembler = assembler + finMainAssembler;
		return assembler;
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
	
	private void getCodSuma() {
		
		// x + y => ope1 = x ; ope2 = y
		String operando2 = pila.pop();
		String operando1 = pila.pop();
		
		String codigo = "";
		
		if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
			//SEGUIMIENTO DE REGISTROS - INTEGER - SITUACION 1 - (2 VARIABLES O CTES)
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("INTEGER"), 1);
				codigo = plantillaSuma;
				codigo = codigo.replace("MOV VAR-REG, XX", "");
				codigo = codigo.replace("XX", registro.getRegistro(1, "INTEGER"));
				codigo = codigo.replace("OP1", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(registro.getRegistro(1, "INTEGER"));
				this.main = this.main + codigo;
			}
			//VARIABLES AUXILIARES - FLOAT - LOS DOS OPERANDOS
			if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT") && this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT"), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				this.main = this.main + generarIstruccionesVariableAux(reg, operando1, operando2, "+");
				registro.ocuparRegistro(reg, 0);
			}
			//SINO CONVERTIR
			else {
				//
			}
		}
		if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
			//SITUACION 2 - (OPERANDO 1 ES UN REGISTRO)
			if(registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				codigo = "ADD XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(operando1);
				this.main = this.main + codigo;
			}
			//VARIABLES AUXILIARES - FLOAT - OPERANDO 1: AUX
			if(this.getSimbolo(operando2).getTipoParametro().equals("FLOAT")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT"), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				this.main = this.main + generarIstruccionesVariableAux(reg, operando1, operando2, "+");
				registro.ocuparRegistro(reg, 0);
			}
		}
		//SITUACION 3 - (2 REGISTROS) - (DEPENDE DEL REGISTRO QUE MUESTRE HAY QUE CONVERTIR O NO)
		if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) == null) {
			if(registroInt(operando1) && registroInt(operando2)) {
				codigo = "ADD XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando1);
				codigo = codigo.replace("OP2", operando2);
				pila.push(registro.getRegistro(1, "INTEGER"));
				registro.ocuparRegistro(operando2, 0);
				this.main = this.main + codigo;
			}
			//SINO CONVERTIR
			else {
				//
			}
		}
		if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) == null) {
			//SITUACION 4 - (OPERANDO 2 ES UN REGISTRO)
			if(registroInt(operando2) && this.getSimbolo(operando1).getTipoParametro().equals("INTEGER")) {
				codigo = "ADD XX, OP2" + saltoDeLinea;
				codigo = codigo.replace("XX", operando2);
				codigo = codigo.replace("OP2", operando1);
				pila.push(operando2);
				this.main = this.main + codigo;
			}
			//VARIABLES AUXILIARES - FLOAT - OPERANDO 2: AUX
			if(this.getSimbolo(operando1).getTipoParametro().equals("FLOAT")) {
				registro.ocuparRegistro(registro.getPrimerRegistroLibre("FLOAT"), 1);
				String reg = registro.getRegistro(1, "FLOAT");
				this.main = this.main + generarIstruccionesVariableAux(reg, operando1, operando2, "+");
				registro.ocuparRegistro(reg, 0);
			}
		}		
	}
	
	private void getCodAsignacion(){
		
		// x = y => ope1 = x ; ope2 = y
		String operando2 = pila.pop();
		String operando1 = pila.pop();
		String codigo = "";
		//SITACION 1
		if(this.getSimbolo(operando1) == null && this.getSimbolo(operando2) != null) {
			if(registroInt(operando1) && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				codigo = plantillaAsignacion;
				codigo = codigo.replace("MOV VAR-REG, XX", "");
				codigo = codigo.replace("XX", operando2);
				codigo = codigo.replace("OP1", operando1);
				registro.ocuparRegistro(operando1, 0);
				this.main = this.main + codigo;
				registro.imprimir();
			}
		}
		
		//SITUACION 2
		if(this.getSimbolo(operando1) != null && this.getSimbolo(operando2) != null) {
			if(this.getSimbolo(operando1).getTipoParametro().equals("INTEGER") && this.getSimbolo(operando2).getTipoParametro().equals("INTEGER")) {
				registro.imprimir();
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
				registro.imprimir();
				this.main = this.main + codigo;
			}
		}
	
			//"MOV XX, OP1" + saltoDeLinea
			// + "MOV VAR-REG, XX" + saltoDeLinea;
			
		
	}
	
	//Me devuelve el Simbolo para poder saber el tipo (INTEGER - FLOAT) y el uso (CTE - ID).
	private Simbolo getSimbolo(String elemento) {
		
		String [] aux = elemento.split("\\:");
		/*
		for(int i=0; i<compilador.Compilador.tablaSimbolo.get(aux[0]).size(); i++)
			if(compilador.Compilador.tablaSimbolo.get(aux[0]).get(i).getAmbito().equals(elemento))
				return compilador.Compilador.tablaSimbolo.get(aux[0]).get(i);
		*/
		if(compilador.Compilador.tablaSimbolo.get(elemento) != null) {
			for(int i=0; i<compilador.Compilador.tablaSimbolo.get(elemento).size(); i++)
				if(compilador.Compilador.tablaSimbolo.get(elemento).get(i).getValor().equals(elemento))
					return compilador.Compilador.tablaSimbolo.get(aux[0]).get(i);
		}
		return null;
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
		testo = testo.replace("VAR-REG", auxAux);
		testo = testo.replace("XX", reg);
		testo = testo.replace("OP1", operando1);
		testo = testo.replace("OP2", operando2);
		data= data.concat(auxAux + " dd " + "?" + saltoDeLinea);
		pila.push(auxAux);
		return testo;
	}
	
	private boolean registroInt(String op1) {
		if(op1.equals("AX") || op1.equals("BX") || op1.equals("CX") || op1.equals("DX")) 
			return true;
		else return false;
	}
	
	private boolean registroFloat(String op1) {
		if(op1.equals("EAX") || op1.equals("EBX") || op1.equals("ECX") || op1.equals("EDX")) 
			return true;
		else return false;
	}
	
}
