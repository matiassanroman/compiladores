package accionesSemanticas;

public class AS8_delete_Caracter extends AccionSemantica{

	// Ejecuta AS que borar el ultim caracter agregado
	public int execute(StringBuffer buffer, char c) {
		int posABorrar = buffer.length()-1;
		buffer.deleteCharAt(posABorrar);
		return 0;
	}

	// Como ya se borro el ultimo caracter, no se necesita 
	// acomodar la linea con lo cual retorna false 
	public boolean acomodarLinea() {
		return false;
	}

}
