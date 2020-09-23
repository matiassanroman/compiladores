package accionesSemanticas;

public class AS7_Fin_Simbolo_Simple extends AccionSemantica{

	/* 
	Estos son un caso especial de los simbolos de un solo caracter, que se da al reconocer =, !, < o >.
	Ya que se lee el siguiente caracter, y se podria dar el caso que venga un =.
	Implicitamente devolvemos el ascii del simbolo.
	*/

	@Override
	public int execute(StringBuffer buffer, char c) {
		
		return buffer.charAt(0);
		
	}

	@Override
	public boolean acomodarLinea() {
		return true;
	}

}
