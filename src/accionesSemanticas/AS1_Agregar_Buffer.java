package accionesSemanticas;

public class AS1_Agregar_Buffer extends AccionSemantica{

	// Agrega el caracter 'c' al buffer (string)
	public int execute(StringBuffer buffer, char c) {
		//System.out.println("AGREGAR C: " + c);
		//System.out.println("AGREGAR BUFFER: " + buffer.toString());
		buffer.append(c);
		return 0;
	}

	// No se necesita acomodar la linea, entonces
	// retorna 'false'
	public boolean acomodarLinea() {
		return false;
	}

}
