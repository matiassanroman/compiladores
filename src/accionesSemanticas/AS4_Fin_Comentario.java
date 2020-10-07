package accionesSemanticas;

import compilador.Compilador;

public class AS4_Fin_Comentario extends AccionSemantica{

	// Al terminar de reconocer el comentario lo descarta 
	// inicializando de nuevo el buffer y pedirle al 
	// analizador que limpie el buffer con su propio metodo
	public int execute(StringBuffer buffer, char c) {
		buffer = new StringBuffer();
		Compilador.limpiarBuffer();
		System.out.println("Reconocio comentario");
		return 0;
	}

	// Como el comentario de descarta, no es necesario 
	// acomodar la linea, con lo cual se retorna false
	public boolean acomodarLinea() {
		return false;
	}

}
