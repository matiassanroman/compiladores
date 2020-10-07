package accionesSemanticas;

public class AS6_Fin_Simbolo extends AccionSemantica{
	
	/*
	Identifica que el token es un simbolo devolviendolo. Estos simbolos son:
	'{' '}' '(' ')' ',' ';' '+' '-' '*' '/' '.' '_' '-'
	*/ 
	
	// Se agrega al buffer el caracter 'c', corresponde 
	// al simbolo que se desea leer e identificar
	public int execute(StringBuffer buffer, char c) {
		buffer = new StringBuffer();				
		return c;
	}
	
	// No es necesario acomodar la linea
	// con lo cual retorna false
	public boolean acomodarLinea(){
		return false;
	}

}
