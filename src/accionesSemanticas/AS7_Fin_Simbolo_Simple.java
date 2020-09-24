package accionesSemanticas;

public class AS7_Fin_Simbolo_Simple extends AccionSemantica{

	/* 
	Estos son un caso especial de los simbolos de un solo caracter, que se da al reconocer =, !, < o >.
	Ya que se lee el siguiente caracter, y se podria dar el caso que venga un =.
	Implicitamente devolvemos el ascii del simbolo.
	*/

	// Se retorna el caracter el primera posicion
	// del buffer de lectura, correspondiente al 
	// simbolo simple
	public int execute(StringBuffer buffer, char c) {
		return buffer.charAt(0);
	}

	// Como se va a haber leido el siguiente caracter
	// se necesitaré acomodar la linea, con lo cual 
	// se retorna true
	public boolean acomodarLinea() {
		return true;
	}

}
