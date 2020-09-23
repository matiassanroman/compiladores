package accionesSemanticas;

public class AS6_Fin_Simbolo extends AccionSemantica{
	
	/*
	Identifica que el token es un simbolo devolviendolo. Estos simbolos son:
	'{' '}' '(' ')' ',' ';' '+' '-' '*' '/' 
	*/ 
	@Override
	public int execute(StringBuffer buffer, char c) {
		buffer = new StringBuffer();				
		return c;
	}
	
	@Override
	public boolean acomodarLinea(){
		return false;
	}

}
