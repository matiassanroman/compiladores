package compilador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

/*
 ***** Controlos en timepo de ejecucion *****   
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
	private ArrayList<String> registros;
	private ArrayList<Character> estados;
	
	private ArrayList<String> registros32Bits;
	private ArrayList<String> registros16Bits;
	private ArrayList<String> registros8BitsBajos;
	private ArrayList<String> registros8BitAaltos;
	
	private ArrayList<String> operadoresBinarios = new ArrayList<String>(Arrays.asList("+","-","*","/","<","<=",">",">=","==","!=","="));
	private ArrayList<String> operadoresUnarios = new ArrayList<String>(Arrays.asList("OUT","BF","BI","CALL"));
	
	private Stack<String> pila;
	
	private String data = "";
	private String code = "";
	
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
			"mensaje db \"Mensaje por pantalla\", 0";
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
	public static String plantillaMostrarPorPantalla = "invoke MessageBox, NULL, addr VAR, addr mensaje, MB_OK";
	public static String plantillaAgregarVar = "";

	private String generarVarAux() {
		String var = varAux + numeroVar ;
		numeroVar += 1;
		return var;
	}
	
	public GeneradorAssembler() {
		this.assembler = "";
		this.conversor = new Conversor();
		this.registros = new ArrayList<String>(Arrays.asList("R1","R2","R3","R4"));
		this.estados  = new ArrayList<Character>(Arrays.asList('L','L','L','L'));
		this.pila = new Stack<String>();
		this.registros32Bits     = new ArrayList<String>(Arrays.asList("EAX","EBX","ECX","EDX"));
		this.registros16Bits     = new ArrayList<String>(Arrays.asList( "AX", "BX", "CX", "DX"));
		this.registros8BitsBajos = new ArrayList<String>(Arrays.asList( "AL", "BL", "CL", "DL"));
		this.registros8BitAaltos = new ArrayList<String>(Arrays.asList( "AH", "BH", "CH", "DH")); 
	}
	
	public void generarData(HashMap<String, Integer> tablaSibolos){
		// volcar toda la tabla de simbolos 
		// a la variable data que luego se agregara a la salida final
		// revisar formato
	}
	
	public void generarAssembler(PolacaInversa polaca) {
		// generar assembler a partir de la polaca
		ArrayList<Par> listaPolaca = polaca.getPolaca();
		for (int i = 0; i < listaPolaca.size(); i++) {
			String elemento = listaPolaca.get(i).getValor();
			if ( !this.operadoresUnarios.contains(elemento) && !this.operadoresBinarios.contains(elemento)) 
				pila.add(elemento);
			if (operadoresUnarios.contains(elemento)) {
				String cadena = pila.pop();
				// formato data: HelloWorld db "Hello World!", 0
				// formato code: invoke MessageBox, NULL, addr HelloWorld, addr HelloWorld, MB_OK
				this.data = this.data + cadena + " db \"" + cadena + "\", 0" + saltoDeLinea;
				this.code = this.code + "invoke MessageBox, NULL, addr " + cadena + ", addr " + cadena + ", MB_OK" + saltoDeLinea;
			}
		}
	}
}
