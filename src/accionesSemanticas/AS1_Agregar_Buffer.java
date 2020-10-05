package accionesSemanticas;

public class AS1_Agregar_Buffer extends AccionSemantica{

	// Agrega el caracter 'c' al buffer (string)
	public int execute(StringBuffer buffer, char c) {
		//System.out.println("caracter: " + (int)c + " " + c);
		buffer.append(c);
		return 0;
	}

	// No se necesita acomodar la linea, entonces
	// retorna 'false'
	public boolean acomodarLinea() {
		return false;
	}

}
