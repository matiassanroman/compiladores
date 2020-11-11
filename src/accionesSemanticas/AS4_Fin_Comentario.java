package accionesSemanticas;

import compilador.Compilador;

public class AS4_Fin_Comentario extends AccionSemantica{

	// Al terminar de reconocer el comentario lo descarta 
	// inicializando de nuevo el buffer y pedirle al 
	// analizador que limpie el buffer con su propio metodo
	public int execute(StringBuffer buffer, char c) {
		if(buffer.toString().length() >= 2 && buffer.toString().charAt(0) == '%' && buffer.toString().charAt(1) == '%') {
			buffer = new StringBuffer();
			Compilador.limpiarBuffer();
			System.out.println("Reconocio comentario");
			return 0;
		}
		else {
			System.out.println("Error en la linea "+compilador.Compilador.nroLinea+": COMENTARIO mal escrito");
			buffer.delete(0, buffer.length());
			return 0;
		}
	}

	// Como el comentario de descarta, no es necesario 
	// acomodar la linea, con lo cual se retorna false
	public boolean acomodarLinea() {
		return true;
	}

}
