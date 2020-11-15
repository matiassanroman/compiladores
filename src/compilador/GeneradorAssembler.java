package compilador;

import java.util.HashMap;

public class GeneradorAssembler {
	// agregar una variable archivo que es donde se va 
	// guardar el assembler
	private String assembler;
	
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
			".data";
	
	public GeneradorAssembler() {
		this.assembler = "";
	}
	
	public void generarData(HashMap<String, Integer> tablaSibolos){
		// volcar toda la tabla de simbolos 
		// al .data del assembler
		// revisar formato
	}
	
	public void generarAssembler(PolacaInversa polaca) {
		// generar assembler a partir de la polaca
		
	}
}
